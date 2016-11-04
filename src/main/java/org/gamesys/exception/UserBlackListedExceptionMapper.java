package org.gamesys.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class UserBlackListedExceptionMapper implements ExceptionMapper<UserBlackListedException> {
    @Override
    public Response toResponse(UserBlackListedException e) {
        return  Response.status(401).entity(new ErrorResponse("User is Black listed")).build();
    }
}
