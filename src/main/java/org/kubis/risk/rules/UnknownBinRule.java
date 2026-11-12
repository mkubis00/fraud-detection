package org.kubis.risk.rules;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.kubis.risk.RiskContext;
import org.kubis.risk.RiskRule;
import org.kubis.risk.RuleResult;

import java.util.Optional;

/***
 * Rule which checks if BIN is known value
 */
@ApplicationScoped
public class UnknownBinRule implements RiskRule {

    private final int points = 100;

    @Override
    public Optional<RuleResult> evaluate(RiskContext context) {
        if (context.binDetails().isEmpty()) {
            Log.debugf("Card BIN is unknown . %s points added", points);
            return Optional.of (new RuleResult(points, "Card BIN is unknown"));
        }
        return Optional.empty();
    }
}
