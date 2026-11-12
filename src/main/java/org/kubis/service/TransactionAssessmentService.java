package org.kubis.service;

import org.kubis.domain.Transaction;
import org.kubis.risk.RiskEvaluation;


public interface TransactionAssessmentService {
    RiskEvaluation assess(Transaction transaction);
}
