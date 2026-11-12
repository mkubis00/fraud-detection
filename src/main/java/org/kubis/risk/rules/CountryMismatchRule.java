package org.kubis.risk.rules;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.kubis.mastercard.BinDetails;
import org.kubis.risk.RiskContext;
import org.kubis.risk.RiskRule;
import org.kubis.risk.RuleResult;

import java.util.Optional;

/***
 * Rule which checks if country of issuer and country of transactions are equal
 */
@ApplicationScoped
public class CountryMismatchRule implements RiskRule {

    private final int points = 21;

    @Override
    public Optional<RuleResult> evaluate(RiskContext context) {
        String userLocation = context.transaction().userLocation();
        return context.binDetails()
                .map(BinDetails::issuerCountryAlpha3)
                .filter(cardIssuer -> !cardIssuer.equalsIgnoreCase(userLocation))
                .map(cardIssuer -> {
                    Log.debugf("Card issued in %s but used in %s. %s points added", cardIssuer, userLocation, points);
                    return new RuleResult(points,
                            "Card issued in " + cardIssuer + " but used in " + userLocation);
                });
    }
}
