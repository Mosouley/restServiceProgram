/*
 * Bignon
 */
package com.bootcamp.rest.exception;

import javax.ws.rs.core.Response;


public class SuccessMessage {
    public static Response message(String msg) {
            return Response.status(200).entity(msg).build();
	}
}
