package org.kubis.risk;

/**
 * Result of the evaluation of single rule
 */
public record RuleResult(
        int points,
        String reason
) {}
