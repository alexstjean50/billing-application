package ca.ulaval.glo4002.billing.service.dto.response.assembler;

import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.service.dto.response.BillCreationResponse;

public class BillCreationResponseAssembler
{
    private static final String BILL_TEMPLATE_URL = "/bills/%d";

    public BillCreationResponse toResponse(Bill createdBill)
    {
        String billUrl = String.format(BILL_TEMPLATE_URL, createdBill.getBillNumber());
        return BillCreationResponse.create(createdBill.getBillNumber(), createdBill.calculateTotalItemPrice()
                        .asBigDecimal(),
                createdBill.getDueTerm()
                        .name(), billUrl);
    }
}
