package org.kubis.exception;

import io.quarkus.logging.Log;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import org.jboss.logging.MDC;

// Global exception handler was written as simple as it could be for the purpose of the task
public class GlobalExceptionHandler {

    @ServerExceptionMapper
    public Response handleUnexpected(Exception ex) {
        Log.error("Error ", ex);
        return error(Response.Status.INTERNAL_SERVER_ERROR, "There was an error during execution");
    }

    private Response error(Response.Status status, String message) {
        String requestId = (String) MDC.get("requestId");
        return Response.status(status)
                .entity(new ErrorResponse(requestId, status.getStatusCode(), message))
                .build();
    }

    public record ErrorResponse(String requestId, int status, String message) {}
}
