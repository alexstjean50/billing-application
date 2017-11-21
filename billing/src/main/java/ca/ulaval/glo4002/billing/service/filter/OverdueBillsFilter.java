package ca.ulaval.glo4002.billing.service.filter;

import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class OverdueBillsFilter implements BillsFilter
{
    @Override
    public List<Bill> filter(List<Bill> bills)
    {
        Instant now = Instant.now();
        return bills
                .stream()
                .filter(bill -> bill.isOverdue(now))
                .collect(Collectors.toList());
    }
}
