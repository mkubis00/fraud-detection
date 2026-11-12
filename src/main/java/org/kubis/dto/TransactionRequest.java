package org.kubis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.kubis.domain.Transaction;

import java.math.BigDecimal;

@Schema(description = "Transaction which risk is going to be evaluated")
public record TransactionRequest(
        @Schema(description = "Card BIN", examples = "12345678")
        @NotBlank @Pattern(regexp = "\\d{8,11}", message = "The BIN must be 8 to 11 digits long")
        String bin,

        @Schema(description = "Amount of the transaction", examples = "100")
        @NotNull @Positive BigDecimal amount,

        @Schema(description = "Country code in which transaction is taking place", examples = "POL")
        @NotNull @Pattern(regexp = "[A-Z]{3}", message = "Country code must consist of 3 uppercase letters") String userLocation
) {
        public Transaction toDomain() {
                return new Transaction(bin, amount, userLocation);
        }
}
