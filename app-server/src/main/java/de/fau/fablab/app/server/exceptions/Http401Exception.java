package de.fau.fablab.app.server.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



public class Http401Exception extends WebApplicationException {

    public Http401Exception() {
        super(Response.Status.UNAUTHORIZED);
    }

    public Http401Exception(String message) {
        super(Response.status(Response.Status.UNAUTHORIZED).entity(new ExceptionMessageWrapper(message)).type(MediaType.APPLICATION_JSON).build());
    }
}
