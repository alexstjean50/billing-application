package ca.ulaval.glo4002.billing.service;

import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainPaymentAssembler;
import ca.ulaval.glo4002.billing.service.dto.request.PaymentCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.PaymentMethod;
import ca.ulaval.glo4002.billing.service.dto.response.PaymentCreationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.PaymentCreationResponseAssembler;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.retriever.AccountRetriever;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceTest
{
    private static final String URL_TEMPLATE = "/payments/%d";
    private static final PaymentMethod SOME_PAYMENT_METHOD = new PaymentMethod();
    private static final long SOME_CLIENT_ID = 42;
    private static final BigDecimal SOME_AMOUNT = BigDecimal.valueOf(42.42);
    private static final long SOME_PAYMENT_NUMBER = 12;
    private PaymentService paymentService;
    @Mock
    private Account account;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountRetriever accountRetriever;
    @Mock
    private DomainPaymentAssembler domainPaymentAssembler;
    @Mock
    private Payment payment;

    @Before
    public void initializePaymentService()
    {
        PaymentCreationResponseAssembler paymentCreationResponseAssembler = new PaymentCreationResponseAssembler();
        this.paymentService = new PaymentService(this.accountRepository, this.domainPaymentAssembler,
                paymentCreationResponseAssembler, this.accountRetriever);
    }

    @Test
    public void whenReceivePayment_thenClientAccountRetrieved()
    {
        PaymentCreationRequest paymentCreationRequest = createPaymentCreationRequest();
        given(this.accountRetriever.retrieveClientAccount(SOME_CLIENT_ID)).willReturn(this.account);
        given(this.payment.getPaymentNumber()).willReturn(SOME_PAYMENT_NUMBER);
        given(this.domainPaymentAssembler.toNewPayment(paymentCreationRequest)).willReturn(this.payment);

        this.paymentService.createPayment(paymentCreationRequest);

        verify(this.accountRetriever).retrieveClientAccount(SOME_CLIENT_ID);
    }

    @Test
    public void whenReceivePayment_thenPaymentAddedToAccount()
    {
        given(this.accountRetriever.retrieveClientAccount(SOME_CLIENT_ID)).willReturn(this.account);
        given(this.payment.getPaymentNumber()).willReturn(SOME_PAYMENT_NUMBER);
        PaymentCreationRequest paymentCreationRequest = createPaymentCreationRequest();
        given(this.domainPaymentAssembler.toNewPayment(paymentCreationRequest)).willReturn(this.payment);

        this.paymentService.createPayment(paymentCreationRequest);

        verify(this.account).addPayment(notNull());
    }

    @Test
    public void whenReceivePayment_thenAccountSaved()
    {
        given(this.accountRetriever.retrieveClientAccount(SOME_CLIENT_ID)).willReturn(this.account);
        given(this.payment.getPaymentNumber()).willReturn(SOME_PAYMENT_NUMBER);
        PaymentCreationRequest paymentCreationRequest = createPaymentCreationRequest();
        given(this.domainPaymentAssembler.toNewPayment(paymentCreationRequest)).willReturn(this.payment);

        this.paymentService.createPayment(paymentCreationRequest);

        verify(this.accountRepository).save(this.account);
    }

    @Test
    public void whenReceivePayment_thenResponseUrlContainsPaymentId()
    {
        PaymentCreationRequest paymentCreationRequest = createPaymentCreationRequest();
        given(this.accountRetriever.retrieveClientAccount(SOME_CLIENT_ID)).willReturn(this.account);
        given(this.payment.getPaymentNumber()).willReturn(SOME_PAYMENT_NUMBER);
        given(this.domainPaymentAssembler.toNewPayment(paymentCreationRequest)).willReturn(this.payment);
        String expectedUrlContainingPaymentId = String.format(URL_TEMPLATE, this.payment.getPaymentNumber());

        PaymentCreationResponse paymentCreationResponse =
                this.paymentService.createPayment(paymentCreationRequest);

        assertEquals(expectedUrlContainingPaymentId, paymentCreationResponse.url);
    }

    private PaymentCreationRequest createPaymentCreationRequest()
    {
        return PaymentCreationRequest.create(SOME_CLIENT_ID, SOME_AMOUNT, SOME_PAYMENT_METHOD);
    }
}