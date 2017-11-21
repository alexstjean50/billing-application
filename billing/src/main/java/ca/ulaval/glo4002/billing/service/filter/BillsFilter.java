package ca.ulaval.glo4002.billing.service.filter;

import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;

import java.util.List;

public interface BillsFilter
{
    List<Bill> filter(List<Bill> bills);
}
