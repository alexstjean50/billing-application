package ca.ulaval.glo4002.billing.domain.billing.bill;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;

public class Discount
{
    private final Identity discountId;
    private final Money amount;
    private final String description;

    private Discount(Identity discountId, Money amount, String description)
    {
        this.discountId = discountId;
        this.amount = amount;
        this.description = description;
    }

    public static Discount create(Money amount, String description)
    {
        return new Discount(Identity.EMPTY, amount, description);
    }

    public static Discount create(Identity discountId, Money amount, String description)
    {
        return new Discount(discountId, amount, description);
    }

    public Money getAmount()
    {
        return this.amount;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Identity getDiscountId()
    {
        return this.discountId;
    }
}
