package ca.ulaval.glo4002.billing.service.dto.response.assembler;

import ca.ulaval.glo4002.billing.domain.billing.bill.Item;
import ca.ulaval.glo4002.billing.service.dto.response.ItemResponse;

import java.util.List;
import java.util.stream.Collectors;

public class ItemResponseAssembler
{
    public List<ItemResponse> toResponses(List<Item> items)
    {
        return items.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ItemResponse toResponse(Item item)
    {
        return ItemResponse.create(item.getUnitPrice()
                .asBigDecimal(), item.getNote(), item.getProductId(), item.getQuantity(), item.calculatePrice()
                .asBigDecimal());
    }
}
