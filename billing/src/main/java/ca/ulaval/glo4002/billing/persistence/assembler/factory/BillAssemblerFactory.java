package ca.ulaval.glo4002.billing.persistence.assembler.factory;

import ca.ulaval.glo4002.billing.persistence.assembler.allocation.AllocationAssembler;
import ca.ulaval.glo4002.billing.persistence.assembler.bill.BillAssembler;
import ca.ulaval.glo4002.billing.persistence.assembler.bill.ItemAssembler;

public class BillAssemblerFactory
{
    private final ItemAssemblerFactory itemAssemblerFactory;
    private final AllocationAssembler allocationAssembler;

    public BillAssemblerFactory()
    {
        this(new ItemAssemblerFactory(), new AllocationAssembler());
    }

    private BillAssemblerFactory(ItemAssemblerFactory itemAssemblerFactory, AllocationAssembler allocationAssembler)
    {
        this.itemAssemblerFactory = itemAssemblerFactory;
        this.allocationAssembler = allocationAssembler;
    }

    public BillAssembler create()
    {
        ItemAssembler itemAssembler = this.itemAssemblerFactory.create();

        return new BillAssembler(itemAssembler, this.allocationAssembler);
    }
}
