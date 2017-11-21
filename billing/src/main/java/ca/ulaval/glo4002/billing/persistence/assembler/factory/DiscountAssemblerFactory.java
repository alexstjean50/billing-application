package ca.ulaval.glo4002.billing.persistence.assembler.factory;

import ca.ulaval.glo4002.billing.persistence.assembler.bill.DiscountAssembler;

class DiscountAssemblerFactory
{
    public DiscountAssembler create()
    {
        return new DiscountAssembler();
    }
}
