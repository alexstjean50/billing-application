package ca.ulaval.glo4002.billing.service;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.domain.billing.bill.Discount;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainBillAssembler;
import ca.ulaval.glo4002.billing.service.dto.request.BillCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.BillStatusParameter;
import ca.ulaval.glo4002.billing.service.dto.request.DiscountApplicationRequest;
import ca.ulaval.glo4002.billing.service.dto.response.BillAcceptationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.BillCreationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.BillResponse;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.BillAcceptationResponseAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.BillCreationResponseAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.BillsByClientIdResponseAssembler;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.retriever.AccountRetriever;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BillService
{
    private final AccountRepository accountRepository;
    private final DomainBillAssembler domainBillAssembler;
    private final BillCreationResponseAssembler billCreationResponseAssembler;
    private final BillAcceptationResponseAssembler billAcceptationResponseAssembler;
    private final BillsByClientIdResponseAssembler billsByClientIdResponseAssembler;
    private final AccountRetriever accountRetriever;

    public BillService(AccountRepository accountRepository,
                       DomainBillAssembler domainBillAssembler,
                       BillCreationResponseAssembler billCreationResponseAssembler,
                       BillAcceptationResponseAssembler billAcceptationResponseAssembler,
                       BillsByClientIdResponseAssembler billsByClientIdResponseAssembler,
                       AccountRetriever accountRetriever)
    {
        this.accountRepository = accountRepository;
        this.domainBillAssembler = domainBillAssembler;
        this.billCreationResponseAssembler = billCreationResponseAssembler;
        this.billAcceptationResponseAssembler = billAcceptationResponseAssembler;
        this.billsByClientIdResponseAssembler = billsByClientIdResponseAssembler;
        this.accountRetriever = accountRetriever;
    }

    public BillCreationResponse createBill(BillCreationRequest request)
    {
        Account account = this.accountRetriever.retrieveClientAccount(request.clientId);

        Bill createdBill = this.domainBillAssembler.toBill(request, account.getClient());
        account.addBill(createdBill);

        this.accountRepository.save(account);

        return this.billCreationResponseAssembler.toResponse(createdBill);
    }

    public void cancelBill(long billNumber)
    {
        Account account = this.accountRepository.findByBillNumber(billNumber);
        account.cancelBill(billNumber);
        this.accountRepository.save(account);
    }

    public BillAcceptationResponse acceptBill(long billNumber)
    {
        Account account = this.accountRepository.findByBillNumber(billNumber);
        Bill bill = account.acceptBill(billNumber, Instant.now());
        this.accountRepository.save(account);
        return this.billAcceptationResponseAssembler.toResponse(bill);
    }

    public List<BillResponse> retrieveBills(Optional<Long> clientId, Optional<BillStatusParameter> status)
    {
        Map<Long, List<Bill>> billsByClientId = this.accountRepository.retrieveFilteredBillsOfClients(clientId, status);

        return this.billsByClientIdResponseAssembler.toResponses(billsByClientId);
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

    public void applyDiscount(long billNumber, DiscountApplicationRequest request)
    {
        Account account = this.accountRepository.findByBillNumber(billNumber);
        account.applyDiscount(billNumber, this.createDiscount(request));
        this.accountRepository.save(account);
    }

    private Discount createDiscount(DiscountApplicationRequest request)
    {
        return Discount.create(Money.valueOf(request.amount), request.description);
    }
}
