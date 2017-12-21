package ca.ulaval.glo4002.billing.service.assembler.domain;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.allocation.Allocation;
import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;

import java.time.Instant;

public class DomainAllocationAssembler
{
    private Instant currentTime;

    public DomainAllocationAssembler()
    {
    }

    public DomainAllocationAssembler(Instant currentTime)
    {
        this.currentTime = currentTime;
    }

    public Allocation toNewAllocation(Bill bill, Money allocatedAmount)
    {
        return new Allocation(Identity.EMPTY, bill.getBillNumber(),
                allocatedAmount, this.currentTime);
    }

    public void setCurrentTime(Instant currentTime)
    {
        this.currentTime = currentTime;
    }
}
