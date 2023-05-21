package com.spotlight.platform.userprofile.api.web.exceptionmappers;

import com.spotlight.platform.userprofile.api.core.exceptions.BadRequestException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {
    @Override
    public Response toResponse(BadRequestException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .build();
    }
}
