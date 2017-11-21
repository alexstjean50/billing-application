package ca.ulaval.glo4002.billing.persistence.entity;

import ca.ulaval.glo4002.billing.domain.billing.bill.BillStatus;
import ca.ulaval.glo4002.billing.domain.billing.client.DueTerm;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "BILL")
public class BillEntity implements Serializable
{
    private static final long serialVersionUID = 2466055953468983457L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BILL_ID")
    private long billId;
    @Column(name = "BILL_NUMBER")
    private long billNumber;
    @Enumerated(EnumType.STRING)
    private BillStatus status;
    @Column
    private Instant creationDate;
    @Column
    private Instant effectiveDate;
    @Enumerated(EnumType.STRING)
    private DueTerm dueTerm;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "BILL_ID", referencedColumnName = "BILL_ID")
    private List<ItemEntity> itemEntities;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "BILL_ID", referencedColumnName = "BILL_ID")
    private List<DiscountEntity> discountEntities;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "BILL_NUMBER", referencedColumnName = "BILL_NUMBER")
    private List<AllocationEntity> allocationEntities;

    public BillEntity()
    {
    }

    public BillEntity(long billId, long billNumber, BillStatus status, Instant creationDate, Instant effectiveDate,
                      DueTerm dueTerm, List<ItemEntity> itemEntities, List<DiscountEntity> discountEntities,
                      List<AllocationEntity> allocationEntities)
    {
        this.billId = billId;
        this.billNumber = billNumber;
        this.status = status;
        this.creationDate = creationDate;
        this.effectiveDate = effectiveDate;
        this.dueTerm = dueTerm;
        this.itemEntities = itemEntities;
        this.discountEntities = discountEntities;
        this.allocationEntities = allocationEntities;
    }

    public long getBillId()
    {
        return this.billId;
    }

    public long getBillNumber()
    {
        return this.billNumber;
    }

    public BillStatus getStatus()
    {
        return this.status;
    }

    public Instant getCreationDate()
    {
        return this.creationDate;
    }

    public Instant getEffectiveDate()
    {
        return this.effectiveDate;
    }

    public DueTerm getDueTerm()
    {
        return this.dueTerm;
    }

    public List<ItemEntity> getItemEntities()
    {
        return this.itemEntities;
    }

    public List<DiscountEntity> getDiscountEntities()
    {
        return this.discountEntities;
    }

    public List<AllocationEntity> getAllocationEntities()
    {
        return this.allocationEntities;
    }

    public void setBillId(long billId)
    {
        this.billId = billId;
    }

    public void setBillNumber(long billNumber)
    {
        this.billNumber = billNumber;
    }

    public void setStatus(BillStatus status)
    {
        this.status = status;
    }

    public void setCreationDate(Instant creationDate)
    {
        this.creationDate = creationDate;
    }

    public void setEffectiveDate(Instant effectiveDate)
    {
        this.effectiveDate = effectiveDate;
    }

    public void setDueTerm(DueTerm dueTerm)
    {
        this.dueTerm = dueTerm;
    }

    public void setItemEntities(List<ItemEntity> itemEntities)
    {
        this.itemEntities = itemEntities;
    }

    public void setDiscountEntities(List<DiscountEntity> discountEntities)
    {
        this.discountEntities = discountEntities;
    }

    public void setAllocationEntities(List<AllocationEntity> allocationEntities)
    {
        this.allocationEntities = allocationEntities;
    }
}
