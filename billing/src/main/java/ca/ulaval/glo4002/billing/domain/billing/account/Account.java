package ca.ulaval.glo4002.billing.domain.billing.account;

import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.domain.billing.bill.Discount;
import ca.ulaval.glo4002.billing.domain.billing.bill.Item;
import ca.ulaval.glo4002.billing.domain.billing.client.Client;
import ca.ulaval.glo4002.billing.domain.billing.client.DueTerm;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;
import ca.ulaval.glo4002.billing.domain.strategy.AllocationStrategy;
import ca.ulaval.glo4002.billing.domain.strategy.DefaultAllocationStrategy;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import ca.ulaval.glo4002.billing.persistence.repository.account.BillNotFoundException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Account
{
    private final Identity accountId;
    private final Client client;
    private final List<Payment> payments;
    private final List<Bill> bills;
    private final AllocationStrategy allocationStrategy;

    private Account(Identity accountId, Client client, AllocationStrategy allocationStrategy, List<Payment> payments,
                    List<Bill> bills)
    {
        this.client = client;
        this.accountId = accountId;
        this.payments = payments;
        this.bills = bills;
        this.allocationStrategy = allocationStrategy;
    }

    public static Account create(Client client)
    {
        return new Account(Identity.EMPTY, client, new DefaultAllocationStrategy(), new ArrayList<>(),
                new ArrayList<>());
    }

    public static Account create(Identity identity, Client client, AllocationStrategy allocationStrategy)
    {
        return new Account(identity, client, allocationStrategy, new ArrayList<>(), new ArrayList<>());
    }

    public static Account create(Identity identity, Client client, AllocationStrategy allocationStrategy,
                                 List<Payment> payments, List<Bill> bills)
    {
        return new Account(identity, client, allocationStrategy, payments, bills);
    }

    public Bill createBill(long billNumber, Instant creationDate, Optional<DueTerm> dueTerm, List<Item> items)
    {
        DueTerm appliedDueTerm = dueTerm.orElseGet(this.client::getDefaultTerm);
        Bill bill = Bill.create(billNumber, creationDate, appliedDueTerm, items);
        this.bills.add(bill);

        return bill;
    }

    public void cancelBill(long billNumber)
    {
        Bill bill = findBillByNumber(billNumber);
        bill.cancel();
        this.payments.forEach(payment -> payment.removeAllocations(billNumber));
        allocate();
    }

    public Bill findBillByNumber(long billNumber)
    {
        return this.bills.stream()
                .filter(bill -> bill.isEqualBillNumber(billNumber))
                .findFirst()
                .orElseThrow(() -> new DomainAccountBillNotFoundException("A bill can't be found in an account."));
    }

    public BigDecimal retrieveBillAmount(long billNumber)
    {
        return findBillByNumber(billNumber)
                .calculateSubTotal()
                .asBigDecimal();
    }

    public void addPayment(Payment payment)
    {
        this.payments.add(payment);
    }

    public void acceptBill(long billNumber, Instant acceptedDate)
    {
        Bill billAccepted = this.findBillByNumber(billNumber);
        billAccepted.accept(acceptedDate);
        allocate();
    }

    public void applyDiscount(long billNumber, Discount discount)
    {
        Bill bill = this.findBillByNumber(billNumber);

        if (!bill.isAccepted())
        {
            throw new BillNotFoundException("A bill can't be found in an account.", String.valueOf(billNumber));
        }

        bill.addDiscount(discount);
        bill.removeAllAllocations();
        this.payments.forEach(payment -> payment.removeAllocations(billNumber));
        allocate();
    }

    public boolean isSaved()
    {
        return accountId != Identity.EMPTY;
    }

    public void allocate()
    {
        this.allocationStrategy.allocate(this.bills, this.payments);
    }

    public List<Bill> retrieveAcceptedBills()
    {
        return this.bills.stream()
                .filter(Bill::isAccepted)
                .collect(Collectors.toList());
    }

    public List<Payment> getPayments()
    {
        return this.payments;
    }

    public List<Bill> getBills()
    {
        return this.bills;
    }

    public Identity getAccountId()
    {
        return this.accountId;
    }

    public long getClientId()
    {
        return this.client.getId();
    }

    public Client getClient()
    {
        return this.client;
    }
}
