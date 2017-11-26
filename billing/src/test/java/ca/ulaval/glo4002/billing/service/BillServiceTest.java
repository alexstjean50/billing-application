package ca.ulaval.glo4002.billing.service;

import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.domain.billing.account.AccountFactory;
import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.domain.billing.bill.BillStatus;
import ca.ulaval.glo4002.billing.domain.billing.bill.Discount;
import ca.ulaval.glo4002.billing.domain.billing.client.DueTerm;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import ca.ulaval.glo4002.billing.persistence.repository.account.BillNotFoundException;
import ca.ulaval.glo4002.billing.service.dto.request.BillCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.DiscountApplicationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.ItemRequest;
import ca.ulaval.glo4002.billing.service.dto.request.assembler.ItemRequestAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.BillAcceptationResponseAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.BillCreationResponseAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.BillsByClientIdResponseAssembler;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.bill.BillRepository;
import ca.ulaval.glo4002.billing.service.repository.product.ProductRepository;
import ca.ulaval.glo4002.billing.service.retriever.AccountRetriever;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BillServiceTest
{
    private static final long SOME_CLIENT_ID = 0;
    private static final Instant SOME_CREATION_DATE = Instant.parse("2017-08-21T16:59:20.142Z");
    private static final long SOME_PRODUCT_ID = 42;
    private static final DueTerm SOME_DUE_TERM = DueTerm.IMMEDIATE;
    private static final long SOME_BILL_NUMBER = 1111;
    private static final BigDecimal SOME_DISCOUNT_AMOUNT = BigDecimal.valueOf(7.42);
    private static final String SOME_DESCRIPTION = "Blackout";
    private static final int SOME_LIST_SIZE = 3;
    private static final BillStatus SOME_BILL_STATUS = BillStatus.ACCEPTED;
    private static final Identity SOME_IDENTITY = mock(Identity.class);
    private static final Instant SOME_DATE = Instant.now();
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private BillRepository billRepository;
    @Mock
    private ItemRequestAssembler itemRequestAssembler;
    @Mock
    private BillCreationResponseAssembler billCreationResponseAssembler;
    @Mock
    private BillAcceptationResponseAssembler billAcceptationResponseAssembler;
    @Mock
    private BillsByClientIdResponseAssembler billsByClientIdResponseAssembler;
    @Mock
    private AccountFactory accountFactory;
    @Mock
    private Account account;
    @Mock
    private ItemRequest itemRequest;
    @Mock
    private AccountRetriever accountRetriever;
    private BillService billService;

    @Before
    public void initializeEmptyBillServiceAndBillCreationRequest()
    {
        this.billService = new BillService(this.accountRepository,
                this.billRepository, this.itemRequestAssembler, this.billCreationResponseAssembler,
                this.billAcceptationResponseAssembler, this.billsByClientIdResponseAssembler, this.accountRetriever);
    }

    @Test
    public void whenCreatingBill_thenRetrieveClientAccount()
    {
        this.setSuccessfulBehaviorsForCreationRequest();
        BillCreationRequest billCreationRequest = createBillCreationRequest();

        this.billService.createBill(billCreationRequest);

        verify(this.accountRetriever).retrieveClientAccount(SOME_CLIENT_ID);
    }

    @Test
    public void whenCreatingBill_thenValidateItems()
    {
        this.setSuccessfulBehaviorsForCreationRequest();
        BillCreationRequest billCreationRequest = createBillCreationRequest();
        given(this.itemRequest.getProductId()).willReturn(SOME_PRODUCT_ID);

        this.billService.createBill(billCreationRequest);

        verify(this.productRepository).findAll(Collections.nCopies(billCreationRequest.itemRequests.size(),
                SOME_PRODUCT_ID));
    }

    @Test
    public void whenCreatingBill_thenAllItemRequestsAreConvertedToItems()
    {
        this.setSuccessfulBehaviorsForCreationRequest();
        BillCreationRequest billCreationRequest = createBillCreationRequest();
        this.billService.createBill(billCreationRequest);

        verify(this.itemRequestAssembler, times(1)).toDomainModel(anyList());
    }

    @Test
    public void whenCancellingBill_thenFindAccountByBillNumber()
    {
        given(this.accountRepository.findByBillNumber(SOME_BILL_NUMBER)).willReturn(this.account);

        this.billService.cancelBill(SOME_BILL_NUMBER);

        verify(this.accountRepository).findByBillNumber(SOME_BILL_NUMBER);
    }

    @Test
    public void whenSuccessfullyCancellingBill_thenAccountCancelBill()
    {
        given(this.accountRepository.findByBillNumber(SOME_BILL_NUMBER)).willReturn(this.account);

        this.billService.cancelBill(SOME_BILL_NUMBER);

        verify(this.account).cancelBill(SOME_BILL_NUMBER);
    }

    @Test
    public void whenSuccessfullyCancellingBill_thenSaveAccount()
    {
        given(this.accountRepository.findByBillNumber(SOME_BILL_NUMBER)).willReturn(this.account);

        this.billService.cancelBill(SOME_BILL_NUMBER);

        verify(this.accountRepository).save(this.account);
    }

    @Test
    public void whenAcceptingBill_thenShouldRetrieveAccount()
    {
        setSuccessfulBehaviorsForAcceptationRequest();
        this.billService.acceptBill(SOME_BILL_NUMBER);

        verify(this.accountRepository).findByBillNumber(SOME_BILL_NUMBER);
    }

    @Test(expected = BillNotFoundException.class)
    public void givenANonExistingBill_whenAcceptingBill_thenShouldThrowABillNotFoundException()
    {
        given(this.accountRepository.findByBillNumber(SOME_BILL_NUMBER)).willThrow(BillNotFoundException.class);

        this.billService.acceptBill(SOME_BILL_NUMBER);
    }

    @Test
    public void whenAcceptingBill_thenBillShouldBeAccept()
    {
        setSuccessfulBehaviorsForAcceptationRequest();

        this.billService.acceptBill(SOME_BILL_NUMBER);

        verify(this.account).acceptBill(eq(SOME_BILL_NUMBER), any());
    }

    @Test
    public void whenSuccessfullyAcceptingBill_thenAccountShouldBeSaved()
    {
        setSuccessfulBehaviorsForAcceptationRequest();

        this.billService.acceptBill(SOME_BILL_NUMBER);

        verify(this.accountRepository).save(this.account);
    }

    @Test
    public void whenApplyingDiscount_thenShouldRetrieveAccount()
    {
        given(this.accountRepository.findByBillNumber(SOME_BILL_NUMBER)).willReturn(this.account);
        DiscountApplicationRequest discountApplicationRequest = createValidDiscountApplicationRequest();
        this.billService.applyDiscount(SOME_BILL_NUMBER, discountApplicationRequest);

        verify(this.accountRepository).findByBillNumber(SOME_BILL_NUMBER);
    }

    @Test(expected = BillNotFoundException.class)
    public void givenANonExistingBill_whenApplyingDiscount_thenShouldThrowABillNotFoundException()
    {
        given(this.accountRepository.findByBillNumber(SOME_BILL_NUMBER)).willThrow(BillNotFoundException.class);
        DiscountApplicationRequest discountApplicationRequest = createValidDiscountApplicationRequest();

        this.billService.applyDiscount(SOME_BILL_NUMBER, discountApplicationRequest);
    }

    @Test
    public void whenApplyingDiscount_thenAccountShouldApplyDiscount()
    {
        given(this.accountRepository.findByBillNumber(SOME_BILL_NUMBER)).willReturn(this.account);
        DiscountApplicationRequest discountApplicationRequest = createValidDiscountApplicationRequest();

        this.billService.applyDiscount(SOME_BILL_NUMBER, discountApplicationRequest);

        verify(this.account).applyDiscount(eq(SOME_BILL_NUMBER),
                argThat(discount -> hasTheSameDiscountInfo(discountApplicationRequest, discount)));
    }

    @Test
    public void whenApplyingDiscount_thenAccountShouldBeSaved()
    {
        given(this.accountRepository.findByBillNumber(SOME_BILL_NUMBER)).willReturn(this.account);
        DiscountApplicationRequest discountApplicationRequest = createValidDiscountApplicationRequest();

        this.billService.applyDiscount(SOME_BILL_NUMBER, discountApplicationRequest);

        verify(this.accountRepository).save(this.account);
    }

    @Test
    public void givenSomeClientId_whenGetBills_thenRetrieveAcceptedBillsFromAccountOfClientId()
    {
        this.billService.retrieveBills(Optional.of(SOME_CLIENT_ID), Optional.empty());

        verify(this.accountRepository).retrieveFilteredBillsOfClients(Optional.of(SOME_CLIENT_ID), Optional.empty());
    }

    private BillCreationRequest createBillCreationRequest()
    {
        return BillCreationRequest.create(SOME_CLIENT_ID, SOME_CREATION_DATE, SOME_DUE_TERM.name(),
                Collections.nCopies(SOME_LIST_SIZE, this.itemRequest));
    }

    private DiscountApplicationRequest createValidDiscountApplicationRequest()
    {
        return DiscountApplicationRequest.create(SOME_DISCOUNT_AMOUNT, SOME_DESCRIPTION);
    }

    private void setSuccessfulBehaviorsForCreationRequest()
    {
        given(this.accountRetriever.retrieveClientAccount(SOME_CLIENT_ID)).willReturn(this.account);
    }

    private void setSuccessfulBehaviorsForAcceptationRequest()
    {
        given(this.accountRepository.findByBillNumber(SOME_BILL_NUMBER)).willReturn(this.account);
        given(this.account.findBillByNumber(SOME_BILL_NUMBER)).willReturn(this.createEmptyBill());
    }

    private boolean hasTheSameDiscountInfo(DiscountApplicationRequest discountApplicationRequest, Discount discount)
    {
        return discount.getAmount()
                .equals(discountApplicationRequest.amount)
                && discount.getDescription()
                .equals(discountApplicationRequest.description);
    }

    private Bill createEmptyBill()
    {
        return Bill.create(SOME_IDENTITY, SOME_BILL_NUMBER, SOME_DATE, SOME_BILL_STATUS,
                SOME_DATE, DueTerm.IMMEDIATE, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }
}
