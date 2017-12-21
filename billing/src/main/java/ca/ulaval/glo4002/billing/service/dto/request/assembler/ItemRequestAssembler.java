package ca.ulaval.glo4002.billing.service.dto.request.assembler;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.bill.Item;
import ca.ulaval.glo4002.billing.service.dto.request.ItemRequest;
import ca.ulaval.glo4002.billing.service.validator.ProductValidator;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestAssembler
{
    private final ProductValidator productValidator;

    public ItemRequestAssembler(ProductValidator productValidator)
    {
        this.productValidator = productValidator;
    }

    public List<Item> toDomainModel(List<ItemRequest> itemRequests)
    {
        this.productValidator.validateThatProductsExist(itemRequests);
        return itemRequests.stream()
                .map(this::toDomainModel)
                .collect(Collectors.toList());
    }

    private Item toDomainModel(ItemRequest itemRequest)
    {
        return Item.create(Money.valueOf(itemRequest.getPrice()), itemRequest.getNote(), itemRequest.getProductId(),
                itemRequest.getQuantity());
    }
}
