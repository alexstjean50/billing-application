package ca.ulaval.glo4002.billing.persistence.assembler.bill;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.bill.Discount;
import ca.ulaval.glo4002.billing.persistence.entity.DiscountEntity;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;

public class DiscountAssembler
{
    public Discount toDomainModel(DiscountEntity discountEntity)
    {
        return Discount.create(new Identity(discountEntity.getDiscountId()),
                Money.valueOf(discountEntity.getAmount()), discountEntity.getDescription());
    }

    public DiscountEntity toPersistenceModel(Discount discount)
    {
        return new DiscountEntity((discount.getDiscountId()).getId(), discount.getAmount()
                .asBigDecimal(), discount.getDescription());
    }
}
