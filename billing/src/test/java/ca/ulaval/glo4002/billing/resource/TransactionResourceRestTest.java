package ca.ulaval.glo4002.billing.resource;

import ca.ulaval.glo4002.billing.contexts.ServiceLocator;
import ca.ulaval.glo4002.billing.manager.factory.TestEntityManagerFactoryConfigurator;
import ca.ulaval.glo4002.billing.service.dto.request.BillCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.ItemRequest;
import ca.ulaval.glo4002.billing.service.dto.request.PaymentCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.PaymentMethod;
import ca.ulaval.glo4002.billing.service.dto.response.BillCreationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.TransactionEntryResponse;
import ca.ulaval.glo4002.billing.service.dto.response.TransactionsResponse;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class TransactionResourceRestTest extends RestTestBase
{
    private static final String START_MONTH = "01";
    private static final String END_MONTH = "02";
    private static final Long YEAR = 2017L;
    private static final int SOME_CLIENT_ID = 1;
    private static final BigDecimal SOME_PAYMENT_AMOUNT = new BigDecimal(30);
    private static final BigDecimal SOME_BILL_AMOUNT = new BigDecimal(120);

    @Before
    public void setUp()
    {
        TestEntityManagerFactoryConfigurator.initializeEntityManagerFactory();
    }

    @Test
    public void givenASubmission_whenAcceptingBill_thenShouldLogInvoiceTransactionWithAmountOfBill()
    {
        createAndAcceptBill();

        TransactionEntryResponse transactionEntryResponse = retrieveLastInsertedTransaction();

        assertThat(SOME_BILL_AMOUNT, Matchers.comparesEqualTo(transactionEntryResponse.getAmount()));
    }

    @Test
    public void givenASubmission_whenAcceptingBill_thenShouldLogInvoiceTransaction()
    {
        createAndAcceptBill();

        TransactionEntryResponse transactionEntryResponse = retrieveLastInsertedTransaction();

        assertEquals("INVOICE", transactionEntryResponse.getTypeTransaction());
    }

    @Test
    public void givenASubmission_whenAcceptingBill_thenShouldLogInvoiceTransactionWithCreditOperation()
    {
        createAndAcceptBill();

        TransactionEntryResponse transactionEntryResponse = retrieveLastInsertedTransaction();

        assertEquals("CREDIT", transactionEntryResponse.getTypeOperation());
    }

    @Test
    public void whenCreatingPayment_thenShouldLogPaymentTransaction()
    {
        createPayment();

        TransactionEntryResponse transactionEntryResponse = retrieveLastInsertedTransaction();

        assertEquals("PAYMENT", transactionEntryResponse.getTypeTransaction());
    }

    @Test
    public void whenCreatingPayment_thenShouldLogPaymentTransactionWithDebitOperation()
    {
        createPayment();

        TransactionEntryResponse transactionEntryResponse = retrieveLastInsertedTransaction();

        assertEquals("DEBIT", transactionEntryResponse.getTypeOperation());
    }

    @Test
    public void whenCreatingPayment_thenShouldLogPaymentTransactionWithPaymentAmount()
    {
        createPayment();

        TransactionEntryResponse transactionEntryResponse = retrieveLastInsertedTransaction();

        assertThat(SOME_PAYMENT_AMOUNT, Matchers.comparesEqualTo(transactionEntryResponse.getAmount()));
    }

    @Test
    public void
    givenAnEmptyInitialBalance_whenAddingCreditTransaction_thenShouldAddNegativeValueOfTransactionToBalance()
    {
        createAndAcceptBill();

        TransactionEntryResponse transactionEntryResponse = retrieveLastInsertedTransaction();

        assertThat(transactionEntryResponse.getAmount()
                .negate(), Matchers.comparesEqualTo(transactionEntryResponse.getBalance()));
    }

    @Test
    public void
    givenAnEmptyInitialBalance_whenAddingDebitTransaction_thenShouldAddValueOfTransactionToBalance()
    {
        createPayment();

        TransactionEntryResponse transactionEntryResponse = retrieveLastInsertedTransaction();

        assertThat(transactionEntryResponse.getAmount(),
                Matchers.comparesEqualTo(transactionEntryResponse.getBalance()));
    }

    @Test
    public void
    givenAnEmptyInitialBalance_whenAddingDebitAndCreditTransactions_thenBalanceShouldBeSumOfBothTransactions()
    {
        createAndAcceptBill();
        createPayment();

        TransactionEntryResponse transactionEntryResponse = retrieveLastInsertedTransaction();

        assertThat(SOME_PAYMENT_AMOUNT.subtract(SOME_BILL_AMOUNT),
                Matchers.comparesEqualTo(transactionEntryResponse.getBalance()));
    }

    @Test
    public void
    givenTransactionsMadeInDifferentMonthsAndYears_whenRetrievingTransactions_thenShouldGetFilteredTransactionsOfSpecifiedMonthAndYearRange()
    {
        createPaymentAtSetTime(Instant.parse("2016-12-31T12:34:56Z"));
        createPaymentAtSetTime(Instant.parse("2017-01-01T12:34:56Z"));
        createPaymentAtSetTime(Instant.parse("2017-02-01T12:34:56Z"));
        createPaymentAtSetTime(Instant.parse("2017-03-01T12:34:56Z"));

        List<TransactionEntryResponse> transactionEntryResponses = givenBaseRequest()
                .when()
                .param("startMonth", START_MONTH)
                .when()
                .param("endMonth", END_MONTH)
                .when()
                .param("year", YEAR)
                .when()
                .get("/ledger/transactions")
                .as(TransactionsResponse.class)
                .getEntries();

        assertThatTransactionsAreInDateRange(transactionEntryResponses, Instant.parse("2017-01-01T00:00:00Z"),
                Instant.parse("2017-02-01T23:59:59Z"));
    }

    private void assertThatTransactionsAreInDateRange(List<TransactionEntryResponse> transactionEntryResponses,
                                                      Instant minimumDate, Instant maximumDate)
    {
        transactionEntryResponses.forEach(transactionEntryResponse -> assertTrue(transactionEntryResponse.getDate()
                .isAfter(minimumDate) && transactionEntryResponse.getDate()
                .isBefore(maximumDate)));
    }

    private void createPaymentAtSetTime(Instant parse)
    {
        changeTime(parse);
        createPayment();
    }

    private void changeTime(Instant anInstant)
    {
        ServiceLocator.loadService(Clock.class, Clock.fixed(anInstant, ZoneOffset.systemDefault()));
    }

    private TransactionEntryResponse retrieveLastInsertedTransaction()
    {
        List<TransactionEntryResponse> entries = retrieveAllTransactionEntries();
        return entries.get(entries.size() - 1);
    }

    private List<TransactionEntryResponse> retrieveAllTransactionEntries()
    {
        return givenBaseRequest()
                .when()
                .get("/ledger/transactions")
                .as(TransactionsResponse.class)
                .getEntries();
    }

    private void createAndAcceptBill()
    {
        ItemRequest table = new ItemRequest(SOME_BILL_AMOUNT, "Table", 1, 1);
        BillCreationRequest billCreationRequest = BillCreationRequest.create(SOME_CLIENT_ID, Instant.now(),
                "IMMEDIATE", Collections.singletonList(table));
        long billNumber = createNewSubmission(billCreationRequest);
        acceptBill(billNumber);
    }

    private void createPayment()
    {
        PaymentCreationRequest paymentCreationRequest = PaymentCreationRequest.create(SOME_CLIENT_ID,
                SOME_PAYMENT_AMOUNT,
                new PaymentMethod("23", "CHECK"));

        givenBaseRequest().body(paymentCreationRequest)
                .post("/payments");
    }

    private long createNewSubmission(BillCreationRequest billCreationRequest)
    {
        BillCreationResponse billCreationResponse = givenBaseRequest().body(billCreationRequest)
                .post("/bills")
                .as(BillCreationResponse.class);

        return billCreationResponse.getId();
    }

    private void acceptBill(long billNumber)
    {
        givenBaseRequest().post("/bills/{billNumber}", buildPathParams("billNumber", billNumber));
    }
}
