package ca.ulaval.glo4002.billing.persistence.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "PAYMENT")
public class PaymentEntity implements Serializable
{
    private static final long serialVersionUID = -7350583157154110831L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYMENT_ID")
    private long paymentId;
    @Column
    private BigDecimal amount;
    @Column(name = "PAYMENT_NUMBER")
    private long paymentNumber;
    @Column
    private Instant creationDate;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PAYMENT_METHOD_ID", referencedColumnName = "PAYMENT_METHOD_ID")
    private PaymentMethodEntity paymentMethodEntity;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "PAYMENT_ID", referencedColumnName = "PAYMENT_ID")
    private List<AllocationEntity> allocations;

    public PaymentEntity()
    {
    }

    public PaymentEntity(long paymentId, BigDecimal amount, Instant creationDate, long paymentNumber,
                         PaymentMethodEntity paymentMethodEntity, List<AllocationEntity> allocations)
    {
        this.paymentId = paymentId;
        this.amount = amount;
        this.creationDate = creationDate;
        this.paymentNumber = paymentNumber;
        this.paymentMethodEntity = paymentMethodEntity;
        this.allocations = allocations;
    }

    public long getPaymentId()
    {
        return this.paymentId;
    }

    public BigDecimal getAmount()
    {
        return this.amount;
    }

    public Instant getCreationDate()
    {
        return this.creationDate;
    }

    public PaymentMethodEntity getPaymentMethodEntity()
    {
        return this.paymentMethodEntity;
    }

    public long getPaymentNumber()
    {
        return this.paymentNumber;
    }

    public List<AllocationEntity> getAllocations()
    {
        return this.allocations;
    }

    public void setPaymentId(long paymentId)
    {
        this.paymentId = paymentId;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public void setCreationDate(Instant creationDate)
    {
        this.creationDate = creationDate;
    }

    public void setPaymentMethodEntity(PaymentMethodEntity paymentMethodEntity)
    {
        this.paymentMethodEntity = paymentMethodEntity;
    }

    public void setPaymentNumber(long paymentNumber)
    {
        this.paymentNumber = paymentNumber;
    }

    public void setAllocations(List<AllocationEntity> allocations)
    {
        this.allocations = allocations;
    }
}
