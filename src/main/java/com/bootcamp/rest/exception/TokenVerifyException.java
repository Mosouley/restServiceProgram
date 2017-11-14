/*
 * Bignon
 */


package com.bootcamp.rest.exception;

import javax.ws.rs.core.Response;


public class TokenVerifyException {
     	
	public static Response tokenException(String msg) {
            return Response.status(498).entity("Token non valide ! \n"+msg).build();
	}
}
