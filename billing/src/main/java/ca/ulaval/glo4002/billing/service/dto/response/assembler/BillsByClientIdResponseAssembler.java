package ca.ulaval.glo4002.billing.service.dto.response.assembler;

import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.service.dto.response.BillResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BillsByClientIdResponseAssembler
{
    private final BillResponseAssembler billResponseAssembler;

    public BillsByClientIdResponseAssembler(BillResponseAssembler billResponseAssembler)
    {
        this.billResponseAssembler = billResponseAssembler;
    }

    public List<BillResponse> toResponses(Map<Long, List<Bill>> billsByClientId)
    {
        List<BillResponse> clientsBillsResponses = new ArrayList<>();
        for (Map.Entry<Long, List<Bill>> clientBills : billsByClientId.entrySet())
        {
            List<BillResponse> createdBillResponses = createBillsResponses(clientBills.getKey(),
                    clientBills.getValue());
            clientsBillsResponses.addAll(createdBillResponses);
        }
        return clientsBillsResponses;
    }

    private List<BillResponse> createBillsResponses(long clientId, List<Bill> bills)
    {
        return bills.stream()
                .map(bill -> createBillResponse(bill, clientId))
                .collect(Collectors.toList());
    }

    private BillResponse createBillResponse(Bill bill, long clientId)
    {
        return this.billResponseAssembler.toResponse(bill.getBillNumber(), clientId,
                bill.getItems(),
                bill.calculateSubTotal());
    }
}
