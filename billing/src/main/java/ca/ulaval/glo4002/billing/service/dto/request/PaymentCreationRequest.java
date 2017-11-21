package ca.ulaval.glo4002.billing.service.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PaymentCreationRequest
{
    @NotNull(message = "clientId cannot be null")
    public long clientId;
    @NotNull(message = "amount cannot be null")
    @DecimalMin(value = "0.00", message = "amount cannot be less than 0.00")
    public BigDecimal amount;
    @Valid
    @NotNull(message = "paymentMethod cannot be null")
    public PaymentMethod paymentMethod;

    public static PaymentCreationRequest create(long clientId, BigDecimal amount, PaymentMethod paymentMethod)
    {
        PaymentCreationRequest paymentCreationRequest = new PaymentCreationRequest();
        paymentCreationRequest.clientId = clientId;
        paymentCreationRequest.amount = amount;
        paymentCreationRequest.paymentMethod = paymentMethod;
        return paymentCreationRequest;
    }
}
