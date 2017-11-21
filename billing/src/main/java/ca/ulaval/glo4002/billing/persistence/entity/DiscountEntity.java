package ca.ulaval.glo4002.billing.persistence.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class DiscountEntity implements Serializable
{
    private static final long serialVersionUID = -2553948153380011296L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DISCOUNT_ID")
    private long discountId;
    @Column
    private BigDecimal amount;
    @Column
    private String description;

    public DiscountEntity()
    {
    }

    public DiscountEntity(long discountId, BigDecimal amount, String description)
    {
        this.discountId = discountId;
        this.amount = amount;
        this.description = description;
    }

    public long getDiscountId()
    {
        return this.discountId;
    }

    public BigDecimal getAmount()
    {
        return this.amount;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDiscountId(long discountId)
    {
        this.discountId = discountId;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
