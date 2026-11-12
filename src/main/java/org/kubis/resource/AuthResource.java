package org.kubis.resource;

import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.time.Duration;

@Path("/auth/token")
public class AuthResource {

    @POST
    @PermitAll
    public String getToken() {
        return Jwt.issuer("transaction-risk-service")
                .subject("demo-client")
                .groups("transaction-risk-check")
                .expiresIn(Duration.ofMinutes(10))
                .sign();
    }
}
