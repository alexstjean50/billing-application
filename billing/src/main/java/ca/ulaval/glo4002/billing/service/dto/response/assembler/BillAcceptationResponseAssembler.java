package ca.ulaval.glo4002.billing.service.dto.response.assembler;

import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.service.dto.response.BillAcceptationResponse;

public class BillAcceptationResponseAssembler
{
    public BillAcceptationResponse toResponse(Bill bill)
    {
        return BillAcceptationResponse.create(bill.getBillNumber(), bill.getEffectiveDate(),
                bill.calculateExpectedPaymentDate(), bill.getDueTerm());
    }
}
