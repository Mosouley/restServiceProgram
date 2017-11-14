/*
 * Bignon
 */
package com.bootcamp.rest.exception;

import javax.ws.rs.core.Response;

/**
 *
 * @author Bignon
 */
public class ReturnResponse {
    public static Response object(String msg,Object obj) {
            return Response.status(200).entity(msg+"\n"+obj).build();
	}
}
