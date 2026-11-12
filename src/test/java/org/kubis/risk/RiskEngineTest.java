package org.kubis.risk;

import org.junit.jupiter.api.Test;
import org.kubis.domain.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


class RiskEngineTest {

    private final static String COUNTRY_MISMATCH = "Country mismatch";
    private final static String PREPAID_CARD = "Prepaid card";

    private final static RiskContext context = new RiskContext(
            new Transaction("12345678", new BigDecimal("100"), "POL"), Optional.empty());

    @Test
    void shouldSumPointsFromTriggeredRulesAndMapToRiskLevel() {
        RiskEngine engine = new RiskEngine(List.of(
                rule(21, COUNTRY_MISMATCH),
                rule(30, PREPAID_CARD),
                notTriggered()));

        RiskEvaluation evaluation = engine.evaluate(context);

        assertEquals(RiskLevel.MEDIUM, evaluation.level());
        assertEquals(List.of(COUNTRY_MISMATCH, PREPAID_CARD), evaluation.reasons());
    }

    @Test
    void shouldReturnVeryLowWhenNoRuleIsTriggered() {
        RiskEngine engine = new RiskEngine(List.of(notTriggered(), notTriggered()));

        RiskEvaluation evaluation = engine.evaluate(context);

        assertEquals(RiskLevel.VERY_LOW, evaluation.level());
        assertEquals(List.of(), evaluation.reasons());
    }

    private static RiskRule rule(int points, String reason) {
        return riskContext -> Optional.of(new RuleResult(points, reason));
    }

    private static RiskRule notTriggered() {
        return riskContext-> Optional.empty();
    }
}
