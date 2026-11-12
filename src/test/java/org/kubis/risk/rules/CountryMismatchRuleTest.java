package org.kubis.risk.rules;

import org.junit.jupiter.api.Test;
import org.kubis.domain.Transaction;
import org.kubis.mastercard.BinDetails;
import org.kubis.risk.RiskContext;
import org.kubis.risk.RuleResult;

import java.math.BigDecimal;
import java.util.Optional;

import static io.smallrye.common.constraint.Assert.assertTrue;


class CountryMismatchRuleTest {

    private final static String POL = "POL";
    private final static String DEBIT = "DEBIT";
    private final static CountryMismatchRule rule = new CountryMismatchRule();

    @Test
    void shouldAddRiskWhenIssuerCountryDiffersFromUserLocation() {
        RiskContext context = context(POL, Optional.of(card("USA", DEBIT)));
        Optional<RuleResult> result = rule.evaluate(context);

        assertTrue(result.isPresent());
    }

    @Test
    void shouldNotAddRiskWhenCountriesMatch() {
        RiskContext context = context(POL, Optional.of(card(POL, DEBIT)));

        assertTrue(rule.evaluate(context).isEmpty());
    }

    @Test
    void shouldNotAddRiskWhenBinDetailsAreMissing() {
        RiskContext context = context(POL, Optional.empty());

        assertTrue(rule.evaluate(context).isEmpty());
    }

    private static RiskContext context(String userLocation, Optional<BinDetails> binDetails) {
        Transaction transaction = new Transaction("12345678", new BigDecimal("100"), userLocation);
        return new RiskContext(transaction, binDetails);
    }

    private static BinDetails card(String countryAlpha3, String fundingSource) {
        return new BinDetails(new BinDetails.Country(countryAlpha3), fundingSource);
    }
}