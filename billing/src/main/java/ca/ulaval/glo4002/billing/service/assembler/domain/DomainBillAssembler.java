package ca.ulaval.glo4002.billing.service.assembler.domain;

import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.domain.billing.bill.Item;
import ca.ulaval.glo4002.billing.domain.billing.client.Client;
import ca.ulaval.glo4002.billing.domain.billing.client.DueTerm;
import ca.ulaval.glo4002.billing.service.dto.request.BillCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.assembler.ItemRequestAssembler;
import ca.ulaval.glo4002.billing.service.repository.bill.BillRepository;

import java.util.List;
import java.util.Optional;

public class DomainBillAssembler
{
    private final BillRepository billRepository;
    private final ItemRequestAssembler itemRequestAssembler;

    public DomainBillAssembler(BillRepository billRepository, ItemRequestAssembler itemRequestAssembler)
    {
        this.billRepository = billRepository;
        this.itemRequestAssembler = itemRequestAssembler;
    }

    public Bill toBill(BillCreationRequest request, Client client)
    {
        long billNumber = this.billRepository.retrieveNextBillNumber();
        List<Item> items = this.itemRequestAssembler.toDomainModel(request.getItemRequests());

        DueTerm appliedDueTerm = Optional.ofNullable(request.getDueTerm())
                .map(DueTerm::valueOf)
                .orElseGet(client::getDefaultTerm);

        return Bill.create(billNumber, request.getCreationDate(), appliedDueTerm, items);
    }
}
