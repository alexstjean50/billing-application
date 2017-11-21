package ca.ulaval.glo4002.billing.persistence.assembler.payment;

import ca.ulaval.glo4002.billing.domain.billing.payment.PaymentMethod;
import ca.ulaval.glo4002.billing.persistence.entity.PaymentMethodEntity;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;

public class PaymentMethodAssembler
{
    public PaymentMethod toDomainModel(PaymentMethodEntity paymentMethodEntity)
    {
        return new PaymentMethod(new Identity(paymentMethodEntity.getId()), paymentMethodEntity.getBankAccount(),
                paymentMethodEntity.getSource());
    }

    public PaymentMethodEntity toPersistenceModel(PaymentMethod paymentMethod)
    {
        return new PaymentMethodEntity((paymentMethod.identity).getId(), paymentMethod.bankAccount,
                paymentMethod.source);
    }
}
