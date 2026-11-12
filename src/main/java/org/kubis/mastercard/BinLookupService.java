package org.kubis.mastercard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.cache.CacheResult;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.kubis.exception.ApplicationException;

import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class BinLookupService {

    @Inject
    @RestClient
    MasterCardBinClient client;

    @Inject
    ObjectMapper objectMapper;

    @CacheResult(cacheName = "bin-lookup")
    public Optional<BinDetails> lookup(String bin) {
        Log.debugf("Sending request to MasterCard for bin %s", bin);
        String body = toJson(Map.of("accountRange", bin));
        Optional<BinDetails> binDetails = client.searchByAccountRange((body))
                .stream().findFirst();
        Log.debugf("Request sent to MasterCard for bin %s is completed", bin);
        return binDetails;
    }

    private String toJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new ApplicationException("Cannot serialize Mastercard request", ex);
        }
    }
}
