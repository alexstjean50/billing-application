package ca.ulaval.glo4002.billing.persistence.assembler.factory;

import ca.ulaval.glo4002.billing.persistence.assembler.bill.ItemAssembler;

class ItemAssemblerFactory
{
    public ItemAssembler create()
    {
        return new ItemAssembler();
    }
}
