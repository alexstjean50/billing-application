package ca.ulaval.glo4002.billing.service.dto.request.assembler;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.bill.Item;
import ca.ulaval.glo4002.billing.service.dto.request.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestAssembler
{
    public Item toDomainModel(ItemRequest itemRequest)
    {
        return Item.create(Money.valueOf(itemRequest.getPrice()), itemRequest.getNote(), itemRequest.getProductId(),
                itemRequest.getQuantity());
    }

    public List<Item> toDomainModel(List<ItemRequest> itemRequests)
    {
        return itemRequests.stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }
}
