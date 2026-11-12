package org.kubis.dto;


import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.kubis.risk.RiskLevel;

import java.util.List;

@Schema(description = "Evaluated risk of the send transaction")
public record TransactionRiskAssessment(
        @Schema(description = "Risk level of the transaction", examples = "HIGH")
        RiskLevel riskLevel,

        @Schema(description = "Risk factors detected for this transaction", example = "[\"Card issued in USA but used in POL\", \"Card is prepaid\"]")
        List<String> reasons,

        @Schema(description = "Card BIN", examples = "12345678")
        String bin,

        @Schema(description = "Request ID", examples = "1234567890-abcd-1234")
        String requestId
) {}
