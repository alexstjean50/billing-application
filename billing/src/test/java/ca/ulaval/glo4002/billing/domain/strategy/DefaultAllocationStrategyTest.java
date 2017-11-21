package ca.ulaval.glo4002.billing.domain.strategy;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.allocation.Allocation;
import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.domain.billing.bill.BillStatus;
import ca.ulaval.glo4002.billing.domain.billing.bill.Discount;
import ca.ulaval.glo4002.billing.domain.billing.bill.Item;
import ca.ulaval.glo4002.billing.domain.billing.client.DueTerm;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;
import ca.ulaval.glo4002.billing.domain.billing.payment.PaymentMethod;
import ca.ulaval.glo4002.billing.domain.billing.payment.PaymentMethodSource;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class DefaultAllocationStrategyTest
{
    private static final DueTerm SOME_DUE_TERM = DueTerm.DAYS30;
    private static final BillStatus SOME_BILL_STATUS = BillStatus.ACCEPTED;
    private static final BillStatus SOME_OTHER_BILL_STATUS = BillStatus.CANCELLED;
    private static final List<Discount> SOME_DISCOUNTS = new ArrayList<>();
    private static final Item SOME_ITEM = mock(Item.class);
    private static final Identity SOME_IDENTITY = mock(Identity.class);
    private static final Instant SOME_DATE = Instant.now();
    private static final Instant SOME_OLDER_DATE = Instant.EPOCH;
    private static final long SOME_PAYMENT_NUMBER = 55;
    private static final long SOME_BILL_NUMBER = 6666;
    private static final Money SOME_AMOUNT = Money.valueOf(67);
    private static final String SOME_BANK_ACCOUNT = "666-666-6666";

    @Test
    public void givenASubmittalBill_whenAllocating_thenShouldNotAllocateOnBill()
    {
        List<Money> paymentAmounts = Collections.singletonList(SOME_AMOUNT);
        List<Payment> payments = createPayments(paymentAmounts);
        Bill submittal = createSubmittal(SOME_AMOUNT);
        List<Bill> bills = Collections.singletonList(submittal);
        DefaultAllocationStrategy allocationStrategy = new DefaultAllocationStrategy();

        allocationStrategy.allocate(bills, payments);

        assertThat(submittal.getAllocations(), is(Collections.emptyList()));
    }

    @Test
    public void givenAnAcceptedPaidBill_whenAllocating_thenShouldNotAllocateOnBill()
    {
        List<Money> paymentAmounts = Collections.singletonList(SOME_AMOUNT);
        List<Payment> payments = createPayments(paymentAmounts);
        Bill bill = createAcceptedBill(SOME_AMOUNT);
        bill.addAllocation(createAnAllocation(SOME_AMOUNT));
        List<Allocation> expectedBillAllocations = new ArrayList<>(bill.getAllocations());
        List<Bill> bills = Collections.singletonList(bill);
        DefaultAllocationStrategy allocationStrategy = new DefaultAllocationStrategy();

        allocationStrategy.allocate(bills, payments);

        assertEquals(expectedBillAllocations, bill.getAllocations());
    }

    @Test
    public void givenACompletedPayment_whenAllocating_thenShouldNotAllocateOnPayment()
    {
        Payment payment = createPayment(SOME_AMOUNT);
        List<Payment> payments = Collections.singletonList(payment);
        payment.addAllocation(createAnAllocation(SOME_AMOUNT));
        Bill bill = createAcceptedBill(SOME_AMOUNT);
        List<Allocation> expectedPaymentAllocations = new ArrayList<>(payment.getAllocations());
        List<Bill> bills = Collections.singletonList(bill);
        DefaultAllocationStrategy allocationStrategy = new DefaultAllocationStrategy();

        allocationStrategy.allocate(bills, payments);

        assertEquals(expectedPaymentAllocations, payment.getAllocations());
    }

    @Test
    public void givenAnAcceptedUnpaidBill_whenAllocating_thenShouldAllocateOnBill()
    {
        List<Money> paymentAmounts = Collections.singletonList(SOME_AMOUNT);
        List<Payment> payments = createPayments(paymentAmounts);
        Bill bill = createAcceptedBill(SOME_AMOUNT);
        List<Bill> bills = Collections.singletonList(bill);
        DefaultAllocationStrategy allocationStrategy = new DefaultAllocationStrategy();

        allocationStrategy.allocate(bills, payments);

        assertThat(bill.getAllocations(), not(is(Collections.emptyList())));
    }

    @Test
    public void givenAnAcceptedUnpaidBill_whenAllocating_thenShouldAllocateOnPayment()
    {
        Payment payment = createPayment(SOME_AMOUNT);
        List<Payment> payments = Collections.singletonList(payment);
        Bill bill = createAcceptedBill(SOME_AMOUNT);
        List<Bill> bills = Collections.singletonList(bill);
        DefaultAllocationStrategy allocationStrategy = new DefaultAllocationStrategy();

        allocationStrategy.allocate(bills, payments);

        assertThat(payment.getAllocations(), not(is(Collections.emptyList())));
    }

    @Test
    public void givenAnAcceptedUnpaidBill_whenAllocating_thenShouldCreateAllocationWithCorrespondingBillNumber()
    {
        Payment payment = createPayment(SOME_AMOUNT);
        List<Payment> payments = Collections.singletonList(payment);
        Bill bill = createAcceptedBill(SOME_AMOUNT);
        List<Bill> bills = Collections.singletonList(bill);
        DefaultAllocationStrategy allocationStrategy = new DefaultAllocationStrategy();

        allocationStrategy.allocate(bills, payments);

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
        DefaultAllocationStrategy allocationStrategy = new DefaultAllocationStrategy();

        allocationStrategy.allocate(bills, payments);

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
        DefaultAllocationStrategy allocationStrategy = new DefaultAllocationStrategy();

        allocationStrategy.allocate(bills, payments);

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
        DefaultAllocationStrategy allocationStrategy = new DefaultAllocationStrategy();

        allocationStrategy.allocate(bills, payments);

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
        DefaultAllocationStrategy allocationStrategy = new DefaultAllocationStrategy();

        allocationStrategy.allocate(bills, payments);

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
        DefaultAllocationStrategy allocationStrategy = new DefaultAllocationStrategy();

        allocationStrategy.allocate(bills, payments);

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
        DefaultAllocationStrategy allocationStrategy = new DefaultAllocationStrategy();
        int expectedAllocationsCount = payments.size();

        allocationStrategy.allocate(bills, payments);

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
        DefaultAllocationStrategy allocationStrategy = new DefaultAllocationStrategy();
        int expectedAllocationsCount = bills.size();

        allocationStrategy.allocate(bills, payments);

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
        DefaultAllocationStrategy allocationStrategy = new DefaultAllocationStrategy();

        allocationStrategy.allocate(bills, payments);

        assertFalse(payment.isCompleted());
    }

    private List<Payment> createPayments(List<Money> amounts)
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
        return Bill.create(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_DATE, SOME_BILL_STATUS, SOME_DATE,
                SOME_DUE_TERM, items, SOME_DISCOUNTS, new ArrayList<>());
    }

    private Bill createSubmittal(Money amount)
    {
        List<Item> items = this.createItemsWithTotalEqualToBillAmount(amount);
        return Bill.create(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_DATE, SOME_OTHER_BILL_STATUS, SOME_DATE,
                SOME_DUE_TERM, items, SOME_DISCOUNTS, new ArrayList<>());
    }

    private Bill createOlderAcceptedBill(Money amount)
    {
        List<Item> items = this.createItemsWithTotalEqualToBillAmount(amount);
        return Bill.create(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_OLDER_DATE, SOME_BILL_STATUS, SOME_OLDER_DATE,
                SOME_DUE_TERM, items, SOME_DISCOUNTS, new ArrayList<>());
    }

    private List<Item> createItemsWithTotalEqualToBillAmount(Money amount)
    {
        List<Item> items = Collections.singletonList(SOME_ITEM);
        given(SOME_ITEM.calculatePrice()).willReturn(amount);
        return items;
    }

    private Allocation createAnAllocation(Money allocatedAmount)
    {
        return new Allocation(SOME_IDENTITY, SOME_BILL_NUMBER, allocatedAmount, SOME_DATE);
    }
}