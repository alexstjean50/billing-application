package ca.ulaval.glo4002.billing.persistence.assembler.allocation;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.allocation.Allocation;
import ca.ulaval.glo4002.billing.persistence.entity.AllocationEntity;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;

public class AllocationAssembler
{
    public Allocation toDomainModel(AllocationEntity allocationEntity)
    {
        return new Allocation(new Identity(allocationEntity.getAllocationId()),
                allocationEntity.getBillNumber(),
                Money.valueOf(allocationEntity.getAllocatedAmount()), allocationEntity.getCreationDate());
    }

    public AllocationEntity toPersistenceModel(Allocation allocation)
    {
        return new AllocationEntity((allocation.getAllocationId()).getId(), allocation.getBillNumber(),
                allocation.getAllocatedAmount()
                        .asBigDecimal(), allocation.getCreationDate());
    }
}
