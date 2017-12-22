package ca.ulaval.glo4002.billing.persistence.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "ITEM")
public class ItemEntity implements Serializable
{
    private static final long serialVersionUID = -1465474995790558906L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ITEM_ID")
    private long itemId;
    @Column
    private long productId;
    @Column(name = "QUANTITY")
    private int quantity;
    @Column(name = "PAID_PRICE")
    private BigDecimal paidPrice;
    @Column
    private String note;

    public ItemEntity()
    {
    }

    public ItemEntity(long itemId, long productId, int quantity, BigDecimal paidPrice, String note)
    {
        this.itemId = itemId;
        this.productId = productId;
        this.quantity = quantity;
        this.paidPrice = paidPrice;
        this.note = note;
    }

    public long getItemId()
    {
        return this.itemId;
    }

    public long getProductId()
    {
        return this.productId;
    }

    public int getQuantity()
    {
        return this.quantity;
    }

    public BigDecimal getPaidPrice()
    {
        return this.paidPrice;
    }

    public String getNote()
    {
        return this.note;
    }

    public void setItemId(long itemId)
    {
        this.itemId = itemId;
    }

    public void setProductId(long productId)
    {
        this.productId = productId;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public void setPaidPrice(BigDecimal paidPrice)
    {
        this.paidPrice = paidPrice;
    }

    public void setNote(String note)
    {
        this.note = note;
    }
}
