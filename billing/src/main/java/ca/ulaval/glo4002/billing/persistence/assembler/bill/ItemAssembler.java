package ca.ulaval.glo4002.billing.persistence.assembler.bill;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.bill.Item;
import ca.ulaval.glo4002.billing.persistence.entity.ItemEntity;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;

public class ItemAssembler
{
    public Item toDomainModel(ItemEntity itemEntity)
    {
        return Item.create(new Identity(itemEntity.getItemId()), Money.valueOf(itemEntity.getPaidPrice()),
                itemEntity.getNote(),
                itemEntity.getProductId(), itemEntity.getQuantity());
    }

    public ItemEntity toPersistenceModel(Item item)
    {
        return new ItemEntity((item.getItemId()).getId(), item.getProductId(), item.getQuantity(),
                item.getUnitPrice()
                        .asBigDecimal(), item.getNote());
    }
}
