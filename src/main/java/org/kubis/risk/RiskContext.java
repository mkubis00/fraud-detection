package org.kubis.risk;

import org.kubis.domain.Transaction;
import org.kubis.mastercard.BinDetails;

import java.util.Optional;

/***
 * Data needed for any risk evaluation
 */
public record RiskContext(
        Transaction transaction,
        Optional<BinDetails> binDetails
) {}
