package ca.ulaval.glo4002.billing.service.filter;

import ca.ulaval.glo4002.billing.service.dto.request.BillStatusParameter;
import org.apache.commons.lang3.NotImplementedException;

public class BillsFilterFactory
{
    public BillsFilter create(BillStatusParameter status)
    {
        switch (status)
        {
            case PAID:
                return new PaidBillsFilter();
            case UNPAID:
                return new UnpaidBillsFilter();
            case OVERDUE:
                return new OverdueBillsFilter();
            default:
                throw new NotImplementedException(String.format("There is no filter strategy implemented yet for %s" +
                        ".", status.toString()));
        }
    }
}
