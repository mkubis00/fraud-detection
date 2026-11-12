package org.kubis.mastercard;

import com.mastercard.developer.oauth.OAuth;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;

import java.nio.charset.StandardCharsets;

/***
 * Class that adds the OAuth 1.0a signature header required by the Mastercard API to outgoing requests.
 */
@ApplicationScoped
public class MasterCardOAuthFilter implements ClientRequestFilter {

    @Inject
    MasterCardCredentials credentials;

    @Override
    public void filter(ClientRequestContext requestContext) {
        String payload = requestContext.hasEntity() ? (String) requestContext.getEntity() : null;

        String authHeader = OAuth.getAuthorizationHeader(
                requestContext.getUri(),
                requestContext.getMethod(),
                payload,
                StandardCharsets.UTF_8,
                credentials.getConsumerKey(),
                credentials.getSigningKey());

        requestContext.getHeaders().putSingle(HttpHeaders.AUTHORIZATION, authHeader);
    }
}
