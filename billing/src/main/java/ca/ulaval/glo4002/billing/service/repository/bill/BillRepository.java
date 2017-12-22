package ca.ulaval.glo4002.billing.service.repository.bill;

import ca.ulaval.glo4002.billing.domain.Money;

public interface BillRepository
{
    long retrieveNextBillNumber();

    Money retrieveBillAmount(long billNumber);
}
