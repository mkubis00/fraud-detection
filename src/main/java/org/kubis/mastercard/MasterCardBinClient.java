package org.kubis.mastercard;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.POST;

import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "mastercard-bin")
@RegisterProvider(MasterCardOAuthFilter.class)
@Path("/bin-ranges")
public interface MasterCardBinClient {

    @POST
    @Path("/account-searches")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    List<BinDetails> searchByAccountRange(String jsonBody);
}
