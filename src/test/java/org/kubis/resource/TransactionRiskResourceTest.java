package org.kubis.resource;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.quarkiverse.wiremock.devservice.ConnectWireMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@ConnectWireMock
@TestSecurity(user = "test-user", roles = "transaction-risk-check")
class TransactionRiskResourceTest {
    private static final String TRANSACTIONS_PATH = "/transactions";
    private static final String X_REQUESTID = "X-REQUESTID";

    private static final String REQUEST_ID = "requestId";
    private static final String RISK_LEVEL = "riskLevel";
    private static final String REASONS = "reasons";

    private static final String BIN = "12345678";
    private static final String REQUEST_ID_VALUE = "test-request-id";
    private static final String MEDIUM_RISK = "MEDIUM";

    private static final String PREPAID_USA_MC = """
        [{
          "fundingSource": "PREPAID",
          "country": { "alpha3": "USA" }
        }]
        """;

    private static final String MASTERCARD_PATH = "/bin-resources/bin-ranges/account-searches";

    WireMock wiremock;

    @Test
    void shouldMapMastercardResponseToRiskAssessment() {
        wiremock.register(post(urlEqualTo(MASTERCARD_PATH)).willReturn(okJson(PREPAID_USA_MC)));

        given().contentType(ContentType.JSON).body(transaction(BIN))
                .header(X_REQUESTID, REQUEST_ID_VALUE)
                .when().post(TRANSACTIONS_PATH)
                .then()
                .statusCode(200)
                .body(REQUEST_ID, equalTo(REQUEST_ID_VALUE))
                .body(RISK_LEVEL, equalTo(MEDIUM_RISK))
                .body(REASONS, hasSize(2));

        verifyMastercardRequestWasSigned();
    }

    @Test
    void shouldGenerateRequestIdIfMissing() {
        wiremock.register(post(urlEqualTo(MASTERCARD_PATH)).willReturn(okJson(PREPAID_USA_MC)));

        given().contentType(ContentType.JSON).body(transaction(BIN))
                .when().post(TRANSACTIONS_PATH)
                .then()
                .statusCode(200)
                .header(X_REQUESTID, not(emptyOrNullString()))
                .body(REQUEST_ID, notNullValue())
                .body(RISK_LEVEL, equalTo(MEDIUM_RISK))
                .body(REASONS, hasSize(2));

        verifyMastercardRequestWasSigned();
    }

    @Test
    void shouldReturnOkWithReasonWhenBinIsUnknown() {
        wiremock.register(post(urlEqualTo(MASTERCARD_PATH)).willReturn(okJson("[]")));

        given().contentType(ContentType.JSON).body(transaction(BIN))
                .when().post(TRANSACTIONS_PATH)
                .then()
                .statusCode(200)
                .body(RISK_LEVEL, equalTo("VERY_HIGH"))
                .body(REASONS, hasSize(1));

        verifyMastercardRequestWasSigned();
    }

    @AfterEach
    void resetWireMock() {
        wiremock.resetMappings();
        wiremock.resetRequests();
    }

    private void verifyMastercardRequestWasSigned() {
        wiremock.verifyThat(postRequestedFor(urlEqualTo(MASTERCARD_PATH))
                .withHeader("Authorization", matching("OAuth .*")));
    }

    private static String transaction(String bin) {
        return """
                { "bin": "%s", "amount": 100, "userLocation": "POL" }
                """.formatted(bin);
    }
}
