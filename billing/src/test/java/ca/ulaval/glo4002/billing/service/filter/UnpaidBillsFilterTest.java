package ca.ulaval.glo4002.billing.service.filter;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.domain.billing.bill.BillStatus;
import ca.ulaval.glo4002.billing.domain.billing.bill.Item;
import ca.ulaval.glo4002.billing.domain.billing.client.DueTerm;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class UnpaidBillsFilterTest
{
    private static final long SOME_BILL_NUMBER = 42;
    private static final DueTerm SOME_DUE_TERM = DueTerm.DAYS30;
    private static final BillStatus SOME_BILL_STATUS = BillStatus.ACCEPTED;
    private static final Item SOME_ITEM = mock(Item.class);
    private static final Item SOME_OTHER_ITEM = mock(Item.class);
    private static final Money SOME_TOTAL = Money.valueOf(42);
    private static final Money SOME_OTHER_TOTAL = Money.valueOf(15);
    private static final Identity SOME_IDENTITY = mock(Identity.class);
    private static final Instant SOME_DATE = Instant.now();
    private static final Instant SOME_OVERDUE_CREATION_DATE = Instant.now()
            .minusSeconds(50);
    private static final Instant SOME_OVERDUE_EFFECTIVE_DATE = Instant.now()
            .minusSeconds(20);
    private static final int UNPAID_BILLS_COUNT = 2;
    private final List<Bill> billsToFilter = new ArrayList<>();
    private final UnpaidBillsFilter unpaidBillsFilter = new UnpaidBillsFilter();

    @Before
    public void initializeBills()
    {
        Bill paidBill = createAcceptedBillWithItems(new ArrayList<>());
        Bill unpaidBill = createAcceptedBillWithItems(createItems());
        Bill overdueBill = createOverdueAcceptedBill(createItems());

        this.billsToFilter.add(paidBill);
        this.billsToFilter.add(unpaidBill);
        this.billsToFilter.add(overdueBill);
    }

    @Test
    public void whenFilteringUnpaidBills_thenShouldOnlyHaveUnpaidBills()
    {
        List<Bill> filteredBills = this.unpaidBillsFilter.filter(this.billsToFilter);

        List<Bill> paidBills = filteredBills.stream()
                .filter(Bill::isPaid)
                .collect(Collectors.toList());

        assertTrue(paidBills.isEmpty());
    }

    @Test
    public void whenFilteringUnpaidBills_thenShouldKeepAllUnpaidBills()
    {
        List<Bill> filteredBills = this.unpaidBillsFilter.filter(this.billsToFilter);

        List<Bill> unpaidBills = filteredBills.stream()
                .filter(bill -> (!bill.isPaid()))
                .collect(Collectors.toList());

        assertEquals(UNPAID_BILLS_COUNT, unpaidBills.size());
    }

    private Bill createOverdueAcceptedBill(List<Item> items)
    {
        return Bill.create(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_OVERDUE_CREATION_DATE, SOME_BILL_STATUS,
                SOME_OVERDUE_EFFECTIVE_DATE,
                DueTerm.IMMEDIATE, items, new ArrayList<>(), new ArrayList<>());
    }

    private Bill createAcceptedBillWithItems(List<Item> items)
    {
        return Bill.create(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_DATE, SOME_BILL_STATUS, SOME_DATE,
                SOME_DUE_TERM, items, new ArrayList<>(), new ArrayList<>());
    }

    private List<Item> createItems()
    {
        List<Item> items = Arrays.asList(SOME_ITEM, SOME_OTHER_ITEM);
        given(SOME_ITEM.calculatePrice()).willReturn(SOME_TOTAL);
        given(SOME_OTHER_ITEM.calculatePrice()).willReturn(SOME_OTHER_TOTAL);
        return items;
    }
}