/**
 * signature Programme
 */
package com.bootcamp.rest.controllers;



import com.bootcamp.entities.PhaseProgramme;
import com.bootcamp.entities.Programme;
import com.bootcamp.model.Programmes;
import com.bootcamp.service.crud.ProgrammeCRUD;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author soul
 */
@Api(value = "programmes", description = "Operations about programmes")
@Path("programmes")
public class ProgrammeRessource {


    
    //Annotation JAX-RS2 that helps gather all the injected info
    @Context
    public UriInfo uriInfo;

    /**
     *URI giving the access to the whole signature
     * @param start
     * @param size
      * @return
     * @throws java.sql.SQLException
     */
    @ApiOperation(value = "To retrieve all the programmes and "
            + "scroll to the previous and the next thanks following links", response = Programmes.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "invalid page given"),
        @ApiResponse(code = 404,message = "Programmes non found")})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Programmes getProgrammes(@ApiParam(value = "start",required = true)  @QueryParam("start") int start,                  //
            @QueryParam("size") @DefaultValue("2") int size  ) throws SQLException{

       
            /**
             * //definition des URI permettant d'obtenir le previous et le next de
             * tout programme aussi les parametres de pagination sont inclus
             * dans la requete http avec un minimum de 2
             */
            //Utilisation de la classe abstraite uribuilder
            
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.queryParam("start", "{start}");
            builder.queryParam("size", "{size}");

            //Method to access all the program
            List<Programme> programmes;

            programmes = ProgrammeCRUD.findAll();


            //a collection of links to use hateaos
            ArrayList<Link> links = new ArrayList<>();
            //creation d'une liste de programme, constituee seulement des programmes
            //respectant les criteres de start and size
            ArrayList<Programme> list = new ArrayList<>();

            //on fait remplir la liste des programmes respectant les queryparam de facon synchronisee pour eviter les appels concurrents
            
            synchronized (programmes) {
                int i = 0;
                for (Programme programme : programmes) {
                    //on ajoute les programmes un a un a la liste jusqu'a ce que la taille definie soit  atteinte
                    if (i >= start && i < start + size) {
                        list.add(programme);
                    }
                    i++;
                }
            }

            // next link
            if (start + size < programmes.size()) {
                int next = start + size;
                URI nextUri = builder.clone().build(next, size);
                Link nextLink = Link.fromUri(nextUri)
                        .rel("next").type("application/json").build();
                links.add(nextLink);
            }
            // previous link
            if (start > 0) {
                int previous = start - size;
                if (previous < 0) {
                    previous = 0;
                }
                URI previousUri = builder.clone().build(previous, size);
                Link previousLink = Link.fromUri(previousUri)
                        .rel("previous")
                        .type("application/json").build();
                links.add(previousLink);
            }

            //Pour chaque programme definir son link
            //mise en oeuvre de l'implementation Hateoas
            //boucle for sur chaque programme
            //Introduire son champ self (lui-meme) qui fait reference a son lien
            for (Programme programme : programmes) {
                //pour chaque bailleur dans la liste
                //trouver l'URI vers sa ressource provenant de la recherche par id se trouvant dans la methode getbyId

                Link lien = Link.fromUri(uriInfo.getBaseUriBuilder()
                        .path(getClass())       //permet d'avoir l'uri de base
                        .path(getClass(), "getById")        //permet d'avoir l'URI de la methode recherche
                        .build(programme.getReference()))   //ajoute la reference du programme comme queryparam
                        .rel(programme.getNom())
                        .type("GET").build();               //faire le build pour resoudre le lien
                //definir le lien du programme ainsi obtenu
                programme.setSelf(lien);

                //pour chaque programme trouve lier le programme a son lien
                Response.accepted(programme)
                        .links(programme.getSelf())
                        .build();
                //setter pour fixer le lien vers cette ressource
                programme.setSelf(lien);
            }

            Programmes listProgrammes = new Programmes();
            //on la remplit de la liste provenant de la base de donnee
            listProgrammes.setProgrammes(list);
            //on lui met ses liens
            listProgrammes.setLinks(links);
            //on retourne la reponse a la requete
            return listProgrammes;
        
 
    }
    @ApiOperation(value = "To retrieve info on a given programme with its reference")
    @GET
    @Path("/programme/{ref}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("ref") int ref) throws SQLException {

        Programme programme = ProgrammeCRUD.findByPropertyUnique("reference", ref);

        if (programme != null) {
            programme.setSelf(
                    Link.fromUri(uriInfo.getAbsolutePath())
                    .rel("self")
                    .type("GET")
                    .build());
            return Response.accepted(programme).links(programme.getSelf()).build();
        } else {
            return Response.status(404).entity(programme).build();
        }
    }

    /**
     * methode de recherche sur un nombre limites de champs
     * @param ref
     * @param fields
     * @return
     * @throws SQLException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws IntrospectionException
     * @throws InvocationTargetException
     */
    @ApiOperation(value = "To retrieve specific info on a given program")
    @GET
    @Path("/programme/param/{ref}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByIdParam(@PathParam("ref") int ref, @QueryParam("fields") String fields) throws SQLException, IllegalArgumentException, IllegalAccessException, IntrospectionException, InvocationTargetException {
       //Trouver les champs de recherche Separes par une virgule
        String[] fieldArray = fields.split(",");

        //retrouver le seul programme dont la reference est
        Programme programme = ProgrammeCRUD.findByPropertyUnique("reference", ref);
        //  Definissons un Map
        Map<String, Object> responseMap = new HashMap<>();

        //meta programming permettant d'obtenir tous les champs declares sur la classe programme
        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(Programme.class).getPropertyDescriptors();

        //pour chaque proprete ou attribut parcourir ses methodes getter
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

            Method method = propertyDescriptor.getReadMethod();

            //si la verification est concluante, donc le client souhaite obtenir ce champ
            if (check(fieldArray, propertyDescriptor.getName())) {
                responseMap.put(propertyDescriptor.getName(), method.invoke(programme));
            }

        }

        //A la fin de la boucle le Map contiendra tous les champs demandes leur nom, et le resultat de leur methode getter
        //on retourne donc la tableau de chmap dans la requete
        if(responseMap.isEmpty()){      //Aucun des champs indiques n'appartient au service programme
            return Response.noContent().build();
        }else{
        return Response.status(200).entity(responseMap).build();
        }
    }

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create a new programme with its gathered info")
    public Response create(Programme programme) {
        String output = " Felicitations objet cree avec succes : ";
        ProgrammeCRUD.create(programme);
        return Response.status(200).entity(output + programme.getNom()).build();
    }
//
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "update a given program")
    public Response update(Programme programme) {
        String output = " Felicitations Mise a jour effectuee avec succes pour : ";
        ProgrammeCRUD.update(programme);
        return Response.status(200).entity(output + programme.getNom()).build();

    }
      @POST
    @Path("/create/phase")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "To create a new phase for a given program")
    public Response addPhase(PhaseProgramme phaseprogramme) {
        String output = " Felicitations objet cree avec succes : ";
        ProgrammeCRUD.addPhase(phaseprogramme);
        return Response.status(200).entity(output + phaseprogramme.getNom()).build();
    }
//
    /**
     * methode permettant de verifier si un attribut fait partie d'une liste de champ indiques
     * @param fields
     * @param field
     * @return
     */
    private boolean check(String[] fields, String field) {

        for (String field1 : fields) {
            if (field.equals(field1)) {
                return true;
            }
        }
        return false;
    }
}
