package ca.ulaval.glo4002.billing.persistence.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class AllocationEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ALLOCATION_ID")
    private long allocationId;
    @Column
    private long billNumber;
    @Column
    private BigDecimal allocatedAmount;
    @Column
    private Instant creationDate;

    public AllocationEntity()
    {
    }

    public AllocationEntity(long allocationId, long billNumber, BigDecimal allocatedAmount,
                            Instant creationDate)
    {
        this.allocationId = allocationId;
        this.billNumber = billNumber;
        this.allocatedAmount = allocatedAmount;
        this.creationDate = creationDate;
    }

    public long getAllocationId()
    {
        return this.allocationId;
    }

    public BigDecimal getAllocatedAmount()
    {
        return this.allocatedAmount;
    }

    public Instant getCreationDate()
    {
        return this.creationDate;
    }

    public long getBillNumber()
    {
        return this.billNumber;
    }

    public void setAllocationId(long allocationId)
    {
        this.allocationId = allocationId;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount)
    {
        this.allocatedAmount = allocatedAmount;
    }

    public void setCreationDate(Instant creationDate)
    {
        this.creationDate = creationDate;
    }

    public void setBillNumber(long billNumber)
    {
        this.billNumber = billNumber;
    }
}
