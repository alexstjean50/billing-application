package ca.ulaval.glo4002.billing.domain.billing.payment;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.allocation.Allocation;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class PaymentTest
{
    private static final Identity SOME_IDENTITY = new Identity(38);
    private static final Money SOME_AMOUNT = Money.valueOf(23);
    private static final Money SOME_ALLOCATED_AMOUNT = Money.valueOf(10);
    private static final Money SOME_OTHER_ALLOCATED_AMOUNT = Money.valueOf(99);
    private static final Instant SOME_DATE = Instant.now();
    private static final long SOME_PAYMENT_NUMBER = 23;
    private static final long SOME_BILL_NUMBER = 23;
    private static final long SOME_OTHER_BILL_NUMBER = 43;
    private static final PaymentMethod SOME_PAYMENT_METHOD = mock(PaymentMethod.class);
    private Payment payment;

    @Before
    public void createPayment()
    {
        this.payment = createAPayment();
    }

    @Test
    public void givenAnIncompletePayment_whenIsCompleted_thenIsNotCompleted()
    {
        assertFalse(this.payment.isCompleted());
    }

    @Test
    public void givenAFullyAllocatedPayment_whenIsCompleted_thenIsCompleted()
    {
        Allocation aSecondAllocation = createAnotherAllocation();
        this.payment.addAllocation(aSecondAllocation);

        boolean isCompleted = this.payment.isCompleted();

        assertTrue(isCompleted);
    }

    @Test
    public void whenGettingUnallocatedBalance_thenShouldReturnAvailableAmount()
    {
        Money expectedAmount = SOME_AMOUNT.subtract(SOME_ALLOCATED_AMOUNT);

        Money balanceCalculationResult = this.payment.calculateUnallocatedBalance();

        assertEquals(expectedAmount, balanceCalculationResult);
    }

    @Test
    public void givenAllocatedPayment_whenGettingUnallocatedBalance_thenAmountShouldNotBeNegative()
    {
        this.payment.addAllocation(createAnotherAllocation());

        Money balanceCalculationResult = this.payment.calculateUnallocatedBalance();

        assertTrue(balanceCalculationResult.isGreaterOrEqualTo(Money.ZERO));
    }

    @Test
    public void
    givenAPaymentWithAllocations_whenRemoveAllocations_thenAllocationsRelatedToTheBillNumberShouldBeRemoved()
    {
        Allocation allocationWithRightBillNumber = mock(Allocation.class);
        this.payment.addAllocation(allocationWithRightBillNumber);
        given(allocationWithRightBillNumber.getBillNumber()).willReturn(SOME_BILL_NUMBER);

        this.payment.removeAllocations(SOME_BILL_NUMBER);

        assertThat(this.payment.getAllocations(), not(IsCollectionContaining.hasItem(allocationWithRightBillNumber)));
    }

    @Test
    public void
    givenAPaymentWithAllocations_whenRemoveAllocations_thenAllocationsNotRelatedToTheBillNumberShouldNotBeRemoved()
    {
        Allocation allocationWithOtherBillNumber = mock(Allocation.class);
        this.payment.addAllocation(allocationWithOtherBillNumber);
        given(allocationWithOtherBillNumber.getBillNumber()).willReturn(SOME_OTHER_BILL_NUMBER);

        this.payment.removeAllocations(SOME_BILL_NUMBER);

        assertThat(this.payment.getAllocations(), IsCollectionContaining.hasItem(allocationWithOtherBillNumber));
    }

    private Payment createAPayment()
    {
        return new Payment(SOME_IDENTITY, SOME_PAYMENT_NUMBER, SOME_AMOUNT, SOME_DATE, SOME_PAYMENT_METHOD,
                new ArrayList<>(Collections.singletonList(this.createAnAllocation())));
    }

    private Allocation createAnAllocation()
    {
        return new Allocation(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_ALLOCATED_AMOUNT, SOME_DATE);
    }

    private Allocation createAnotherAllocation()
    {
        return new Allocation(SOME_IDENTITY, SOME_OTHER_BILL_NUMBER,
                SOME_OTHER_ALLOCATED_AMOUNT, SOME_DATE);
    }
}