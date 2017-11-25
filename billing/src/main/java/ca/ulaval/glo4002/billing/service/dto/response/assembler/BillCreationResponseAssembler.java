package ca.ulaval.glo4002.billing.service.dto.response.assembler;

import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.service.dto.response.BillCreationResponse;

public class BillCreationResponseAssembler
{
    public BillCreationResponse toCreationResponse(Bill createdBill)
    {
        return BillCreationResponse.create(createdBill.getBillNumber(), createdBill.calculateSubTotal()
                        .asBigDecimal(),
                createdBill.getDueTerm()
                        .name());
    }
}
