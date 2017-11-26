package ca.ulaval.glo4002.billing.service.dto.request.assembler;

import ca.ulaval.glo4002.billing.domain.billing.bill.Item;
import ca.ulaval.glo4002.billing.service.dto.request.ItemRequest;
import ca.ulaval.glo4002.billing.service.validator.ProductValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ItemRequestAssemblerTest
{
    private static final int SOME_QUANTITY = 4;
    private static final long SOME_PRODUCT_ID = 12;
    private static final String SOME_NOTE = "Quintessa";
    private static final BigDecimal SOME_PRICE = BigDecimal.valueOf(62);
    @Mock
    private ProductValidator productValidator;
    private ItemRequestAssembler itemAssembler;

    @Before
    public void initializeItemRequestAssembler()
    {
        this.itemAssembler = new ItemRequestAssembler(this.productValidator);
    }

    @Test
    public void givenAnItemRequest_whenConvertedToItem_thenProductIdsShouldBeEqual()
    {
        List<ItemRequest> someItemRequests = createSomeItemRequests();

        List<Item> conversionResults = this.itemAssembler.toDomainModel(someItemRequests);

        assertEquals(someItemRequests.get(0).getProductId(), conversionResults.get(0).getProductId());
    }

    @Test
    public void givenAnItemRequest_whenConvertedToItem_thenQuantitiesShouldBeEqual()
    {
        List<ItemRequest> someItemRequests = createSomeItemRequests();

        List<Item> conversionResults = this.itemAssembler.toDomainModel(someItemRequests);

        assertEquals(someItemRequests.get(0).getQuantity(), conversionResults.get(0).getQuantity());
    }

    @Test
    public void givenAnItemRequest_whenConvertedToItem_thenNotesShouldBeEqual()
    {
        List<ItemRequest> someItemRequests = createSomeItemRequests();

        List<Item> conversionResults = this.itemAssembler.toDomainModel(someItemRequests);

        assertEquals(someItemRequests.get(0).getNote(), conversionResults.get(0).getNote());
    }

    @Test
    public void givenAnItemRequest_whenConvertedToItem_thenPricesShouldBeEqual()
    {
        List<ItemRequest> someItemRequests = createSomeItemRequests();

        List<Item> conversionResults = this.itemAssembler.toDomainModel(someItemRequests);

        assertEquals(someItemRequests.get(0).getPrice(), conversionResults.get(0).getUnitPrice()
                .asBigDecimal());
    }

    private List<ItemRequest> createSomeItemRequests()
    {
        return Collections.singletonList(createSomeItemRequest());
    }

    private ItemRequest createSomeItemRequest()
    {
        return new ItemRequest(SOME_PRICE, SOME_NOTE, SOME_PRODUCT_ID, SOME_QUANTITY);
    }
}