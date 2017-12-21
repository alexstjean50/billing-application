package ca.ulaval.glo4002.billing.domain.billing.bill;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.allocation.Allocation;
import ca.ulaval.glo4002.billing.domain.billing.client.DueTerm;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Bill implements Comparable<Bill>
{
    private static final BillStatus DEFAULT_BILL_STATUS = BillStatus.SUBMITTAL;
    private final Identity billId;
    private final long billNumber;
    private final Instant creationDate;
    private final DueTerm dueTerm;
    private final List<Item> items;
    private final List<Allocation> allocations;
    private BillStatus status;
    private Instant effectiveDate;

    private Bill(Identity billId, long billNumber, Instant creationDate, BillStatus billStatus, Instant effectiveDate,
                 DueTerm dueTerm, List<Item> items,
                 List<Allocation> allocations)
    {
        this.billId = billId;
        this.billNumber = billNumber;
        this.creationDate = creationDate;
        this.dueTerm = dueTerm;
        this.status = billStatus;
        this.effectiveDate = effectiveDate;
        this.items = items;
        this.allocations = allocations;
    }

    public static Bill create(long billNumber, Instant creationDate, DueTerm dueTerm, List<Item> items)
    {
        return new Bill(Identity.EMPTY, billNumber, creationDate, DEFAULT_BILL_STATUS, null, dueTerm, items,
                new ArrayList<>());
    }

    public static Bill create(Identity billId, long billNumber, Instant creationDate, DueTerm dueTerm, List<Item> items)
    {
        return new Bill(billId, billNumber, creationDate, DEFAULT_BILL_STATUS, null, dueTerm, items, new ArrayList<>());
    }

    public static Bill create(Identity billId, long billNumber, Instant creationDate, BillStatus billStatus,
                              Instant effectiveDate, DueTerm dueTerm, List<Item> items,
                              List<Allocation> allocations)
    {
        return new Bill(billId, billNumber, creationDate, billStatus, effectiveDate, dueTerm, items,
                allocations);
    }

    public void accept(Instant acceptedDate)
    {
        if (this.status != BillStatus.SUBMITTAL)
        {
            throw new BillAlreadyAcceptedException("This bill has already been accepted.");
        }
        this.status = BillStatus.ACCEPTED;
        this.effectiveDate = acceptedDate;
    }

    public boolean isOverdue(Instant now)
    {
        return isAllocatable() &&
                calculateExpectedPaymentDate()
                        .isBefore(now);
    }

    public boolean isAllocatable()
    {
        return isAccepted() && !isPaid();
    }

    public Instant calculateExpectedPaymentDate()
    {
        if (!isAccepted())
        {
            throw new BillNotYetAcceptedException("No expected payment, because the bill isn't " +
                    "accepted yet.");
        }
        return this.effectiveDate.plus(this.dueTerm.asDays());
    }

    public boolean isAccepted()
    {
        return this.status == BillStatus.ACCEPTED;
    }

    public boolean isPaid()
    {
        Money total = calculateTotalItemPrice();
        return calculatePaidAmount().isGreaterOrEqualTo(total) && isAccepted();
    }

    private Money calculatePaidAmount()
    {
        return this.allocations.stream()
                .map(Allocation::getAllocatedAmount)
                .reduce(Money::add)
                .orElse(Money.ZERO);
    }

    public Money calculateTotalItemPrice()
    {
        return this.items.stream()
                .map(Item::calculatePrice)
                .reduce(Money::add)
                .orElse(Money.ZERO);
    }

    public void cancel()
    {
        this.allocations.clear();
        this.status = BillStatus.CANCELLED;
    }

    public Money calculateUnpaidBalance()
    {
        return calculateTotalItemPrice().subtract(calculatePaidAmount());
    }

    public void addAllocation(Allocation allocation)
    {
        this.allocations.add(allocation);
    }

    public boolean isEqualBillNumber(long billNumber)
    {
        return this.billNumber == billNumber;
    }

    @Override
    public int compareTo(Bill that)
    {
        if (calculateExpectedPaymentDate().equals(that.calculateExpectedPaymentDate()))
        {
            return 0;
        }
        else if (calculateExpectedPaymentDate().isBefore(that.calculateExpectedPaymentDate()))
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }

    public long getBillNumber()
    {
        return this.billNumber;
    }

    public BillStatus getStatus()
    {
        return this.status;
    }

    public Instant getEffectiveDate()
    {
        return this.effectiveDate;
    }

    public DueTerm getDueTerm()
    {
        return this.dueTerm;
    }

    public List<Item> getItems()
    {
        return this.items;
    }

    public List<Allocation> getAllocations()
    {
        return this.allocations;
    }

    public Identity getBillId()
    {
        return this.billId;
    }

    public Instant getCreationDate()
    {
        return this.creationDate;
    }
}