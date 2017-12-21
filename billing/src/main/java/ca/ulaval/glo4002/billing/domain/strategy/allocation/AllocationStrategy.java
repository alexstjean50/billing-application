package ca.ulaval.glo4002.billing.domain.strategy.allocation;

import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;

import java.util.List;

public interface AllocationStrategy
{
    void allocate(List<Bill> bills, List<Payment> payments);
}
