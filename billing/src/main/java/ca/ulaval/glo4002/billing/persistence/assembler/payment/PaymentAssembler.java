package ca.ulaval.glo4002.billing.persistence.assembler.payment;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.allocation.Allocation;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;
import ca.ulaval.glo4002.billing.persistence.assembler.allocation.AllocationAssembler;
import ca.ulaval.glo4002.billing.persistence.entity.AllocationEntity;
import ca.ulaval.glo4002.billing.persistence.entity.PaymentEntity;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentAssembler
{
    private final PaymentMethodAssembler paymentMethodAssembler;
    private final AllocationAssembler allocationAssembler;

    public PaymentAssembler(PaymentMethodAssembler paymentMethodAssembler, AllocationAssembler allocationAssembler)
    {
        this.paymentMethodAssembler = paymentMethodAssembler;
        this.allocationAssembler = allocationAssembler;
    }

    public Payment toDomainModel(PaymentEntity paymentEntity)
    {
        List<Allocation> allocations = paymentEntity.getAllocations()
                .stream()
                .map(this.allocationAssembler::toDomainModel)
                .collect(Collectors.toList());
        return new Payment(new Identity(paymentEntity.getPaymentId()),
                paymentEntity.getPaymentNumber(),
                Money.valueOf(paymentEntity.getAmount()),
                paymentEntity.getCreationDate(),
                this.paymentMethodAssembler.toDomainModel(paymentEntity.getPaymentMethodEntity()),
                allocations
        );
    }

    public PaymentEntity toPersistenceModel(Payment payment)
    {
        List<AllocationEntity> allocations = payment.getAllocations()
                .stream()
                .map(this.allocationAssembler::toPersistenceModel)
                .collect(Collectors.toList());
        return new PaymentEntity(
                (payment.getPaymentId()).getId(),
                payment.getAmount()
                        .asBigDecimal(),
                payment.getPaymentDate(),
                payment.getPaymentNumber(),
                this.paymentMethodAssembler.toPersistenceModel(payment.getPaymentMethod()),
                allocations
        );
    }
}
