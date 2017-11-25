package ca.ulaval.glo4002.billing.service.dto.response.assembler;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.bill.Item;
import ca.ulaval.glo4002.billing.service.dto.response.BillResponse;

import java.util.List;

public class BillResponseAssembler
{
    private final ItemResponseAssembler itemResponseAssembler;

    public BillResponseAssembler(ItemResponseAssembler itemResponseAssembler)
    {
        this.itemResponseAssembler = itemResponseAssembler;
    }

    public BillResponse toResponse(long billNumber, long clientId, List<Item> items, Money amount)
    {
        return BillResponse.create(billNumber, clientId, this.itemResponseAssembler.toResponses(items), amount
                .asBigDecimal());
    }
}
