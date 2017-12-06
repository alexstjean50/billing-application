package ca.ulaval.glo4002.billing.domain.strategy.allocation;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.allocation.Allocation;
import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.domain.billing.bill.Item;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAllocationStrategyTest
{
    private static final Instant SOME_DATE = Instant.now();
    private static final long SOME_BILL_NUMBER = 6666;
    private static final Money SOME_AMOUNT = Money.valueOf(67);
    @Mock
    private Item item;
    @Mock
    private Identity identity;
    @Mock
    private Allocation allocation;
    @Mock
    private Bill bill;
    @Mock
    private Payment payment;
    private List<Bill> bills;
    private List<Payment> payments;
    private List<Allocation> allocations;
    private Clock clock;
    private DefaultAllocationStrategy allocationStrategy;

    @Before
    public void initializeBill() throws Exception
    {
        this.clock = Clock.fixed(SOME_DATE, ZoneId.systemDefault());

        given(this.payment.getAmount()).willReturn(SOME_AMOUNT);
        given(this.payment.getPaymentDate()).willReturn(SOME_DATE);
        given(this.payment.isCompleted()).willReturn(false);

        given(this.bill.calculateExpectedPaymentDate()).willReturn(SOME_DATE);
        given(this.bill.isAccepted()).willReturn(true);
        given(this.bill.calculateTotalItemPrice()).willReturn(SOME_AMOUNT);

        given(this.allocation.getAllocatedAmount()).willReturn(SOME_AMOUNT);

        this.payments = new ArrayList<>();
        this.payments.add(this.payment);

        this.bills = new ArrayList<>();
        this.bills.add(this.bill);

        this.allocationStrategy = new DefaultAllocationStrategy();
    }

    @Test
    public void givenASubmittalBill_whenAllocating_thenShouldNotAllocateOnBill()
    {
        given(this.bill.isAccepted()).willReturn(false);

        this.allocationStrategy.allocate(this.bills, this.payments);

        assertThat(this.bill.getAllocations(), is(Collections.emptyList()));
    }

    @Test
    public void givenAnAcceptedPaidBill_whenAllocating_thenShouldNotAllocateOnBill()
    {
        given(this.bill.isPaid()).willReturn(true);

        this.allocationStrategy.allocate(bills, payments);

        verify(this.bill, never()).addAllocation(any());
    }

    @Test
    public void givenACompletedPayment_whenAllocating_thenShouldNotAllocateOnPayment()
    {
        given(this.payment.isCompleted()).willReturn(true);

        this.allocationStrategy.allocate(bills, payments);

        verify(this.payment, never()).addAllocation(any());
    }

    @Test
    public void givenAnAcceptedUnpaidBill_whenAllocating_thenShouldAllocateOnBill()
    {
        List<Money> paymentAmounts = Collections.singletonList(SOME_AMOUNT);
        List<Payment> payments = createPayments(paymentAmounts);
        Bill bill = createAcceptedBill(SOME_AMOUNT);
        List<Bill> bills = Collections.singletonList(bill);

        this.allocationStrategy.allocate(bills, payments);

        assertThat(bill.getAllocations(), not(is(Collections.emptyList())));
    }

    @Test
    public void givenAnAcceptedUnpaidBill_whenAllocating_thenShouldAllocateOnPayment()
    {
        Payment payment = createPayment(SOME_AMOUNT);
        List<Payment> payments = Collections.singletonList(payment);
        Bill bill = createAcceptedBill(SOME_AMOUNT);
        List<Bill> bills = Collections.singletonList(bill);

        this.allocationStrategy.allocate(bills, payments);

        assertThat(payment.getAllocations(), not(is(Collections.emptyList())));
    }

    @Test
    public void givenAnAcceptedUnpaidBill_whenAllocating_thenShouldCreateAllocationWithCorrespondingBillNumber()
    {
        Payment payment = createPayment(SOME_AMOUNT);
        List<Payment> payments = Collections.singletonList(payment);
        Bill bill = createAcceptedBill(SOME_AMOUNT);
        List<Bill> bills = Collections.singletonList(bill);

        this.allocationStrategy.allocate(bills, payments);

        assertTrue(payment.getAllocations()
                .stream()
                .anyMatch(allocation -> allocation.getBillNumber() == bill.getBillNumber()));
    }

    @Test
    public void givenAcceptedUnpaidBills_whenAllocating_thenShouldAllocateOnBillWithNewerExpectedPaymentDateLast()
    {
        List<Money> paymentAmounts = Collections.singletonList(SOME_AMOUNT);
        List<Payment> payments = createPayments(paymentAmounts);
        Bill olderBill = createOlderAcceptedBill(SOME_AMOUNT);
        Bill newerBill = createAcceptedBill(SOME_AMOUNT);
        List<Bill> bills = Arrays.asList(newerBill, olderBill);

        this.allocationStrategy.allocate(bills, payments);

        assertThat(newerBill.getAllocations(), is(Collections.emptyList()));
    }

    @Test
    public void givenAcceptedUnpaidBills_whenAllocating_thenShouldAllocateOnBillWithOldestExpectedPaymentDateFirst()
    {
        List<Money> paymentAmounts = Collections.singletonList(SOME_AMOUNT);
        List<Payment> payments = createPayments(paymentAmounts);
        Bill olderBill = createOlderAcceptedBill(SOME_AMOUNT);
        Bill newerBill = createAcceptedBill(SOME_AMOUNT);
        List<Bill> bills = Arrays.asList(newerBill, olderBill);

        this.allocationStrategy.allocate(bills, payments);

        assertThat(olderBill.getAllocations(), not(is(Collections.emptyList())));
    }

    @Test
    public void givenUnallocatedPayments_whenAllocating_thenShouldAllocateNewerPaymentLast()
    {
        Payment olderPayment = createOlderPayment(SOME_AMOUNT);
        Payment newerPayment = createPayment(SOME_AMOUNT);
        List<Payment> payments = Arrays.asList(newerPayment, olderPayment);
        List<Money> billAmounts = Collections.singletonList(SOME_AMOUNT);
        List<Bill> bills = createAcceptedBills(billAmounts);

        this.allocationStrategy.allocate(bills, payments);

        assertThat(newerPayment.getAllocations(), is(Collections.emptyList()));
    }

    @Test
    public void givenUnallocatedPayments_whenAllocating_thenShouldAllocateOldestPaymentFirst()
    {
        Payment olderPayment = createOlderPayment(SOME_AMOUNT);
        Payment newerPayment = createPayment(SOME_AMOUNT);
        List<Payment> payments = Arrays.asList(newerPayment, olderPayment);
        List<Money> billAmounts = Collections.singletonList(SOME_AMOUNT);
        List<Bill> bills = createAcceptedBills(billAmounts);

        this.allocationStrategy.allocate(bills, payments);

        assertThat(olderPayment.getAllocations(), not(is(Collections.emptyList())));
    }

    @Test
    public void givenABillAndPaymentWithUnevenAmounts_whenAllocating_thenShouldAllocateMaximumPossibleAmount()
    {
        Money expectedAllocatedAmount = SOME_AMOUNT.subtract(Money.valueOf(BigDecimal.TEN));
        List<Money> paymentAmounts = Collections.singletonList(expectedAllocatedAmount);
        List<Payment> payments = createPayments(paymentAmounts);
        Bill bill = createAcceptedBill(SOME_AMOUNT);
        List<Bill> bills = Collections.singletonList(bill);

        this.allocationStrategy.allocate(bills, payments);

        assertTrue(bill.getAllocations()
                .stream()
                .anyMatch(allocation -> allocation.getAllocatedAmount()
                        .equals(expectedAllocatedAmount)));
    }

    @Test
    public void givenMultiplePaymentsWithLowerTotalAmountThanBill_whenAllocating_thenShouldAllocateAllPaymentsToBill()
    {
        List<Money> paymentAmounts = Collections.nCopies(2, SOME_AMOUNT);
        List<Payment> payments = createPayments(paymentAmounts);
        Bill bill = createAcceptedBill(SOME_AMOUNT.multiply(BigDecimal.TEN));
        List<Bill> bills = Collections.singletonList(bill);
        int expectedAllocationsCount = payments.size();

        this.allocationStrategy.allocate(bills, payments);

        assertEquals(expectedAllocationsCount, bill.getAllocations()
                .size());
    }

    @Test
    public void givenPaymentWithHigherAmountThanMultipleBills_whenAllocating_thenShouldAllocatePaymentToAllBills()
    {
        Payment payment = createPayment(SOME_AMOUNT.multiply(BigDecimal.TEN));
        List<Payment> payments = Collections.singletonList(payment);
        List<Money> billAmounts = Collections.nCopies(3, SOME_AMOUNT);
        List<Bill> bills = createAcceptedBills(billAmounts);
        int expectedAllocationsCount = bills.size();

        this.allocationStrategy.allocate(bills, payments);

        assertEquals(expectedAllocationsCount, payment.getAllocations()
                .size());
    }

    @Test
    public void givenSmallBillsWithBigPayment_whenAllocating_thenShouldHaveExceedingAmount()
    {
        Payment payment = createPayment(SOME_AMOUNT.multiply(BigDecimal.TEN));
        List<Payment> payments = Collections.singletonList(payment);
        List<Money> billAmounts = Collections.nCopies(3, SOME_AMOUNT);
        List<Bill> bills = createAcceptedBills(billAmounts);

        this.allocationStrategy.allocate(bills, payments);

        assertFalse(payment.isCompleted());
    }

    /*private List<Payment> createPayments(List<Money> amounts)
    {
        return amounts.stream()
                .map(this::createPayment)
                .collect(Collectors.toList());
    }

    private Payment createPayment(Money amount)
    {
        PaymentMethod paymentMethod = new PaymentMethod(Identity.EMPTY, SOME_BANK_ACCOUNT,
                PaymentMethodSource.CREDIT_CARD);
        return new Payment(Identity.EMPTY, SOME_PAYMENT_NUMBER, amount, SOME_DATE, paymentMethod,
                new ArrayList<>());
    }

    private Payment createOlderPayment(Money amount)
    {
        PaymentMethod paymentMethod = new PaymentMethod(Identity.EMPTY, SOME_BANK_ACCOUNT,
                PaymentMethodSource.CREDIT_CARD);
        return new Payment(Identity.EMPTY, SOME_PAYMENT_NUMBER, amount, SOME_OLDER_DATE, paymentMethod,
                new ArrayList<>());
    }

    private List<Bill> createAcceptedBills(List<Money> amounts)
    {
        return amounts.stream()
                .map(this::createAcceptedBill)
                .collect(Collectors.toList());
    }

    private Bill createAcceptedBill(Money amount)
    {
        List<Item> items = this.createItemsWithTotalEqualToBillAmount(amount);
        return Bill.create(identity, SOME_BILL_NUMBER, SOME_DATE, SOME_BILL_STATUS, SOME_DATE,
                SOME_DUE_TERM, items, new ArrayList<>());
    }

    private Bill createSubmittal(Money amount)
    {
        List<Item> items = this.createItemsWithTotalEqualToBillAmount(amount);
        return Bill.create(identity, SOME_BILL_NUMBER, SOME_DATE, SOME_OTHER_BILL_STATUS, SOME_DATE,
                SOME_DUE_TERM, items, new ArrayList<>());
    }

    private Bill createOlderAcceptedBill(Money amount)
    {
        List<Item> items = this.createItemsWithTotalEqualToBillAmount(amount);
        return Bill.create(identity, SOME_BILL_NUMBER, SOME_OLDER_DATE, SOME_BILL_STATUS, SOME_OLDER_DATE,
                SOME_DUE_TERM, items, new ArrayList<>());
    }

    private List<Item> createItemsWithTotalEqualToBillAmount(Money amount)
    {
        List<Item> items = Collections.singletonList(item);
        given(item.calculatePrice()).willReturn(amount);
        return items;
    }*/

    private Allocation createAnAllocation(Money allocatedAmount)
    {
        return new Allocation(this.identity, SOME_BILL_NUMBER, allocatedAmount, SOME_DATE);
    }
}