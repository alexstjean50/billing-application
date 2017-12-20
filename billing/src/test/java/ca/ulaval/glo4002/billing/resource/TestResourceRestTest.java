package ca.ulaval.glo4002.billing.resource;

import org.junit.Test;

import javax.ws.rs.core.Response.Status;

public class TestResourceRestTest extends RestTestBase
{
    private static final String START_MONTH = "01";
    private static final String END_MONTH = "12";
    private static final Long YEAR = 2017L;

    @Test
    public void withdrawMoneyOnAnAccountWithMoneyResultsInAnAcceptedTransaction()
    {
        givenBaseRequest()
                .when()
                .param("startMonth", START_MONTH)
                .when()
                .param("endMonth", END_MONTH)
                .when()
                .param("year", YEAR)
                .when()
                .get("/ledger/transactions")
                .then()
                .statusCode(Status.OK.getStatusCode());
    }
}
