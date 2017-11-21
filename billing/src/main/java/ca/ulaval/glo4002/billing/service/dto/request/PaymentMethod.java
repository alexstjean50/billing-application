package ca.ulaval.glo4002.billing.service.dto.request;

import javax.validation.constraints.NotNull;

public class PaymentMethod
{
    @NotNull(message = "paymentMethod.account cannot be null")
    public String account;
    @NotNull(message = "paymentMethod.source cannot be null")
    public String source;
}
