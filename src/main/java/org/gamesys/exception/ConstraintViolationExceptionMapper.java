package org.gamesys.exception;

import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        String propertyName = exception.getConstraintViolations().stream().findFirst().get().getPropertyPath().toString();
        return Response.status(400).entity(new ErrorResponse(propertyName + " is invalid")).build();
    }
}