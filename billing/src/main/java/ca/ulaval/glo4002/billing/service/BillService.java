package ca.ulaval.glo4002.billing.service;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.domain.billing.account.AccountFactory;
import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.domain.billing.bill.Discount;
import ca.ulaval.glo4002.billing.domain.billing.bill.Item;
import ca.ulaval.glo4002.billing.domain.billing.client.Client;
import ca.ulaval.glo4002.billing.domain.billing.client.DueTerm;
import ca.ulaval.glo4002.billing.persistence.repository.AccountClientNotFoundException;
import ca.ulaval.glo4002.billing.service.dto.request.BillCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.BillStatusParameter;
import ca.ulaval.glo4002.billing.service.dto.request.DiscountApplicationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.ItemRequest;
import ca.ulaval.glo4002.billing.service.dto.request.assembler.ItemRequestAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.BillAcceptationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.BillCreationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.BillResponse;
import ca.ulaval.glo4002.billing.service.dto.response.ItemResponse;
import ca.ulaval.glo4002.billing.service.filter.BillsFilter;
import ca.ulaval.glo4002.billing.service.filter.BillsFilterFactory;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.bill.BillRepository;
import ca.ulaval.glo4002.billing.service.repository.client.ClientRepository;
import ca.ulaval.glo4002.billing.service.repository.product.ProductRepository;
import com.google.common.collect.ImmutableMap;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class BillService
{
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final ProductRepository productRepository;
    private final BillRepository billRepository;
    private final ItemRequestAssembler itemRequestAssembler;
    private final BillsFilterFactory billsFilterFactory;
    private final AccountFactory accountFactory;

    public BillService(ClientRepository clientRepository, AccountRepository accountRepository,
                       ProductRepository productRepository, BillRepository billRepository,
                       ItemRequestAssembler itemRequestAssembler, BillsFilterFactory billsFilterFactory,
                       AccountFactory accountFactory)
    {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.billRepository = billRepository;
        this.itemRequestAssembler = itemRequestAssembler;
        this.billsFilterFactory = billsFilterFactory;
        this.accountFactory = accountFactory;
    }

    public BillCreationResponse createBill(BillCreationRequest request)
    {
        this.validateThatProductIdsExist(request.itemRequests);
        Account account = this.retrieveClientAccount(request.clientId);

        List<Item> items = request.itemRequests.stream()
                .map(this.itemRequestAssembler::toDomainModel)
                .collect(Collectors.toList());

        long newBillNumber = this.billRepository.retrieveNextBillNumber();
        account.createBill(newBillNumber, request.creationDate,
                Optional.ofNullable(request.dueTerm)
                        .map(DueTerm::valueOf), items);

        this.accountRepository.save(account);

        Bill createdBill = account.findBillByNumber(newBillNumber);
        return BillCreationResponse.create(createdBill.getBillNumber(), createdBill.calculateSubTotal()
                        .asBigDecimal(),
                createdBill.getDueTerm()
                        .name());
    }

    private void validateThatProductIdsExist(List<ItemRequest> itemRequests)
    {
        List<Long> productIds = itemRequests.stream()
                .map(ItemRequest::getProductId)
                .collect(Collectors.toList());
        this.productRepository.findAll(productIds);
    }

    private Account retrieveClientAccount(long clientId)
    {
        Account account;
        try
        {
            account = this.accountRepository.findByClientId(clientId);
        }
        catch (AccountClientNotFoundException exception)
        {
            Client client = this.clientRepository.findById(clientId);
            account = this.accountFactory.create(client);
        }
        return account;
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
        return BillAcceptationResponse.create(bill.getBillNumber(), bill.getEffectiveDate(),
                bill.calculateExpectedPaymentDate(), bill.getDueTerm());
    }

    public List<BillResponse> getBills(Optional<Long> clientId, Optional<BillStatusParameter> status)
    {
        Map<Long, List<Bill>> billsByClientId = retrieveBillsOfClients(clientId);

        if (status.isPresent())
        {
            billsByClientId = filterBills(status.get(), billsByClientId);
        }

        return createClientsBillResponses(billsByClientId);
    }

    private Map<Long, List<Bill>> retrieveBillsOfClients(Optional<Long> optionalClientId)
    {
        return optionalClientId.map(this::retrieveClientBills)
                .orElseGet(this::retrieveAllClientBills);
    }

    private Map<Long, List<Bill>> filterBills(BillStatusParameter status, Map<Long, List<Bill>> billsByClientId)
    {
        Map<Long, List<Bill>> filteredBillsByClientId = new HashMap<>();
        BillsFilter billsFilter = this.billsFilterFactory.create(status);

        for (Map.Entry<Long, List<Bill>> bill : billsByClientId.entrySet())
        {
            List<Bill> filteredBills = billsFilter.filter(bill.getValue());
            filteredBillsByClientId.put(bill.getKey(), filteredBills);
        }

        return filteredBillsByClientId;
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

    private Map<Long, List<Bill>> retrieveClientBills(long clientId)
    {
        Account account = retrieveClientAccount(clientId);
        return ImmutableMap.of(clientId, account.retrieveAcceptedBills());
    }

    private Map<Long, List<Bill>> retrieveAllClientBills()
    {
        return this.accountRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Account::getClientId, Account::retrieveAcceptedBills));
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
