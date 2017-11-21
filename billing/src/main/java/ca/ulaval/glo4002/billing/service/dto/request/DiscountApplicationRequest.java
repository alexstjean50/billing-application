package ca.ulaval.glo4002.billing.service.dto.request;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class DiscountApplicationRequest
{
    @NotNull(message = "amount cannot be null")
    @DecimalMin(value = "0.00", message = "amount cannot be less than 0.00")
    public BigDecimal amount;
    @NotNull(message = "description cannot be null")
    public String description;

    public static DiscountApplicationRequest create(BigDecimal amount, String description)
    {
        DiscountApplicationRequest discountApplicationRequest = new DiscountApplicationRequest();
        discountApplicationRequest.amount = amount;
        discountApplicationRequest.description = description;
        return discountApplicationRequest;
    }
}
