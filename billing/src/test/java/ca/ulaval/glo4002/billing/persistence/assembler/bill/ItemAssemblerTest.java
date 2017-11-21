package ca.ulaval.glo4002.billing.persistence.assembler.bill;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.bill.Item;
import ca.ulaval.glo4002.billing.persistence.entity.ItemEntity;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ItemAssemblerTest
{
    private static final long SOME_OTHER_ID = 32;
    private static final int SOME_QUANTITY = 21;
    private static final long SOME_PRODUCT_ID = 87;
    private static final String SOME_TEXT = "Ratchet";
    private static final Money SOME_PRICE = Money.valueOf(55);
    private static final long SOME_OTHER_PRODUCT_ID = 6543987;
    private static final int SOME_OTHER_QUANTITY = 43276;
    private static final String SOME_OTHER_TEXT = "Grimlock";
    private static final Money SOME_OTHER_PRICE = Money.valueOf(42783);
    private static final long SOME_ID = 432176;
    private ItemAssembler itemAssembler;

    @Before
    public void initializeItemAssembler()
    {
        this.itemAssembler = new ItemAssembler();
    }

    @Test
    public void givenAnItem_whenConvertedToItemEntity_thenItemIdentityShouldCorrespondToItemEntityId()
    {
        long expectedId = SOME_OTHER_ID;
        Identity someIdentity = new Identity(expectedId);
        Item someItem = Item.create(someIdentity, SOME_PRICE, SOME_TEXT, SOME_PRODUCT_ID, SOME_QUANTITY);

        ItemEntity result = this.itemAssembler.toPersistenceModel(someItem);

        assertEquals(expectedId, result.getItemId());
    }

    @Test
    public void givenAnItemEntity_whenConvertedToItem_thenItemIdentityShouldCorrespondToItemEntityId()
    {
        Identity expectedIdentity = new Identity(SOME_OTHER_ID);
        ItemEntity someItemEntity = new ItemEntity(SOME_OTHER_ID, SOME_PRODUCT_ID, SOME_QUANTITY,
                SOME_PRICE.asBigDecimal(), SOME_TEXT);

        Item result = this.itemAssembler.toDomainModel(someItemEntity);

        assertEquals(expectedIdentity, result.getItemId());
    }

    @Test
    public void givenAnItem_whenConvertedToItemEntity_thenProductIdsShouldBeEqual()
    {
        long expectedProductId = SOME_OTHER_PRODUCT_ID;
        Item someItem = Item.create(SOME_PRICE, SOME_TEXT, expectedProductId, SOME_QUANTITY);

        ItemEntity result = this.itemAssembler.toPersistenceModel(someItem);

        assertEquals(expectedProductId, result.getProductId());
    }

    @Test
    public void givenAnItemEntity_whenConvertedToItem_thenProductIdsShouldBeEqual()
    {
        long expectedProductId = SOME_OTHER_PRODUCT_ID;
        ItemEntity someItemEntity = new ItemEntity(SOME_ID, expectedProductId, SOME_QUANTITY, SOME_PRICE.asBigDecimal(),
                SOME_TEXT);

        Item result = this.itemAssembler.toDomainModel(someItemEntity);

        assertEquals(expectedProductId, result.getProductId());
    }

    @Test
    public void givenAnItem_whenConvertedToItemEntity_thenQuantitiesShouldBeEqual()
    {
        int expectedQuantity = SOME_OTHER_QUANTITY;
        Item someItem = Item.create(SOME_PRICE, SOME_TEXT, SOME_PRODUCT_ID, expectedQuantity);

        ItemEntity result = this.itemAssembler.toPersistenceModel(someItem);

        assertEquals(expectedQuantity, result.getQuantity());
    }

    @Test
    public void givenAnItemEntity_whenConvertedToItem_thenQuantitiesShouldBeEqual()
    {
        int expectedQuantity = SOME_OTHER_QUANTITY;
        ItemEntity someItemEntity = new ItemEntity(SOME_ID, SOME_PRODUCT_ID, expectedQuantity,
                SOME_PRICE.asBigDecimal(), SOME_TEXT);

        Item result = this.itemAssembler.toDomainModel(someItemEntity);

        assertEquals(expectedQuantity, result.getQuantity());
    }

    @Test
    public void givenAnItem_whenConvertedToItemEntity_thenNotesShouldBeEqual()
    {
        String expectedNote = SOME_OTHER_TEXT;
        Item someItem = Item.create(SOME_PRICE, expectedNote, SOME_PRODUCT_ID, SOME_QUANTITY);

        ItemEntity result = this.itemAssembler.toPersistenceModel(someItem);

        assertEquals(expectedNote, result.getNote());
    }

    @Test
    public void givenAnItemEntity_whenConvertedToItem_thenNotesShouldBeEqual()
    {
        String expectedNote = SOME_OTHER_TEXT;
        ItemEntity someItemEntity = new ItemEntity(SOME_ID, SOME_PRODUCT_ID, SOME_QUANTITY, SOME_PRICE.asBigDecimal(),
                expectedNote);

        Item result = this.itemAssembler.toDomainModel(someItemEntity);

        assertEquals(expectedNote, result.getNote());
    }

    @Test
    public void givenAnItem_whenConvertedToItemEntity_thenPricesShouldBeEqual()
    {
        Money expectedPrice = SOME_OTHER_PRICE;
        Item someItem = Item.create(expectedPrice, SOME_TEXT, SOME_PRODUCT_ID, SOME_QUANTITY);

        ItemEntity result = this.itemAssembler.toPersistenceModel(someItem);

        assertEquals(expectedPrice.asBigDecimal(), result.getPaidPrice());
    }

    @Test
    public void givenAnItemEntity_whenConvertedToItem_thenPricesShouldBeEqual()
    {
        Money expectedPrice = SOME_OTHER_PRICE;
        ItemEntity someItemEntity = new ItemEntity(SOME_ID, SOME_PRODUCT_ID, SOME_QUANTITY,
                expectedPrice.asBigDecimal(), SOME_TEXT);

        Item result = this.itemAssembler.toDomainModel(someItemEntity);

        assertEquals(expectedPrice, result.getUnitPrice());
    }
}
