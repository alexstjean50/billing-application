package ca.ulaval.glo4002.billing.service.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ItemRequest
{
    @NotNull(message = "item.clientId cannot be null")
    private final long productId;
    @DecimalMin(value = "0.00", message = "item.price cannot be less than 0.00")
    @NotNull(message = "item.price cannot be null")
    private final BigDecimal price;
    @NotNull(message = "item.note cannot be null")
    private final String note;
    @Min(value = 0, message = "item.quantity cannot be less than 0")
    @NotNull(message = "item.quantity cannot be null")
    private final int quantity;

    @JsonCreator
    public ItemRequest(@JsonProperty("price") BigDecimal price, @JsonProperty("note") String note,
                       @JsonProperty("productId") long productId, @JsonProperty("quantity") int quantity)
    {
        this.productId = productId;
        this.price = price;
        this.note = note;
        this.quantity = quantity;
    }

    public long getProductId()
    {
        return this.productId;
    }

    public BigDecimal getPrice()
    {
        return this.price;
    }

    public String getNote()
    {
        return this.note;
    }

    public int getQuantity()
    {
        return this.quantity;
    }
}
