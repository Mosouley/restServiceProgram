/*
 * Bignon
 */
package com.bootcamp.rest.exception;

import javax.ws.rs.core.Response;

public class AuthentificationException_1 extends Exception{
     	
	public static Response auth(String msg,Object obj) {
            return Response.status(401).entity(msg+"!\n"+obj).build();
	}
	
}
