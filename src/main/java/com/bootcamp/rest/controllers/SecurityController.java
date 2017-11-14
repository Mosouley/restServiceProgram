
package com.bootcamp.rest.controllers;

import com.bootcamp.entities.User;
import com.bootcamp.jpa.UserRepository;
import com.bootcamp.rest.exception.AuthentificationException;
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
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response login(User user) throws SQLException{
        
        //authentifiaction
        if(UserCRUD.findByPropertyUnique("login", user.getLogin()).equals(UserCRUD.findByPropertyUnique("login", user.getPwd())) ){
        
        }else  {
            resp = AuthentificationException.auth("Echec de l'authentification");
        }
        return resp;
    }
}
