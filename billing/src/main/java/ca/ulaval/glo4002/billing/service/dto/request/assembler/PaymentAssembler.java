package ca.ulaval.glo4002.billing.service.dto.request.assembler;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;
import ca.ulaval.glo4002.billing.domain.billing.payment.PaymentMethod;
import ca.ulaval.glo4002.billing.domain.billing.payment.PaymentMethodSource;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import ca.ulaval.glo4002.billing.service.dto.request.PaymentCreationRequest;

import java.time.Instant;
import java.util.ArrayList;

public class PaymentAssembler
{
    public Payment toPayment(PaymentCreationRequest request, long paymentNumber, Instant paymentDate)
    {
        PaymentMethod paymentMethod = new PaymentMethod(Identity.EMPTY, request.paymentMethod.account,
                PaymentMethodSource.valueOf(request.paymentMethod.source));
        return new Payment(Identity.EMPTY, paymentNumber, Money.valueOf(request.amount),
                paymentDate, paymentMethod, new ArrayList<>());
    }
}
