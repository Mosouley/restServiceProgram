/**
 * Realisation de quelques tests
 */
package com.bootcamp.rest.tests;


import com.bootcamp.entities.PhaseProgramme;
import com.bootcamp.entities.Programme;
import com.bootcamp.enums.EtatProgramme;
import com.bootcamp.rest.controllers.ProgrammeRessource;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.testng.annotations.Test;

/**
 *
 * @author soul
 */
public class GenerateTableTest  {
 
//we make no test for retrieving programmes because the resource uses uriinfo
    //which can not be used out of a servlet context and can only be test using a client

     @Test
    public void getServiceProgrammeById() throws SQLException{
        ProgrammeRessource uriProg=new ProgrammeRessource();
        try {
            //get the entire specified data for program 1
            System.out.println("contenu demande: " + uriProg.getByIdParam(1, "nom,description").toString());
        } catch (IllegalArgumentException | IllegalAccessException | IntrospectionException | InvocationTargetException ex) {
            Logger.getLogger(GenerateTableTest.class.getName()).log(Level.SEVERE, null, ex);
           
        }
    }

    @Test
    public void postServiceProgramme(){
    
      //initialisation de la signature  
     ProgrammeRessource uriProg=new ProgrammeRessource();
     
     String nom="Luttre contre violence conjugale via web service";

     String description="zero tolerance aux violences faites aux femmes via webservice"; //description du programme

     int idObjectif=3; //id to recevoir permettant de faire des recherche sur un objectif precis

     double budprevionnel=100000000;

     double coutreel=300000000;

    Date dateDebutPrev= new Date(2014/01/01);

   Date dateFinPrev=new Date(2014/12/31);

    Date dateDebutReel=new Date(2014/03/01);

    Date dateFinReel=new Date(2015/06/01);

    PhaseProgramme nPhase1=new PhaseProgramme();
    nPhase1.setNomPhase("Phase cree depuis le web service");
    PhaseProgramme nPhase2=new PhaseProgramme();
    nPhase1.setNomPhase("Phase cree depuis le web service 2");
    PhaseProgramme nPhase3=new PhaseProgramme();
    nPhase1.setNomPhase("Phase cree depuis le web service 3");
    PhaseProgramme nPhase4=new PhaseProgramme();
    nPhase1.setNomPhase("Phase cree depuis le web service 4");
    List<PhaseProgramme> nomPhases=new ArrayList<>();
    //add some phases tu this project
        uriProg.addPhase(nPhase1);
        uriProg.addPhase(nPhase2);
        uriProg.addPhase(nPhase3);
        uriProg.addPhase(nPhase4);


    PhaseProgramme phaseActuelle=nPhase3; // the programme is at begenning

    EtatProgramme etatProgramme=EtatProgramme.EXECUTION;

        Programme nvoProg=new Programme();
        nvoProg.setBudprevionnel(budprevionnel);
        nvoProg.setNom(nom);
        nvoProg.setDescription(description);
        nvoProg.setPhaseActuelle(phaseActuelle);
        nvoProg.setIdObjectif(idObjectif);
        nvoProg.setCoutreel(coutreel);
        nvoProg.setDateDebutPrev(dateDebutPrev);
        nvoProg.setDateDebutReel(dateDebutReel);
        nvoProg.setDateFinPrev(dateFinPrev);
        nvoProg.setDateFinReel(dateFinReel);
        nvoProg.setNomPhases(nomPhases);
        nvoProg.setEtatProgramme(etatProgramme);
        uriProg.create(nvoProg);
        
   
    }
}
