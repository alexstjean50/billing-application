package ca.ulaval.glo4002.billing.service.repository.product;

import ca.ulaval.glo4002.billing.domain.billing.product.Product;

import java.util.List;

public interface ProductRepository
{
    List<Product> findAll(List<Long> ids);
}
