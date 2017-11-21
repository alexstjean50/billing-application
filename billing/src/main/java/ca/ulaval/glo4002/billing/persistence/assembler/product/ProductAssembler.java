package ca.ulaval.glo4002.billing.persistence.assembler.product;

import ca.ulaval.glo4002.billing.domain.billing.product.Product;
import ca.ulaval.glo4002.billing.persistence.entity.ProductEntity;

public class ProductAssembler
{
    public Product toDomainModel(ProductEntity productEntity)
    {
        return new Product(productEntity.getId(), productEntity.getName(), productEntity.getUnitPrice());
    }
}
