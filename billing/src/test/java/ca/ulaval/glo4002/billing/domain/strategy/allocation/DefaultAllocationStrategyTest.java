package ca.ulaval.glo4002.billing.domain.strategy.allocation;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.domain.billing.bill.Item;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultAllocationStrategyTest
{
    private static final Money SOME_AMOUNT = Money.valueOf(67);
    @Mock
    private Item item;
    @Mock
    private Identity identity;
    @Mock
    private Bill bill;
    @Mock
    private Bill anotherBill;
    @Mock
    private Payment payment;
    @Mock
    private Payment anotherPayment;
    private Instant someDate;
    private Instant someOlderDate;
    private List<Bill> bills;
    private List<Payment> payments;
    private Clock clock;
    private DefaultAllocationStrategy allocationStrategy;

    @Before
    public void initializeTests() throws Exception
    {
        this.clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        this.someDate = Instant.now(this.clock);
        this.someOlderDate = this.someDate.minusSeconds(100);

        initializePayment(this.payment);
        initializePayment(this.anotherPayment);

        initializeBill(this.bill);
        initializeBill(this.anotherBill);

        this.payments = Arrays.asList(this.payment, this.anotherPayment);

        this.bills = Arrays.asList(this.bill, this.anotherBill);

        this.allocationStrategy = new DefaultAllocationStrategy();
    }

    private void initializePayment(Payment payment)
    {
        given(payment.getPaymentDate()).willReturn(this.someDate);
        when(payment.compareTo(any(Payment.class))).thenCallRealMethod();
        given(payment.calculateUnallocatedBalance()).willReturn(SOME_AMOUNT);
        given(payment.isCompleted()).willReturn(false);
    }

    private void initializeBill(Bill bill)
    {
        when(bill.compareTo(any(Bill.class))).thenCallRealMethod();
        given(bill.calculateExpectedPaymentDate()).willReturn(this.someDate);
        given(bill.calculateUnpaidBalance()).willReturn(SOME_AMOUNT);
        given(bill.isAllocatable()).willReturn(true, false);
        given(bill.isPaid()).willReturn(false, true);
    }

    @Test
    public void givenAnUnallocatableBill_whenAllocating_thenShouldNotAllocateOnBill()
    {
        given(this.bill.isAllocatable()).willReturn(false);

        this.allocationStrategy.allocate(this.bills, this.payments);

        verify(this.bill, never()).addAllocation(any());
    }

    @Test
    public void givenACompletedPayment_whenAllocating_thenShouldNotAllocateOnPayment()
    {
        given(this.payment.isCompleted()).willReturn(true);

        this.allocationStrategy.allocate(this.bills, this.payments);

        verify(this.payment, never()).addAllocation(any());
    }

    @Test
    public void givenAnAllocatableBill_whenAllocating_thenShouldAllocateOnBill()
    {
        given(this.bill.isAllocatable()).willReturn(true, false);

        this.allocationStrategy.allocate(this.bills, this.payments);

        verify(this.bill).addAllocation(any());
    }

    @Test
    public void givenAnAllocatableBill_whenAllocating_thenShouldAllocateOnPayment()
    {
        given(this.bill.isAllocatable()).willReturn(true, false);

        this.allocationStrategy.allocate(this.bills, this.payments);

        verify(this.payment, times(this.bills.size())).addAllocation(any());
    }

    @Test
    public void givenAcceptedUnpaidBills_whenAllocating_thenShouldAllocateOnBillWithNewerExpectedPaymentDateLast()
    {
        given(this.anotherBill.calculateExpectedPaymentDate()).willReturn(this.someOlderDate);

        this.allocationStrategy.allocate(this.bills, this.payments);

        InOrder inOrder = inOrder(this.anotherBill, this.bill);

        inOrder.verify(this.anotherBill)
                .addAllocation(any());
        inOrder.verify(this.bill)
                .addAllocation(any());
    }

    @Test
    public void givenUnallocatedPayments_whenAllocating_thenShouldAllocateOlderPaymentFirst()
    {
        given(this.anotherPayment.getPaymentDate()).willReturn(this.someOlderDate);
        given(this.anotherPayment.isCompleted()).willReturn(false, false, true);

        this.allocationStrategy.allocate(this.bills, this.payments);

        InOrder inOrder = inOrder(this.anotherPayment, this.payment);

        inOrder.verify(this.anotherPayment)
                .addAllocation(any());
        inOrder.verify(this.payment)
                .addAllocation(any());
    }
}