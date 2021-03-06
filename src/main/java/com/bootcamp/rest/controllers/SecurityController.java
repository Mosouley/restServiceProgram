
package com.bootcamp.rest.controllers;

import com.bootcamp.entities.User;
import com.bootcamp.jpa.UserRepository;
import com.bootcamp.rest.exception.AuthentificationException;
import com.bootcamp.rest.exception.SuccessMessage;
import com.bootcamp.rest.exception.TokenNotGenerateException;
import com.bootcamp.rest.security.JavaJsonWebToken;
import com.bootcamp.service.crud.UserCRUD;
import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 *
 * @author Bignon
 */
@Path("v0/security")
public class SecurityController {


    UserRepository ur = new UserRepository("databasePU");
    Response resp;
    JavaJsonWebToken jwt = new JavaJsonWebToken();
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response login(User user) throws SQLException{
        
        //authentifiaction
        if(UserCRUD.findByPropertyUnique("login", user.getLogin()).equals(UserCRUD.findByPropertyUnique("login", user.getPwd())) ){
            String iat ="BootcampToken";
            long tm = 1800000; //30 min
            String subject=user.toString();
            
            // generation du token
            String token="";
            try {
              token=jwt.createJWT(iat, subject, tm); 
              resp=SuccessMessage.message("\n Authentification réussie.Token bien généré ! \n Retenez bien ce token \n"+token);
            } catch (Exception e) {
                resp=TokenNotGenerateException.generateTokenException();
            }
                                   
        }else  {
            resp = AuthentificationException.auth("Echec de l'authentification");
        }
        return resp;
    }
    
    //methode qui verifie si le token est bon
    //return true si c'est bon
    public boolean checkToken(String token){
        try {
            jwt.parseJWT(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    // verification du token avant n'importe quelle tache dans n'importe qu'elle service
    /*  
             try {
            jt.parseJWT(token);
            SuccessMessage.message("\n Verification du token avec succes !");
    
                // to do  . . .
    
        } catch (Exception e) {
            TokenVerifyException.tokenException();
        }

        */
}
