package ca.ulaval.glo4002.billing.service.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PaymentCreationRequest
{
    @NotNull(message = "clientId cannot be null")
    private long clientId;
    @NotNull(message = "amount cannot be null")
    @DecimalMin(value = "0.00", message = "amount cannot be less than 0.00")
    private BigDecimal amount;
    @Valid
    @NotNull(message = "paymentMethod cannot be null")
    private PaymentMethod paymentMethod;

    public PaymentCreationRequest()
    {
    }

    public PaymentCreationRequest(long clientId, BigDecimal amount, PaymentMethod paymentMethod)
    {
        this.clientId = clientId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public long getClientId()
    {
        return this.clientId;
    }

    public void setClientId(long clientId)
    {
        this.clientId = clientId;
    }

    public BigDecimal getAmount()
    {
        return this.amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod()
    {
        return this.paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod)
    {
        this.paymentMethod = paymentMethod;
    }
}
