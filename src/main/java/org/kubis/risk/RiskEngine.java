package org.kubis.risk;

import io.quarkus.arc.All;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

/***
 * Class which calculates risk of transaction by evaluating all risks
 * defined as implementation of RiskRule interface.
 */
@ApplicationScoped
public class RiskEngine {
    private final List<RiskRule> rules;


    public RiskEngine(@All List<RiskRule> rules) {
        this.rules = rules;
    }

    public RiskEvaluation evaluate(RiskContext context) {
        Log.debugf("Evaluating risk for bin=%s", context.transaction().bin());
        List<RuleResult> results = rules.stream()
                .map(rule -> rule.evaluate(context))
                .flatMap(Optional::stream)
                .toList();

        int points = results.stream().mapToInt(RuleResult::points).sum();
        RiskLevel level = RiskLevel.fromPoints(points);
        List<String> reasons = results.stream().map(RuleResult::reason).toList();

        Log.debugf("Evaluating risk for bin=%s is completed with %s points", context.transaction().bin(), points);
        return new RiskEvaluation(level, reasons);
    }
}
