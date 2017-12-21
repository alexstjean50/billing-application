package ca.ulaval.glo4002.billing.service;

import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainBillAssembler;
import ca.ulaval.glo4002.billing.service.dto.request.BillCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.response.BillAcceptationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.BillCreationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.BillAcceptationResponseAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.BillCreationResponseAssembler;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.clock.ClockRepository;
import ca.ulaval.glo4002.billing.service.retriever.AccountRetriever;

import java.math.BigDecimal;

public class BillService
{
    private final AccountRepository accountRepository;
    private final ClockRepository clockRepository;
    private final DomainBillAssembler domainBillAssembler;
    private final BillCreationResponseAssembler billCreationResponseAssembler;
    private final BillAcceptationResponseAssembler billAcceptationResponseAssembler;
    private final AccountRetriever accountRetriever;

    public BillService(AccountRepository accountRepository,
                       ClockRepository clockRepository, DomainBillAssembler domainBillAssembler,
                       BillCreationResponseAssembler billCreationResponseAssembler,
                       BillAcceptationResponseAssembler billAcceptationResponseAssembler,
                       AccountRetriever accountRetriever)
    {
        this.accountRepository = accountRepository;
        this.clockRepository = clockRepository;
        this.domainBillAssembler = domainBillAssembler;
        this.billCreationResponseAssembler = billCreationResponseAssembler;
        this.billAcceptationResponseAssembler = billAcceptationResponseAssembler;
        this.accountRetriever = accountRetriever;
    }

    public BillCreationResponse createBill(BillCreationRequest request)
    {
        Account account = this.accountRetriever.retrieveClientAccount(request.getClientId());

        Bill createdBill = this.domainBillAssembler.toBill(request, account.getClient());
        account.addBill(createdBill);

        this.accountRepository.save(account);

        return this.billCreationResponseAssembler.toResponse(createdBill);
    }

    public void cancelBill(long billNumber)
    {
        Account account = this.accountRepository.findByBillNumber(billNumber);
        account.cancelBill(billNumber, this.clockRepository.retrieveCurrentTime());
        this.accountRepository.save(account);
    }

    public BillAcceptationResponse acceptBill(long billNumber)
    {
        Account account = this.accountRepository.findByBillNumber(billNumber);
        Bill bill = account.acceptBill(billNumber, this.clockRepository.retrieveCurrentTime());
        this.accountRepository.save(account);
        return this.billAcceptationResponseAssembler.toResponse(bill);
    }

    public long retrieveRelatedClientId(long billNumber)
    {
        Account account = this.accountRepository.findByBillNumber(billNumber);

        return account.getClientId();
    }

    public BigDecimal retrieveBillAmount(long billNumber)
    {
        Account account = this.accountRepository.findByBillNumber(billNumber);

        return account.retrieveBillAmount(billNumber);
    }
}
