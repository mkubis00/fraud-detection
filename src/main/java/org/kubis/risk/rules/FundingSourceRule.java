package org.kubis.risk.rules;

import jakarta.enterprise.context.ApplicationScoped;
import org.kubis.mastercard.BinDetails;
import org.kubis.risk.RiskContext;
import org.kubis.risk.RiskRule;
import org.kubis.risk.RuleResult;

import java.util.Optional;
import io.quarkus.logging.Log;

/***
 * Rule which checks founding source
 */
@ApplicationScoped
public class FundingSourceRule implements RiskRule {

    private final static String PREPAID = "PREPAID";
    private final int points = 30;

    @Override
    public Optional<RuleResult> evaluate(RiskContext context) {
        return context.binDetails()
                .map(BinDetails::fundingSource)
                .filter(PREPAID::equalsIgnoreCase)
                .map(fundingSource -> {
                    Log.debugf("Card funding source is %s. %s points added", PREPAID, points);
                    return new RuleResult(points, "Card funding source is prepaid");
                });
    }
}
