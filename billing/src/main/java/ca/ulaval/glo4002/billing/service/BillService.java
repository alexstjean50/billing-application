package ca.ulaval.glo4002.billing.service;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.domain.billing.bill.Discount;
import ca.ulaval.glo4002.billing.domain.billing.bill.Item;
import ca.ulaval.glo4002.billing.domain.billing.client.DueTerm;
import ca.ulaval.glo4002.billing.service.dto.request.BillCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.BillStatusParameter;
import ca.ulaval.glo4002.billing.service.dto.request.DiscountApplicationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.ItemRequest;
import ca.ulaval.glo4002.billing.service.dto.request.assembler.ItemRequestAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.BillAcceptationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.BillCreationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.BillResponse;
import ca.ulaval.glo4002.billing.service.dto.response.ItemResponse;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.BillAcceptationResponseAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.BillCreationResponseAssembler;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.bill.BillRepository;
import ca.ulaval.glo4002.billing.service.repository.product.ProductRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BillService
{
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final BillRepository billRepository;
    private final ItemRequestAssembler itemRequestAssembler;
    private final BillCreationResponseAssembler billCreationResponseAssembler;
    private final BillAcceptationResponseAssembler billAcceptationResponseAssembler;
    private final AccountRetriever accountRetriever;

    public BillService(AccountRepository accountRepository, ProductRepository productRepository,
                       BillRepository billRepository, ItemRequestAssembler itemRequestAssembler,
                       BillCreationResponseAssembler billCreationResponseAssembler,
                       BillAcceptationResponseAssembler billAcceptationResponseAssembler,
                       AccountRetriever accountRetriever)
    {
        this.accountRepository = accountRepository;
        this.productRepository = productRepository;
        this.billRepository = billRepository;
        this.itemRequestAssembler = itemRequestAssembler;
        this.billCreationResponseAssembler = billCreationResponseAssembler;
        this.billAcceptationResponseAssembler = billAcceptationResponseAssembler;
        this.accountRetriever = accountRetriever;
    }

    public BillCreationResponse createBill(BillCreationRequest request)
    {
        this.validateThatProductIdsExist(request.itemRequests);
        Account account = this.accountRetriever.retrieveClientAccount(request.clientId);

        List<Item> items = this.itemRequestAssembler.toDomainModel(request.itemRequests);

        long newBillNumber = this.billRepository.retrieveNextBillNumber();
        account.createBill(newBillNumber, request.creationDate,
                Optional.ofNullable(request.dueTerm)
                        .map(DueTerm::valueOf), items);

        this.accountRepository.save(account);

        Bill createdBill = account.findBillByNumber(newBillNumber);
        return this.billCreationResponseAssembler.toResponse(createdBill);
    }

    private void validateThatProductIdsExist(List<ItemRequest> itemRequests)
    {
        List<Long> productIds = itemRequests.stream()
                .map(ItemRequest::getProductId)
                .collect(Collectors.toList());
        this.productRepository.findAll(productIds);
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
        account.acceptBill(billNumber, Instant.now());
        Bill bill = account.findBillByNumber(billNumber);
        this.accountRepository.save(account);
        return this.billAcceptationResponseAssembler.toResponse(bill);
    }

    public List<BillResponse> retrieveBills(Optional<Long> clientId, Optional<BillStatusParameter> status)
    {
        Map<Long, List<Bill>> billsByClientId = this.accountRepository.retrieveFilteredBillsOfClients(clientId, status);

        return createClientsBillResponses(billsByClientId);
    }

    public long retrieveRelatedClientId(long billNumber)
    {
        Account account = this.accountRepository.findByBillNumber(billNumber);

        return account.getClientId();
    }

    public BigDecimal retrieveBillAmount(long billNumber)
    {
        Account account = this.accountRepository.findByBillNumber(billNumber);

        return account.findBillByNumber(billNumber)
                .calculateSubTotal()
                .asBigDecimal();
    }

    private List<BillResponse> createClientsBillResponses(Map<Long, List<Bill>> billsByClientId)
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
                .map(bill -> this.createBillResponse(bill, clientId))
                .collect(Collectors.toList());
    }

    private BillResponse createBillResponse(Bill bill, long clientId)
    {
        return BillResponse.create(bill.getBillNumber(), clientId, createItemResponses(bill.getItems()),
                bill.calculateSubTotal()
                        .asBigDecimal());
    }

    private List<ItemResponse> createItemResponses(List<Item> items)
    {
        return items.stream()
                .map(this::createItemResponse)
                .collect(Collectors.toList());
    }

    private ItemResponse createItemResponse(Item item)
    {
        return ItemResponse.create(item.getUnitPrice()
                .asBigDecimal(), item.getNote(), item.getProductId(), item.getQuantity(), item.calculatePrice()
                .asBigDecimal());
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
