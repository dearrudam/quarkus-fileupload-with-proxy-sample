package com.github.dearrudam;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RegisterRestClient
public interface GettingDataService {

    @GET
    @Path("/payload/{fileName}")
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    public Response getPayload(@PathParam("fileName") String fileName,
                               @QueryParam("size") Long size,
                               @QueryParam("chunkSize") Long chunkSize) throws ProcessingException, WebApplicationException;

}
