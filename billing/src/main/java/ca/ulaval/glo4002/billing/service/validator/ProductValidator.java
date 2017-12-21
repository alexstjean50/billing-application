package ca.ulaval.glo4002.billing.service.validator;

import ca.ulaval.glo4002.billing.service.dto.request.ItemRequest;
import ca.ulaval.glo4002.billing.service.repository.product.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ProductValidator
{
    private final ProductRepository productRepository;

    public ProductValidator(ProductRepository productRepository)
    {
        this.productRepository = productRepository;
    }

    public void validateThatProductsExist(List<ItemRequest> itemRequests)
    {
        List<Long> productIds = itemRequests.stream()
                .map(ItemRequest::getProductId)
                .collect(Collectors.toList());
        this.productRepository.findAll(productIds);
    }
}
