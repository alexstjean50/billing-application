package ca.ulaval.glo4002.billing.service.assembler.domain;

import ca.ulaval.glo4002.billing.domain.billing.payment.PaymentMethodSource;
import ca.ulaval.glo4002.billing.service.dto.request.PaymentCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.PaymentMethod;
import ca.ulaval.glo4002.billing.service.repository.payment.PaymentRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DomainPaymentAssemblerTest
{
    private static final String SOME_PAYMENT_ACCOUNT = "METRO-PLEX-XXX";
    private static final PaymentMethodSource SOME_PAYMENT_SOURCE = PaymentMethodSource.CHECK;
    private static final PaymentMethod SOME_PAYMENT_METHOD = new PaymentMethod();
    private static final long SOME_CLIENT_ID = 42;
    private static final BigDecimal SOME_AMOUNT = BigDecimal.valueOf(42.42);
    @Mock
    private PaymentRepository paymentRepository;
    private DomainPaymentAssembler domainPaymentAssembler;

    @BeforeClass
    public static void initializePaymentMethod()
    {
        SOME_PAYMENT_METHOD.account = SOME_PAYMENT_ACCOUNT;
        SOME_PAYMENT_METHOD.source = SOME_PAYMENT_SOURCE.name();
    }

    @Before
    public void initializeDomainPaymentAssembler()
    {
        this.domainPaymentAssembler = new DomainPaymentAssembler(this.paymentRepository);
    }

    @Test
    public void whenReceivePayment_thenNextBillNumberRetrieved()
    {
        this.domainPaymentAssembler.toNewPayment(createPaymentCreationRequest());

        verify(this.paymentRepository, times(1)).retrieveNextPaymentNumber();
    }

    private PaymentCreationRequest createPaymentCreationRequest()
    {
        return PaymentCreationRequest.create(SOME_CLIENT_ID, SOME_AMOUNT, SOME_PAYMENT_METHOD);
    }
}
