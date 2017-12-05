package ca.ulaval.glo4002.billing.persistence.assembler.bill;

import ca.ulaval.glo4002.billing.domain.billing.allocation.Allocation;
import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.domain.billing.bill.Item;
import ca.ulaval.glo4002.billing.persistence.assembler.allocation.AllocationAssembler;
import ca.ulaval.glo4002.billing.persistence.entity.AllocationEntity;
import ca.ulaval.glo4002.billing.persistence.entity.BillEntity;
import ca.ulaval.glo4002.billing.persistence.entity.ItemEntity;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;

import java.util.List;
import java.util.stream.Collectors;

public class BillAssembler
{
    private final ItemAssembler itemAssembler;
    private final AllocationAssembler allocationAssembler;

    public BillAssembler(ItemAssembler itemAssembler, AllocationAssembler
            allocationAssembler)
    {
        this.itemAssembler = itemAssembler;
        this.allocationAssembler = allocationAssembler;
    }

    public Bill toDomainModel(BillEntity billEntity)
    {
        List<Item> items = billEntity.getItemEntities()
                .stream()
                .map(this.itemAssembler::toDomainModel)
                .collect(Collectors.toList());
        List<Allocation> allocations = billEntity.getAllocationEntities()
                .stream()
                .map(this.allocationAssembler::toDomainModel)
                .collect(Collectors.toList());
        return Bill.create(new Identity(billEntity.getBillId()), billEntity.getBillNumber(),
                billEntity.getCreationDate(), billEntity.getStatus(), billEntity.getEffectiveDate(),
                billEntity.getDueTerm(), items, allocations);
    }

    public BillEntity toPersistenceModel(Bill bill)
    {
        List<ItemEntity> itemEntities = bill.getItems()
                .stream()
                .map(this.itemAssembler::toPersistenceModel)
                .collect(Collectors.toList());
        List<AllocationEntity> allocationEntities = bill.getAllocations()
                .stream()
                .map(this.allocationAssembler::toPersistenceModel)
                .collect(Collectors.toList());

        return new BillEntity((bill.getBillId()).getId(), bill.getBillNumber(), bill.getStatus(),
                bill.getCreationDate(), bill.getEffectiveDate(), bill.getDueTerm(),
                itemEntities, allocationEntities);
    }
}
