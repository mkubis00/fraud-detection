package org.kubis.risk;

import org.kubis.dto.TransactionRiskAssessment;

import java.util.List;

/***
 * Domain representation of finished transaction evaluation
 */
public record RiskEvaluation(
        RiskLevel level,
        List<String> reasons
) {
    public TransactionRiskAssessment toDto(String bin, String requestId) {
        return new TransactionRiskAssessment(level, reasons, bin, requestId);
    }
}
