/*
 * Bignon
 */
package com.bootcamp.rest.exception;

import javax.ws.rs.core.Response;

public class TokenNotGenerateException {
    public static Response generateTokenException() {
            return Response.status(409).entity("Le token n'a pu etre génére !").build();
	}
}
