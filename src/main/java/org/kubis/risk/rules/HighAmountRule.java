package org.kubis.risk.rules;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.kubis.risk.RiskContext;
import org.kubis.risk.RiskRule;
import org.kubis.risk.RuleResult;

import java.math.BigDecimal;
import java.util.Optional;

/***
 * Rule which checks amount of the transaction
 */
@ApplicationScoped
public class HighAmountRule implements RiskRule {

    // This two values could be defined in application.properties and read depending on the application profile
    // Same things applies for other rules
    private final int points = 25;
    private final BigDecimal threshold = new BigDecimal(5000);

    @Override
    public Optional<RuleResult> evaluate(RiskContext context) {
        if (context.transaction().amount().compareTo(threshold) > 0) {
            Log.debugf("Amount exceed %s. %s points added", threshold, points);
            return Optional.of (new RuleResult(points, "Amount exceed " + threshold));
        }
        return Optional.empty();
    }
}
