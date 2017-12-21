package ca.ulaval.glo4002.billing.domain.billing.payment;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.allocation.Allocation;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class Payment implements Comparable<Payment>
{
    private final Identity paymentId;
    private final long paymentNumber;
    private final Money amount;
    private final Instant paymentDate;
    private final PaymentMethod paymentMethod;
    private List<Allocation> allocations;

    public Payment(Identity paymentId, long paymentNumber, Money amount, Instant paymentDate,
                   PaymentMethod paymentMethod, List<Allocation> allocations)
    {
        this.paymentId = paymentId;
        this.paymentNumber = paymentNumber;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.allocations = allocations;
    }

    public Money calculateUnallocatedBalance()
    {
        Money unallocatedAmount = this.amount.subtract(this.calculateAllocatedAmount());
        return unallocatedAmount.max(Money.ZERO);
    }

    private Money calculateAllocatedAmount()
    {
        return this.allocations.stream()
                .map(Allocation::getAllocatedAmount)
                .reduce(Money::add)
                .orElse(Money.ZERO);
    }

    public void addAllocation(Allocation allocation)
    {
        this.allocations.add(allocation);
    }

    public void removeAllocations(long billNumber)
    {
        this.allocations = this.allocations.stream()
                .filter(allocation -> allocation.getBillNumber() != billNumber)
                .collect(Collectors.toList());
    }

    @Override
    public int compareTo(Payment that)
    {
        return this.getPaymentDate()
                .isBefore(that.getPaymentDate()) ? -1 : 1;
    }

    public Instant getPaymentDate()
    {
        return this.paymentDate;
    }

    public boolean isCompleted()
    {
        return this.calculateAllocatedAmount()
                .isGreaterOrEqualTo(this.amount);
    }

    public List<Allocation> getAllocations()
    {
        return this.allocations;
    }

    public long getPaymentNumber()
    {
        return this.paymentNumber;
    }

    public Identity getPaymentId()
    {
        return this.paymentId;
    }

    public PaymentMethod getPaymentMethod()
    {
        return this.paymentMethod;
    }

    public Money getAmount()
    {
        return this.amount;
    }
}
