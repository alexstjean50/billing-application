package ca.ulaval.glo4002.billing.service.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class BillResponse
{
    public final long id;
    public final long clientId;
    public final List<ItemResponse> items;
    public final BigDecimal total;

    private BillResponse(long id, long clientId, List<ItemResponse> items, BigDecimal total)
    {
        this.id = id;
        this.clientId = clientId;
        this.items = items;
        this.total = total;
    }

    public static BillResponse create(long id, long clientId, List<ItemResponse> items, BigDecimal total)
    {
        return new BillResponse(id, clientId, items, total);
    }
}
