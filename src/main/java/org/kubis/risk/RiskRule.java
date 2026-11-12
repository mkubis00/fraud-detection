package org.kubis.risk;

import java.util.Optional;

public interface RiskRule {
    Optional<RuleResult> evaluate(RiskContext context);
}
