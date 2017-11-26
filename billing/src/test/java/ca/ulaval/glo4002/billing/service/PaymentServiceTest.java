package ca.ulaval.glo4002.billing.service;

import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.domain.billing.client.Client;
import ca.ulaval.glo4002.billing.domain.billing.client.DueTerm;
import ca.ulaval.glo4002.billing.domain.billing.payment.PaymentMethodSource;
import ca.ulaval.glo4002.billing.persistence.repository.AccountClientNotFoundException;
import ca.ulaval.glo4002.billing.service.dto.request.PaymentCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.PaymentMethod;
import ca.ulaval.glo4002.billing.service.dto.request.assembler.PaymentAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.PaymentCreationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.PaymentCreationResponseAssembler;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.payment.PaymentRepository;
import ca.ulaval.glo4002.billing.service.retriever.AccountRetriever;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest
{
    private static final String SOME_PAYMENT_ACCOUNT = "METRO-PLEX-XXX";
    private static final PaymentMethodSource SOME_PAYMENT_SOURCE = PaymentMethodSource.CHECK;
    private static final String URL_TEMPLATE = "/payments/%d";
    private static final PaymentMethod SOME_PAYMENT_METHOD = new PaymentMethod();
    private static final long SOME_CLIENT_ID = 42;
    private static final BigDecimal SOME_AMOUNT = BigDecimal.valueOf(42.42);
    private PaymentService paymentService;
    @Mock
    private Account account;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private AccountRetriever accountRetriever;
    @Mock
    private PaymentAssembler paymentAssembler;
    @Mock
    private PaymentCreationResponseAssembler paymentCreationResponseAssembler;

    @BeforeClass
    public static void initializePaymentMethod()
    {
        SOME_PAYMENT_METHOD.account = SOME_PAYMENT_ACCOUNT;
        SOME_PAYMENT_METHOD.source = SOME_PAYMENT_SOURCE.name();
    }

    @Before
    public void initializePaymentService()
    {
        this.paymentService = new PaymentService(this.accountRepository, this.paymentRepository, this
                .paymentAssembler, this.paymentCreationResponseAssembler, this.accountRetriever);
    }

    @Test
    public void whenReceivePayment_thenClientAccountRetrieved()
    {
        setAccountRepositoryBehavior();
        PaymentCreationRequest paymentCreationRequest = createPaymentCreationRequest();

        this.paymentService.createPayment(paymentCreationRequest);

        verify(this.accountRepository).findByClientId(SOME_CLIENT_ID);
    }

    @Test
    public void whenReceivePayment_thenPaymentNumberReturned()
    {
        setAccountRepositoryBehavior();
        PaymentCreationRequest paymentCreationRequest = createPaymentCreationRequest();
        long expectedPaymentNumber = this.paymentRepository.retrieveNextPaymentNumber();

        PaymentCreationResponse paymentCreationResponse = this.paymentService.createPayment(paymentCreationRequest);
        long responsePaymentId = paymentCreationResponse.id;

        assertEquals(expectedPaymentNumber, responsePaymentId);
    }

    @Test
    public void whenReceivePayment_thenNextBillNumberRetrieved()
    {
        setAccountRepositoryBehavior();

        this.paymentService.createPayment(createPaymentCreationRequest());

        verify(this.paymentRepository, atLeastOnce()).retrieveNextPaymentNumber();
    }

    @Test
    public void whenReceivePayment_thenPaymentAddedToAccount()
    {
        setAccountRepositoryBehavior();

        this.paymentService.createPayment(createPaymentCreationRequest());

        verify(this.account).addPayment(notNull());
    }

    @Test
    public void whenReceivePayment_thenAccountSaved()
    {
        setAccountRepositoryBehavior();

        this.paymentService.createPayment(createPaymentCreationRequest());

        verify(this.accountRepository).save(this.account);
    }

    @Test
    public void whenReceivePayment_thenResponseUrlContainsPaymentId()
    {
        setAccountRepositoryBehavior();
        String expectedUrlContainingPaymentId = String.format(URL_TEMPLATE,
                this.paymentRepository.retrieveNextPaymentNumber());

        PaymentCreationResponse paymentCreationResponse =
                this.paymentService.createPayment(createPaymentCreationRequest());

        Assert.assertEquals(expectedUrlContainingPaymentId, paymentCreationResponse.url);
    }

    private void setAccountRepositoryBehavior()
    {
        given(this.accountRepository.findByClientId(SOME_CLIENT_ID)).willReturn(this.account);
    }

    private PaymentCreationRequest createPaymentCreationRequest()
    {
        return PaymentCreationRequest.create(SOME_CLIENT_ID, SOME_AMOUNT, SOME_PAYMENT_METHOD);
    }
}