package ca.ulaval.glo4002.billing.domain.billing.bill;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ItemTest
{
    private static final Identity SOME_ITEM_ID = mock(Identity.class);
    private static final long SOME_ITEM_PRODUCT_ID = 4;
    private static final Money SOME_ITEM_UNIT_PRICE = Money.valueOf(42);
    private static final int SOME_ITEM_QUANTITY = 13;
    private static final String SOME_ITEM_NOTE = "This is the item note.";

    @Test
    public void givenAnItemWithAUnitPriceAndAQuantity_whenCalculatePrice_thenShouldHaveUnitPriceTimesQuantity()
    {
        Item item = Item.create(SOME_ITEM_ID, SOME_ITEM_UNIT_PRICE, SOME_ITEM_NOTE, SOME_ITEM_PRODUCT_ID,
                SOME_ITEM_QUANTITY);
        Money expectedPrice = SOME_ITEM_UNIT_PRICE.multiply(BigDecimal.valueOf(SOME_ITEM_QUANTITY));

        Money priceCalculationResult = item.calculatePrice();

        assertEquals(expectedPrice, priceCalculationResult);
    }
}
