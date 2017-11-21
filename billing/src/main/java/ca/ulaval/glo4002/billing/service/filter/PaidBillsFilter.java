package ca.ulaval.glo4002.billing.service.filter;

import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;

import java.util.List;
import java.util.stream.Collectors;

public class PaidBillsFilter implements BillsFilter
{
    @Override
    public List<Bill> filter(List<Bill> bills)
    {
        return bills
                .stream()
                .filter(Bill::isPaid)
                .collect(Collectors.toList());
    }
}
