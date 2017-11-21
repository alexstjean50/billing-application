package ca.ulaval.glo4002.billing.service.filter;

import ca.ulaval.glo4002.billing.service.dto.request.BillStatusParameter;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class BillsFilterFactoryTest
{
    private BillsFilterFactory billsFilterFactory;

    @Before
    public void createBillsFilterFactory()
    {
        this.billsFilterFactory = new BillsFilterFactory();
    }

    @Test
    public void givenOverdueBillStatusParameter_whenCreate_thenShouldReturnOverdueBillsFilter()
    {
        assertThat(this.billsFilterFactory.create(BillStatusParameter.OVERDUE), instanceOf(OverdueBillsFilter.class));
    }

    @Test
    public void givenPaidBillStatusParameter_whenCreate_thenShouldReturnPaidBillsFilter()
    {
        assertThat(this.billsFilterFactory.create(BillStatusParameter.PAID), instanceOf(PaidBillsFilter.class));
    }

    @Test
    public void givenUnpaidBillStatusParameter_whenCreate_thenShouldReturnPaidBillsFilter()
    {
        assertThat(this.billsFilterFactory.create(BillStatusParameter.UNPAID), instanceOf(UnpaidBillsFilter.class));
    }
}