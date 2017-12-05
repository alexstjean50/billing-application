package ca.ulaval.glo4002.billing.domain.billing.bill;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.allocation.Allocation;
import ca.ulaval.glo4002.billing.domain.billing.client.DueTerm;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class BillTest
{
    private static final long SOME_BILL_NUMBER = 42;
    private static final DueTerm SOME_DUE_TERM = DueTerm.DAYS30;
    private static final Money SOME_TOTAL = Money.valueOf(42);
    private static final Money SOME_OTHER_TOTAL = Money.valueOf(15);
    private static final Money SOME_ALLOCATED_AMOUNT = Money.valueOf(10);
    private static final Money SOME_OTHER_ALLOCATED_AMOUNT = Money.valueOf(99);
    private static final Identity SOME_IDENTITY = mock(Identity.class);
    private static final Instant SOME_DATE = Instant.now();
    private static final Instant SOME_OVERDUE_CREATION_DATE = Instant.now()
            .minusSeconds(50);
    private static final Instant SOME_OVERDUE_EFFECTIVE_DATE = Instant.now()
            .minusSeconds(20);

    @Test
    public void givenAnEmptyBill_whenCalculatingTotal_thenTotalShouldBe0()
    {
        Bill bill = this.createEmptyBill();

        Money billSubTotal = bill.calculateTotalItemPrice();

        assertEquals(Money.ZERO, billSubTotal);
    }

    @Test
    public void whenAcceptingABill_thenBillStatusShouldBeAccepted()
    {
        Bill bill = createEmptyBill();

        bill.accept(SOME_DATE);

        assertEquals(BillStatus.ACCEPTED, bill.getStatus());
    }

    @Test
    public void whenAcceptingABill_thenEffectiveDateShouldBeTheArgumentDate()
    {
        Bill bill = createEmptyBill();

        bill.accept(SOME_DATE);

        assertEquals(SOME_DATE, bill.getEffectiveDate());
    }

    @Test(expected = BillNotYetAcceptedException.class)
    public void
    givenAnUnacceptedBill_whenCalculatingExpectedPaymentDate_thenShouldThrowABillNotYetAcceptedException()
    {
        Bill bill = createEmptyBill();

        bill.calculateExpectedPaymentDate();
    }

    @Test
    public void givenAnOverdueUnpaidAcceptedBill_whenCheckingIsOverdue_theShouldBeTrue()
    {
        Bill overdueBill = createOverdueAcceptedBill(createItems());

        assertTrue(overdueBill.isOverdue(Instant.now()));
    }

    @Test
    public void givenAnOverduePaidAcceptedBill_whenCheckingIsOverdue_theShouldBeFalse()
    {
        Bill bill = createEmptyBill();

        assertFalse(bill.isOverdue(Instant.now()));
    }

    @Test
    public void givenANonAcceptedBill_whenCheckingIsOverdue_thenShouldBeFalse()
    {
        Bill bill = createEmptyBill();

        assertFalse(bill.isOverdue(Instant.now()));
    }

    @Test
    public void givenAnNonOverdueUnpaidAcceptedBill_whenCheckingIsOverdue_theShouldBeFalse()
    {
        Bill acceptedBill = createAcceptedBillWithItems(createItems());

        assertFalse(acceptedBill.isOverdue(Instant.now()));
    }

    @Test
    public void givenAnNonOverduePaidAcceptedBill_whenCheckingIsOverdue_theShouldBeFalse()
    {
        Bill acceptedBill = createAcceptedBillWithItems(new ArrayList<>());

        assertFalse(acceptedBill.isOverdue(Instant.now()));
    }

    @Test
    public void givenAnAcceptedBill_whenCalculatingExpectedPaymentDate_thenShouldBeTheAcceptedDatePlusDueTerm()
    {
        Bill bill = createAcceptedBill();

        Instant billPaymentDate = bill.calculateExpectedPaymentDate();

        Instant expectedPaymentDate = SOME_DATE.plus(Duration.ofDays(bill.getDueTerm().daysToPay));

        assertEquals(expectedPaymentDate, billPaymentDate);
    }

    @Test(expected = BillAlreadyAcceptedException.class)
    public void givenAnAcceptedBill_whenAcceptingTheBill_thenShouldThrowABillAlreadyAcceptedException()
    {
        Bill bill = createAcceptedBill();

        bill.accept(SOME_DATE);
    }

    @Test
    public void givenASubmittal_whenIsAllocatable_thenShouldReturnFalse()
    {
        List<Item> items = this.createItems();
        Bill bill = this.createSubmittalWithItems(items);

        assertFalse(bill.isAllocatable());
    }

    @Test
    public void givenAPaidBill_whenIsAllocatable_thenShouldReturnFalse()
    {
        List<Item> items = createItems();
        Bill bill = createAcceptedBillWithItems(items);

        bill.addAllocation(createAnAllocation());
        bill.addAllocation(createAnotherAllocation());

        assertFalse(bill.isAllocatable());
    }

    @Test
    public void givenAnAcceptedUnpaidBill_whenIsAllocatable_thenShouldReturnTrue()
    {
        List<Item> items = createItems();
        Bill bill = createAcceptedBillWithItems(items);

        bill.addAllocation(createAnAllocation());

        assertTrue(bill.isAllocatable());
    }

    @Test
    public void givenAllocationsCoveringBillAmount_whenIsPaid_thenBillIsPaid()
    {
        List<Item> items = createItems();
        Bill bill = createAcceptedBillWithItems(items);

        bill.addAllocation(createAnAllocation());
        bill.addAllocation(createAnotherAllocation());

        assertTrue(bill.isPaid());
    }

    @Test
    public void givenAllocationsNotCoveringBillAmount_whenIsPaid_thenBillIsNotPaid()
    {
        List<Item> items = createItems();
        Bill bill = createAcceptedBillWithItems(items);

        bill.addAllocation(createAnAllocation());

        assertFalse(bill.isPaid());
    }

    @Test
    public void givenAnAcceptedBill_whenCalculatingUnpaidBalance_thenShouldReturnAvailableAmount()
    {
        List<Item> items = createItems();
        Bill bill = createAcceptedBillWithItems(items);

        bill.addAllocation(createAnAllocation());
        Money unpaidBalance = bill.calculateUnpaidBalance();

        Money expectedUnpaidBalance = bill.calculateTotalItemPrice()
                .subtract(SOME_ALLOCATED_AMOUNT);

        assertEquals(expectedUnpaidBalance, unpaidBalance);
    }

    @Test
    public void whenCancelled_thenStatusShouldChangeToCancelled()
    {
        Bill bill = createEmptyBill();

        bill.cancel();

        assertEquals(BillStatus.CANCELLED, bill.getStatus());
    }

    @Test
    public void whenCancelled_thenBillShouldNotHaveAllocations()
    {
        Bill bill = createEmptyBill();

        bill.cancel();

        assertThat(bill.getAllocations(), is(Collections.emptyList()));
    }

    private Bill createEmptyBill()
    {
        return Bill.create(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_DATE, SOME_DUE_TERM, new ArrayList<>());
    }

    private Bill createAcceptedBill()
    {
        Bill bill = Bill.create(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_DATE, SOME_DUE_TERM, new ArrayList<>());
        bill.accept(SOME_DATE);
        return bill;
    }

    private Bill createOverdueAcceptedBill(List<Item> items)
    {
        return Bill.create(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_OVERDUE_CREATION_DATE, BillStatus.ACCEPTED,
                SOME_OVERDUE_EFFECTIVE_DATE, DueTerm.IMMEDIATE, items, new ArrayList<>());
    }

    private Bill createAcceptedBillWithItems(List<Item> items)
    {
        return Bill.create(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_DATE, BillStatus.ACCEPTED, SOME_DATE,
                SOME_DUE_TERM, items, new ArrayList<>());
    }

    private Bill createSubmittalWithItems(List<Item> items)
    {
        return Bill.create(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_DATE, BillStatus.SUBMITTAL, SOME_DATE,
                SOME_DUE_TERM, items, new ArrayList<>());
    }

    private Allocation createAnAllocation()
    {
        return new Allocation(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_ALLOCATED_AMOUNT, SOME_DATE);
    }

    private Allocation createAnotherAllocation()
    {
        return new Allocation(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_OTHER_ALLOCATED_AMOUNT,
                SOME_DATE);
    }

    private List<Item> createItems()
    {
        Item item = mock(Item.class);
        Item otherItem = mock(Item.class);
        List<Item> items = Arrays.asList(item, otherItem);
        given(item.calculatePrice()).willReturn(SOME_TOTAL);
        given(otherItem.calculatePrice()).willReturn(SOME_OTHER_TOTAL);
        return items;
    }
}
