package ca.ulaval.glo4002.billing.persistence.assembler.factory;

import ca.ulaval.glo4002.billing.persistence.assembler.payment.PaymentMethodAssembler;

class PaymentMethodAssemblerFactory
{
    public PaymentMethodAssembler create()
    {
        return new PaymentMethodAssembler();
    }
}
