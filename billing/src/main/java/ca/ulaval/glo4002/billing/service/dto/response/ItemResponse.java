package ca.ulaval.glo4002.billing.service.dto.response;

import java.math.BigDecimal;

public class ItemResponse
{
    public final BigDecimal amount;
    public final String description;
    public final long productId;
    public final long quantity;
    public final BigDecimal total;

    private ItemResponse(BigDecimal amount, String description, long productId, long quantity, BigDecimal total)
    {
        this.amount = amount;
        this.description = description;
        this.productId = productId;
        this.quantity = quantity;
        this.total = total;
    }

    public static ItemResponse create(BigDecimal amount, String description, long productId, long quantity,
                                      BigDecimal total)
    {
        return new ItemResponse(amount, description, productId, quantity, total);
    }
}
