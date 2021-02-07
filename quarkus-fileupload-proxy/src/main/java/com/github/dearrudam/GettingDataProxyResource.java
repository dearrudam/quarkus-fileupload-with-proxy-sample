package com.github.dearrudam;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/payload")
public class GettingDataProxyResource {

    @Inject
    @RestClient
    GettingDataService gettingDataService;

    @GET
    @Path("{filename}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Retry(delay = 1000l)
    @CircuitBreaker(requestVolumeThreshold = 4)
    @Fallback(fallbackMethod = "fallBackGetPayload")
    public Response getPayload(@PathParam("filename") String fileName,
                               @QueryParam("size") @DefaultValue("4000") Long size,
                               @QueryParam("chunkSize") @DefaultValue("4000") Long chunkSize) {
        try {
            return gettingDataService.getPayload(fileName,size,chunkSize);
        } catch (WebApplicationException ex) {
            return ex.getResponse();
        }
    }

    public Response fallBackGetPayload(String fileName,
                                       Long size,
                                       Long chunkSize) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
    }
}