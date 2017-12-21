package ca.ulaval.glo4002.billing.service.dto.response.assembler;

import ca.ulaval.glo4002.billing.service.dto.response.PaymentCreationResponse;

public class PaymentCreationResponseAssembler
{
    public PaymentCreationResponse toResponse(long paymentNumber)
    {
        return PaymentCreationResponse.create(paymentNumber);
    }
}
