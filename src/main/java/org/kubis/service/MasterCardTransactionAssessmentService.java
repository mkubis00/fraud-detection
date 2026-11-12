package org.kubis.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kubis.domain.Transaction;
import org.kubis.mastercard.BinDetails;
import org.kubis.mastercard.BinLookupService;
import org.kubis.risk.RiskContext;
import org.kubis.risk.RiskEngine;
import org.kubis.risk.RiskEvaluation;

import java.util.Optional;

@ApplicationScoped
class MasterCardTransactionAssessmentService implements TransactionAssessmentService {

    @Inject
    BinLookupService binLookupService;

    @Inject
    RiskEngine riskEngine;

    @Override
    public RiskEvaluation assess(Transaction transaction) {
        Optional<BinDetails> binDetails = binLookupService.lookup(transaction.bin());
        RiskContext riskContext = new RiskContext(transaction, binDetails);
        return riskEngine.evaluate(riskContext);
    }
}
