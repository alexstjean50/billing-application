package ca.ulaval.glo4002.billing.service.assembler.domain;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;
import ca.ulaval.glo4002.billing.domain.billing.payment.PaymentMethod;
import ca.ulaval.glo4002.billing.domain.billing.payment.PaymentMethodSource;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import ca.ulaval.glo4002.billing.service.dto.request.PaymentCreationRequest;
import ca.ulaval.glo4002.billing.service.repository.payment.PaymentRepository;

import java.time.Instant;
import java.util.ArrayList;

public class DomainPaymentAssembler
{
    private final PaymentRepository paymentRepository;

    public DomainPaymentAssembler(PaymentRepository paymentRepository)
    {
        this.paymentRepository = paymentRepository;
    }

    public Payment toNewPayment(PaymentCreationRequest request)
    {
        long paymentNumber = this.paymentRepository.retrieveNextPaymentNumber();

        PaymentMethod paymentMethod = new PaymentMethod(Identity.EMPTY, request.paymentMethod.account,
                PaymentMethodSource.valueOf(request.paymentMethod.source));
        return new Payment(Identity.EMPTY, paymentNumber, Money.valueOf(request.amount),
                Instant.now(), paymentMethod, new ArrayList<>());
    }
}
