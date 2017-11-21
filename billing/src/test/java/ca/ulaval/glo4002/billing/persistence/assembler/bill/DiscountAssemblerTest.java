package ca.ulaval.glo4002.billing.persistence.assembler.bill;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.bill.Discount;
import ca.ulaval.glo4002.billing.persistence.entity.DiscountEntity;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DiscountAssemblerTest
{
    private static final String SOME_TEXT = "";
    private static final Money SOME_AMOUNT = Money.valueOf(42);
    private static final long SOME_ID = 3;
    private static final Identity SOME_IDENTITY = new Identity(SOME_ID);
    private static final long SOME_OTHER_ID = 21;
    private static final Money SOME_OTHER_AMOUNT = Money.valueOf(123);
    private static final String SOME_OTHER_TEXT = "IronHide";
    private DiscountAssembler discountAssembler;

    @Before
    public void initializeDiscountAssembler()
    {
        this.discountAssembler = new DiscountAssembler();
    }

    @Test
    public void givenADiscount_whenConvertedToDiscountEntity_thenDiscountEntityIdShouldCorrespondToDiscountIdentity()
    {
        long expectedId = SOME_OTHER_ID;
        Identity someOtherIdentity = new Identity(expectedId);
        Discount someDiscount = Discount.create(someOtherIdentity, SOME_AMOUNT, SOME_TEXT);

        DiscountEntity result = this.discountAssembler.toPersistenceModel(someDiscount);

        assertEquals(expectedId, result.getDiscountId());
    }

    @Test
    public void givenADiscountEntity_whenConvertedToDiscount_thenDiscountEntityIdCorrespondToDiscountIdentity()
    {
        Identity expectedIdentity = new Identity(SOME_OTHER_ID);
        DiscountEntity someDiscountEntity = createSomeDiscountEntity();
        someDiscountEntity.setDiscountId(SOME_OTHER_ID);

        Discount result = this.discountAssembler.toDomainModel(someDiscountEntity);

        assertEquals(expectedIdentity, result.getDiscountId());
    }

    @Test
    public void givenADiscount_whenConvertedToDiscountEntity_thenAmountsShouldBeEqual()
    {
        Money expectedAmount = SOME_OTHER_AMOUNT;
        Discount someDiscount = Discount.create(SOME_IDENTITY, expectedAmount, SOME_TEXT);

        DiscountEntity result = this.discountAssembler.toPersistenceModel(someDiscount);

        assertEquals(expectedAmount.asBigDecimal(), result.getAmount());
    }

    @Test
    public void givenADiscountEntity_whenConvertedToDiscount_thenAmountsShouldBeEqual()
    {
        Money expectedAmount = SOME_OTHER_AMOUNT;
        DiscountEntity someDiscountEntity = createSomeDiscountEntity();
        someDiscountEntity.setAmount(expectedAmount.asBigDecimal());

        Discount result = this.discountAssembler.toDomainModel(someDiscountEntity);

        assertEquals(expectedAmount, result.getAmount());
    }

    @Test
    public void givenADiscount_whenConvertedToDiscountEntity_thenDescriptionsShouldBeEqual()
    {
        String expectedDescription = SOME_OTHER_TEXT;
        Discount someDiscount = Discount.create(SOME_IDENTITY, SOME_AMOUNT, expectedDescription);

        DiscountEntity result = this.discountAssembler.toPersistenceModel(someDiscount);

        assertEquals(expectedDescription, result.getDescription());
    }

    @Test
    public void givenADiscountEntity_whenConvertedToDiscount_thenDescriptionsShouldBeEqual()
    {
        String expectedDescription = SOME_OTHER_TEXT;
        DiscountEntity someDiscountEntity = createSomeDiscountEntity();
        someDiscountEntity.setDescription(expectedDescription);

        Discount result = this.discountAssembler.toDomainModel(someDiscountEntity);

        assertEquals(expectedDescription, result.getDescription());
    }

    private DiscountEntity createSomeDiscountEntity()
    {
        return new DiscountEntity(SOME_ID, SOME_AMOUNT.asBigDecimal(), SOME_TEXT);
    }
}
