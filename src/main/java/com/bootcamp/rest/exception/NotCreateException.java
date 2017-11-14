/*
 * Bignon
 */
package com.bootcamp.rest.exception;

import javax.ws.rs.core.Response;

public class NotCreateException extends Exception{
    public static Response notCreateException(String msg,Exception e) {
            return Response.status(401).entity(msg+"!\n"+e).build();
	}
}
