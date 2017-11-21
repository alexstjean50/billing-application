package ca.ulaval.glo4002.billing.service.dto.request.assembler;

import ca.ulaval.glo4002.billing.domain.billing.bill.Item;
import ca.ulaval.glo4002.billing.service.dto.request.ItemRequest;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class ItemRequestAssemblerTest
{
    private static final int SOME_QUANTITY = 4;
    private static final long SOME_PRODUCT_ID = 12;
    private static final String SOME_NOTE = "Quintessa";
    private static final BigDecimal SOME_PRICE = BigDecimal.valueOf(62);
    private ItemRequestAssembler itemAssembler;

    @Before
    public void initializeItemRequestAssembler()
    {
        this.itemAssembler = new ItemRequestAssembler();
    }

    @Test
    public void givenAnItemRequest_whenConvertedToItem_thenProductIdsShouldBeEqual()
    {
        ItemRequest someItemRequest = createSomeItemRequest();

        Item conversionResult = this.itemAssembler.toDomainModel(someItemRequest);

        assertEquals(someItemRequest.getProductId(), conversionResult.getProductId());
    }

    @Test
    public void givenAnItemRequest_whenConvertedToItem_thenQuantitiesShouldBeEqual()
    {
        ItemRequest someItemRequest = createSomeItemRequest();

        Item conversionResult = this.itemAssembler.toDomainModel(someItemRequest);

        assertEquals(someItemRequest.getQuantity(), conversionResult.getQuantity());
    }

    @Test
    public void givenAnItemRequest_whenConvertedToItem_thenNotesShouldBeEqual()
    {
        ItemRequest someItemRequest = createSomeItemRequest();

        Item conversionResult = this.itemAssembler.toDomainModel(someItemRequest);

        assertEquals(someItemRequest.getNote(), conversionResult.getNote());
    }

    @Test
    public void givenAnItemRequest_whenConvertedToItem_thenPricesShouldBeEqual()
    {
        ItemRequest someItemRequest = createSomeItemRequest();

        Item conversionResult = this.itemAssembler.toDomainModel(someItemRequest);

        assertEquals(someItemRequest.getPrice(), conversionResult.getUnitPrice()
                .asBigDecimal());
    }

    private ItemRequest createSomeItemRequest()
    {
        return new ItemRequest(SOME_PRICE, SOME_NOTE, SOME_PRODUCT_ID, SOME_QUANTITY);
    }
}