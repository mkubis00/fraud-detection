package org.kubis.resource;

import io.quarkus.logging.Log;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.MDC;
import org.kubis.domain.Transaction;
import org.kubis.dto.TransactionRiskAssessment;
import org.kubis.dto.TransactionRequest;
import org.kubis.service.TransactionAssessmentService;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionRiskResource {

    private final TransactionAssessmentService transactionAssessmentService;

    public TransactionRiskResource(TransactionAssessmentService transactionAssessmentService) {
        this.transactionAssessmentService = transactionAssessmentService;
    }

    @POST
    @RolesAllowed("transaction-risk-check")
    @Operation(
            summary = "Assess transaction fraud risk"
    )
    @APIResponse(responseCode = "200", description = "Risk assessment")
    @APIResponse(responseCode = "401", description = "Missing or invalid JWT token")
    @APIResponse(responseCode = "500", description = "Error occurred during execution")
    public TransactionRiskAssessment assess(@Valid TransactionRequest request) {
        Log.infof("Assessing transaction with bin=%s, amount=%s and userLocation=%s",
                request.bin(), request.amount(), request.userLocation());
        Transaction transactionDomain = request.toDomain();
        String requestId = (String) MDC.get("requestId");
        TransactionRiskAssessment transactionRiskAssessmentDto = transactionAssessmentService.assess(transactionDomain).toDto(transactionDomain.bin(), requestId);
        Log.infof("Transaction assessment done with bin=%s with level=%s", transactionRiskAssessmentDto.bin(), transactionRiskAssessmentDto.riskLevel());
        return transactionRiskAssessmentDto;
    }
}
