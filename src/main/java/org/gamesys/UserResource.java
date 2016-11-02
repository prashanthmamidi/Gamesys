package org.gamesys;

import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Singleton
@Path("/user")
public class UserResource {

    @POST
    @Path("/register")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response registerUser(@Valid UserDTO userDTO) {
        // check if the userDTO already exists based on ssn
        // balck-list userDTO - Exclusion service based on ssn and dob
        return Response.status(Response.Status.CREATED)
                .entity(new User(UUID.randomUUID(), userDTO.getUserName(), userDTO.getDob(), userDTO.getSsn()))
                .build();
    }


}
