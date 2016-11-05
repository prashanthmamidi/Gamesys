package org.gamesys;

import org.gamesys.exception.UserBlackListedException;
import org.gamesys.model.User;
import org.gamesys.model.UserDTO;
import org.gamesys.service.ExclusionService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.gamesys.service.ValidationService.validateExistingUser;

@Singleton
@Path("/user")
public class UserResource {

    private final ExclusionService exclusionService;
    @Inject
    public UserResource(ExclusionService exclusionService) {
        this.exclusionService = exclusionService;
    }

    @POST
    @Path("/register")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response registerUser(@Valid UserDTO userDTO) {

        if (isUserBlackListed(userDTO)) {
            throw new UserBlackListedException("User is black lised");
        }
        validateExistingUser(userDTO.getSsn());

        return Response.status(Response.Status.CREATED)
                .entity(new User(UUID.randomUUID(), userDTO.getUserName(), userDTO.getDob().toString(), userDTO.getSsn()))
                .build();
    }

    private boolean isUserBlackListed(@Valid UserDTO userDTO) {
        return !exclusionService.validate(dateToString(userDTO.getDob()), userDTO.getSsn());
    }

    private String dateToString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        return df.format(date);
    }


}
