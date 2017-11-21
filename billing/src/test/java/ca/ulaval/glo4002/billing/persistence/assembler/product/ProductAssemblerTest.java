package ca.ulaval.glo4002.billing.persistence.assembler.product;

import ca.ulaval.glo4002.billing.domain.billing.product.Product;
import ca.ulaval.glo4002.billing.persistence.entity.ProductEntity;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class ProductAssemblerTest
{
    private static final long SOME_ID = 19871;
    private static final String SOME_NAME = "Fallen";
    private static final BigDecimal SOME_PRICE = BigDecimal.valueOf(132);
    private ProductAssembler productAssembler;

    @Before
    public void initializeProductAssembler()
    {
        this.productAssembler = new ProductAssembler();
    }

    @Test
    public void givenAProductEntity_whenConvertedToProduct_thenIdsShouldBeEqual()
    {
        ProductEntity someProductEntity = createSomeProductEntity();

        Product conversionResult = this.productAssembler.toDomainModel(someProductEntity);

        assertEquals(someProductEntity.getId(), conversionResult.getId());
    }

    @Test
    public void givenAProductEntity_whenConvertedToProduct_thenNamesShouldBeEqual()
    {
        ProductEntity someProductEntity = createSomeProductEntity();

        Product conversionResult = this.productAssembler.toDomainModel(someProductEntity);

        assertEquals(someProductEntity.getName(), conversionResult.getName());
    }

    @Test
    public void givenAProductEntity_whenConvertedToProduct_thenPricesShouldBeEqual()
    {
        ProductEntity someProductEntity = createSomeProductEntity();

        Product conversionResult = this.productAssembler.toDomainModel(someProductEntity);

        assertEquals(someProductEntity.getUnitPrice(), conversionResult.getUnitPrice());
    }

    private ProductEntity createSomeProductEntity()
    {
        return new ProductEntity(SOME_ID, SOME_NAME, SOME_PRICE);
    }
}