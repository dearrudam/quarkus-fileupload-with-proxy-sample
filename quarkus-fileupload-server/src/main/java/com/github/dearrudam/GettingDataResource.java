package com.github.dearrudam;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;


@ApplicationScoped
@Path("/payload")
public class GettingDataResource {

    @Inject
    DataProvider dataProvider;

    AtomicBoolean notFound=new AtomicBoolean(Boolean.FALSE);

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response shouldNotFound(JsonObject payload) {
        this.notFound.set(payload.getBoolean("status",false));
        return Response.ok(Json.createObjectBuilder().add("status",this.notFound.get()).build()).build();
    }

    @GET
    @Path("{fileName}")
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    public Response getPayload(@PathParam("fileName") String fileName,
                               @QueryParam("size") @DefaultValue("4000") Long size,
                               @QueryParam("chunkSize") @DefaultValue("4000") Long chunkSize) {

        if ( notFound.get()){
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }

        final DataProvider.Data data = dataProvider.getData(chunkSize,size);

        StreamingOutput output = new StreamingOutput() {
            @Override
            public void write(OutputStream output) throws IOException, WebApplicationException {
                    data.write(output);
            }
        };

        return Response.ok(output)
                .header("content-disposition", "attachment; filename = " + fileName + ".txt")
                .build();
    }


}