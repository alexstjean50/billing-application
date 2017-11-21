package ca.ulaval.glo4002.billing.domain.billing.bill;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;

import java.math.BigDecimal;

public class Item
{
    private final Identity itemId;
    private final long productId;
    private final Money unitPrice;
    private final String note;
    private final int quantity;

    private Item(Identity itemId, Money unitPrice, String note, long productId, int quantity)
    {
        this.itemId = itemId;
        this.productId = productId;
        this.unitPrice = unitPrice;
        this.note = note;
        this.quantity = quantity;
    }

    public static Item create(Money unitPrice, String note, long productId, int quantity)
    {
        return new Item(Identity.EMPTY, unitPrice, note, productId, quantity);
    }

    public static Item create(Identity itemId, Money unitPrice, String note, long productId, int quantity)
    {
        return new Item(itemId, unitPrice, note, productId, quantity);
    }

    public Money calculatePrice()
    {
        return this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }

    public long getProductId()
    {
        return this.productId;
    }

    public Identity getItemId()
    {
        return this.itemId;
    }

    public Money getUnitPrice()
    {
        return this.unitPrice;
    }

    public String getNote()
    {
        return this.note;
    }

    public int getQuantity()
    {
        return this.quantity;
    }
}
