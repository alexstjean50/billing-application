package ca.ulaval.glo4002.billing.resource;

import ca.ulaval.glo4002.billing.domain.billing.transaction.TransactionType;
import ca.ulaval.glo4002.billing.service.PaymentService;
import ca.ulaval.glo4002.billing.service.TransactionService;
import ca.ulaval.glo4002.billing.service.dto.request.PaymentCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.PaymentMethod;
import ca.ulaval.glo4002.billing.service.dto.response.PaymentCreationResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PaymentResourceTest
{
    public static final long SOME_CLIENT_ID = 1L;
    public static final int SOME_PAYMENT_NUMBER = 12;
    public static final BigDecimal SOME_AMOUNT = BigDecimal.TEN;
    @Mock
    private TransactionService transactionService;
    private PaymentCreationResponse paymentCreationResponse;
    @Mock
    private PaymentService paymentService;
    private PaymentResource paymentResource;
    private PaymentCreationRequest paymentCreationRequest;

    @Before
    public void setUp()
    {
        this.paymentResource = new PaymentResource(this.paymentService, this.transactionService);
        this.paymentCreationRequest = new PaymentCreationRequest(SOME_CLIENT_ID, SOME_AMOUNT, new PaymentMethod
                ("", ""));
        this.paymentCreationResponse = PaymentCreationResponse.create(SOME_PAYMENT_NUMBER);
    }

    @Test
    public void whenCreatingPayment_thenShouldLogPaymentTransaction()
    {
        given(this.paymentService.createPayment(this.paymentCreationRequest)).willReturn(this.paymentCreationResponse);

        this.paymentResource.createPayment(this.paymentCreationRequest);

        verify(this.transactionService).logTransaction(SOME_CLIENT_ID, SOME_AMOUNT, TransactionType.PAYMENT);
    }
}
