package ca.ulaval.glo4002.billing.domain.strategy;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.allocation.Allocation;
import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultAllocationStrategy implements AllocationStrategy
{
    @Override
    public void allocate(List<Bill> bills, List<Payment> payments)
    {
        List<Bill> billsToAllocate = this.retrieveAllocatableBills(bills);
        List<Payment> paymentsToAllocate = this.retrieveAllocatablePayments(payments);

        Collections.sort(paymentsToAllocate);
        Collections.sort(billsToAllocate);

        billsToAllocate.forEach(bill -> this.allocateAvailablePaymentsOnBill(bill, paymentsToAllocate));
    }

    private List<Bill> retrieveAllocatableBills(List<Bill> bills)
    {
        return bills.stream()
                .filter(Bill::isAllocatable)
                .collect(Collectors.toList());
    }

    private List<Payment> retrieveAllocatablePayments(List<Payment> payments)
    {
        return payments.stream()
                .filter(payment -> !payment.isCompleted())
                .collect(Collectors.toList());
    }

    private void allocateAvailablePaymentsOnBill(Bill bill, List<Payment> payments)
    {
        Optional<Payment> nextPayment = this.retrieveNextAvailablePayment(payments);

        while (!bill.isPaid() && nextPayment.isPresent())
        {
            this.allocateOnBill(bill, nextPayment.get());
            nextPayment = this.retrieveNextAvailablePayment(payments);
        }
    }

    private Optional<Payment> retrieveNextAvailablePayment(List<Payment> payments)
    {
        return payments.stream()
                .filter(payment -> !payment.isCompleted())
                .findFirst();
    }

    private void allocateOnBill(Bill bill, Payment payment)
    {
        Money allocatedAmount = this.retrieveMaximumPossibleAllocationAmount(bill, payment);

        Allocation allocation = new Allocation(Identity.EMPTY, bill.getBillNumber(),
                allocatedAmount, Instant.now());

        bill.addAllocation(allocation);
        payment.addAllocation(allocation);
    }

    private Money retrieveMaximumPossibleAllocationAmount(Bill bill, Payment payment)
    {
        return payment.calculateUnallocatedBalance()
                .min(bill.calculateUnpaidBalance());
    }
}
