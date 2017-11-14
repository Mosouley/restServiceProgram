/*
 * Bignon
 */
package com.bootcamp.rest.exception;

import javax.ws.rs.core.Response;


public class UnknownException extends Exception{
    public static Response unknownException(Exception e) {
            return Response.status(401).entity(e).build();
	}
}
