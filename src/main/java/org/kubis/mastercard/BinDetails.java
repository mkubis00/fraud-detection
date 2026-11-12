package org.kubis.mastercard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Representation of the data received from MasterCard BIN Lookup API
 * @param country describes where the card was issued.
 * @param fundingSource describes founding source
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record BinDetails(
        Country country,
        String fundingSource
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Country(String alpha3) {}

    public String issuerCountryAlpha3() {
        return country != null ? country.alpha3() : null;
    }
}
