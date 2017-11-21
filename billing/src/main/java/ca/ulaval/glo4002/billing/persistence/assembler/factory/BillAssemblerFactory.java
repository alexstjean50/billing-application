package ca.ulaval.glo4002.billing.persistence.assembler.factory;

import ca.ulaval.glo4002.billing.persistence.assembler.allocation.AllocationAssembler;
import ca.ulaval.glo4002.billing.persistence.assembler.bill.BillAssembler;
import ca.ulaval.glo4002.billing.persistence.assembler.bill.DiscountAssembler;
import ca.ulaval.glo4002.billing.persistence.assembler.bill.ItemAssembler;

public class BillAssemblerFactory
{
    private final ItemAssemblerFactory itemAssemblerFactory;
    private final DiscountAssemblerFactory discountAssemblerFactory;
    private final AllocationAssembler allocationAssembler;

    public BillAssemblerFactory()
    {
        this(new ItemAssemblerFactory(), new DiscountAssemblerFactory(), new AllocationAssembler());
    }

    public BillAssemblerFactory(ItemAssemblerFactory itemAssemblerFactory, DiscountAssemblerFactory
            discountAssemblerFactory, AllocationAssembler allocationAssembler)
    {
        this.itemAssemblerFactory = itemAssemblerFactory;
        this.discountAssemblerFactory = discountAssemblerFactory;
        this.allocationAssembler = allocationAssembler;
    }

    public BillAssembler create()
    {
        ItemAssembler itemAssembler = this.itemAssemblerFactory.create();
        DiscountAssembler discountAssembler = this.discountAssemblerFactory.create();

        return new BillAssembler(itemAssembler, discountAssembler, this.allocationAssembler);
    }
}
