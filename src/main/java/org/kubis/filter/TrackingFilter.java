package org.kubis.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import org.jboss.logging.MDC;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;

import java.util.UUID;


/***
 * Class handles creating/checking X-REQUESTID parameter.
 */
public class TrackingFilter {

    public static final String HEADER = "X-REQUESTID";
    public static final String MDC_KEY = "requestId";
    private static final int MAX_LENGTH = 64;

    @ServerRequestFilter
    public void onRequest(ContainerRequestContext request) {
        String id = request.getHeaderString(HEADER);
        if (id == null || id.isBlank() || id.length() > MAX_LENGTH) {
            id = UUID.randomUUID().toString();
        }
        request.setProperty(MDC_KEY, id);
        MDC.put(MDC_KEY, id);
    }

    @ServerResponseFilter
    public void onResponse(ContainerRequestContext request, ContainerResponseContext response) {
        Object id = request.getProperty(MDC_KEY);
        if (id != null) {
            response.getHeaders().putSingle(HEADER, id);
        }
        MDC.remove(MDC_KEY);
    }
}
