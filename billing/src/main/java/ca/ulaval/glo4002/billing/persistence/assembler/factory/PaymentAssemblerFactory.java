package ca.ulaval.glo4002.billing.persistence.assembler.factory;

import ca.ulaval.glo4002.billing.persistence.assembler.allocation.AllocationAssembler;
import ca.ulaval.glo4002.billing.persistence.assembler.payment.PaymentAssembler;
import ca.ulaval.glo4002.billing.persistence.assembler.payment.PaymentMethodAssembler;

public class PaymentAssemblerFactory
{
    private final PaymentMethodAssemblerFactory paymentMethodAssemblerFactory;
    private final AllocationAssembler allocationAssembler;

    public PaymentAssemblerFactory()
    {
        this(new PaymentMethodAssemblerFactory(), new AllocationAssembler());
    }

    private PaymentAssemblerFactory(PaymentMethodAssemblerFactory paymentMethodAssemblerFactory, AllocationAssembler
            allocationAssembler)
    {
        this.paymentMethodAssemblerFactory = paymentMethodAssemblerFactory;
        this.allocationAssembler = allocationAssembler;
    }

    public PaymentAssembler create()
    {
        PaymentMethodAssembler paymentMethodAssembler = this.paymentMethodAssemblerFactory.create();

        return new PaymentAssembler(paymentMethodAssembler, this.allocationAssembler);
    }
}
