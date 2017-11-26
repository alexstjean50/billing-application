package ca.ulaval.glo4002.billing.domain.billing.account;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.domain.billing.bill.Discount;
import ca.ulaval.glo4002.billing.domain.billing.client.Client;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;
import ca.ulaval.glo4002.billing.domain.strategy.allocation.AllocationStrategy;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import ca.ulaval.glo4002.billing.persistence.repository.account.BillNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountTest
{
    private static final Payment SOME_PAYMENT = mock(Payment.class);
    private static final Payment SOME_OTHER_PAYMENT = mock(Payment.class);
    private static final Instant SOME_DATE = Instant.EPOCH;
    private static final long SOME_BILL_NUMBER = 42;
    private static final Money SOME_DISCOUNT_AMOUNT = Money.valueOf(4.2);
    private static final String SOME_DESCRIPTION = "TheFallen";
    @Mock
    private Bill bill;
    @Mock
    private AllocationStrategy allocationStrategy;

    @Test
    public void whenCreatingABill_thenBillShouldBeAddedToAccount()
    {
        Account account = createEmptyAccount();

        given(this.bill.isEqualBillNumber(SOME_BILL_NUMBER)).willReturn(true);

        account.addBill(this.bill);
        Bill bill = account.findBillByNumber(SOME_BILL_NUMBER);

        assertTrue(account.getBills()
                .contains(bill));
    }

    @Test
    public void givenAnAccountWithABill_whenAcceptingTheBill_thenTheBillShouldBeAccepted()
    {
        given(this.bill.isEqualBillNumber(SOME_BILL_NUMBER)).willReturn(true);
        Account account = createAccountWithABill(this.bill);

        account.acceptBill(SOME_BILL_NUMBER, SOME_DATE);

        verify(this.bill).accept(SOME_DATE);
    }

    @Test
    public void givenAnAccountWithABill_whenAcceptingTheBill_thenShouldCallAllocationStrategy()
    {
        given(this.bill.isEqualBillNumber(SOME_BILL_NUMBER)).willReturn(true);
        Account account = createAccountWithABill(this.bill);

        account.acceptBill(SOME_BILL_NUMBER, SOME_DATE);

        verify(this.allocationStrategy).allocate(any(), any());
    }

    @Test
    public void givenAnAccountWithBills_whenAcceptingABill_thenOtherBillsAreNotWronglyAccepted()
    {
        Bill billToAccept = mock(Bill.class);
        given(billToAccept.isEqualBillNumber(SOME_BILL_NUMBER)).willReturn(true);
        Bill billNotAccepted = mock(Bill.class);
        given(billNotAccepted.isEqualBillNumber(SOME_BILL_NUMBER)).willReturn(false);
        Account account = createAccountWithManyBills(Arrays.asList(billNotAccepted, billToAccept));

        account.acceptBill(SOME_BILL_NUMBER, SOME_DATE);

        verify(billNotAccepted, never()).accept(any());
    }

    @Test
    public void givenAnAccountWithBills_whenRetrievingAcceptedBills_thenShouldOnlyReturnAcceptedBills()
    {
        Bill acceptedBill = mock(Bill.class);
        given(acceptedBill.isAccepted()).willReturn(true);
        Bill cancelledBill = mock(Bill.class);
        given(cancelledBill.isAccepted()).willReturn(false);
        Bill submittal = mock(Bill.class);
        given(submittal.isAccepted()).willReturn(false);
        Account account = createAccountWithManyBills(Arrays.asList(cancelledBill, acceptedBill, submittal));

        List<Bill> retrievedBills = account.retrieveAcceptedBills();

        assertEquals(Collections.singletonList(acceptedBill), retrievedBills);
    }

    @Test
    public void givenAnAccountWithABill_whenFindingTheBill_thenShouldReturnTheSameBill()
    {
        given(this.bill.isEqualBillNumber(SOME_BILL_NUMBER)).willReturn(true);
        Account account = createAccountWithABill(this.bill);

        Bill billFound = account.findBillByNumber(SOME_BILL_NUMBER);

        assertEquals(this.bill, billFound);
    }

    @Test
    public void givenAnAccountWithABill_whenCancellingTheBill_thenPaymentsShouldRemoveAllocationsRelatedToBill()
    {
        given(this.bill.isEqualBillNumber(SOME_BILL_NUMBER)).willReturn(true);
        given(this.bill.isAccepted()).willReturn(true);
        Account account = createAccountWithPayments(this.bill, Collections.singletonList(SOME_PAYMENT));

        account.cancelBill(SOME_BILL_NUMBER);

        account.getPayments()
                .forEach(payment -> verify(payment).removeAllocations(SOME_BILL_NUMBER));
    }

    @Test
    public void givenAnAccountWithABill_whenCancellingTheBill_thenBillShouldBeCanceled()
    {
        given(this.bill.isEqualBillNumber(SOME_BILL_NUMBER)).willReturn(true);
        given(this.bill.isAccepted()).willReturn(true);
        Account account = createAccountWithABill(this.bill);

        account.cancelBill(SOME_BILL_NUMBER);

        verify(this.bill).cancel();
    }

    @Test
    public void givenAnAccountWithABill_whenCancellingTheBill_thenShouldReallocate()
    {
        given(this.bill.isEqualBillNumber(SOME_BILL_NUMBER)).willReturn(true);
        given(this.bill.isAccepted()).willReturn(true);
        Account account = createAccountWithABill(this.bill);

        account.cancelBill(SOME_BILL_NUMBER);

        verify(this.allocationStrategy).allocate(account.getBills(), account.getPayments());
    }

    @Test(expected = DomainAccountBillNotFoundException.class)
    public void whenTryingToFindAnNonExistingBill_thenShouldThrowAnException()
    {
        Account account = createEmptyAccount();

        account.findBillByNumber(SOME_BILL_NUMBER);
    }

    @Test
    public void givenAnAccountWithABill_whenApplyingDiscountOnTheBill_thenDiscountShouldBeApplied()
    {
        given(this.bill.isEqualBillNumber(SOME_BILL_NUMBER)).willReturn(true);
        given(this.bill.isAccepted()).willReturn(true);
        Account account = createAccountWithABill(this.bill);

        Discount discountToApply = Discount.create(SOME_DISCOUNT_AMOUNT, SOME_DESCRIPTION);
        account.applyDiscount(SOME_BILL_NUMBER, discountToApply);

        verify(this.bill).addDiscount(discountToApply);
    }

    @Test(expected = BillNotFoundException.class)
    public void
    givenAnAccountWithABillThatIsNotAccepted_whenApplyingDiscountOnTheBill_thenShouldThrowDomainAccountBillNotFoundException()
    {
        given(this.bill.isEqualBillNumber(SOME_BILL_NUMBER)).willReturn(true);
        given(this.bill.isAccepted()).willReturn(false);
        Account account = createAccountWithABill(this.bill);

        Discount discountToApply = Discount.create(SOME_DISCOUNT_AMOUNT, SOME_DESCRIPTION);
        account.applyDiscount(SOME_BILL_NUMBER, discountToApply);
    }

    @Test
    public void
    givenAnAccountWithABillAndPayments_whenApplyingDiscountOnTheBill_thenRelatedPaymentsShouldUnallocate()
    {
        given(this.bill.isEqualBillNumber(SOME_BILL_NUMBER)).willReturn(true);
        given(this.bill.isAccepted()).willReturn(true);
        Account account = createAccountWithPayments(this.bill, Collections.singletonList(SOME_OTHER_PAYMENT));

        Discount discountToApply = Discount.create(SOME_DISCOUNT_AMOUNT, SOME_DESCRIPTION);
        account.applyDiscount(SOME_BILL_NUMBER, discountToApply);

        account.getPayments()
                .forEach(payment -> verify(payment).removeAllocations(SOME_BILL_NUMBER));
    }

    @Test
    public void givenAnAccountWithABill_whenApplyingDiscountOnTheBill_thenShouldReallocate()
    {
        given(this.bill.isEqualBillNumber(SOME_BILL_NUMBER)).willReturn(true);
        given(this.bill.isAccepted()).willReturn(true);
        Account account = createAccountWithABill(this.bill);

        Discount discountToApply = Discount.create(SOME_DISCOUNT_AMOUNT, SOME_DESCRIPTION);
        account.applyDiscount(SOME_BILL_NUMBER, discountToApply);

        verify(this.allocationStrategy).allocate(account.getBills(), account.getPayments());
    }

    private Account createEmptyAccount()
    {
        return Account.create(mock(Identity.class), mock(Client.class), this.allocationStrategy);
    }

    private Account createAccountWithABill(Bill bill)
    {
        return Account.create(mock(Identity.class), mock(Client.class), this.allocationStrategy,
                new ArrayList<>(), Collections.singletonList(bill));
    }

    private Account createAccountWithManyBills(List<Bill> bills)
    {
        return Account.create(mock(Identity.class), mock(Client.class), this.allocationStrategy,
                new ArrayList<>(), bills);
    }

    private Account createAccountWithPayments(Bill bill, List<Payment> payments)
    {
        return Account.create(mock(Identity.class), mock(Client.class), this.allocationStrategy, payments,
                Collections.singletonList(bill));
    }
}