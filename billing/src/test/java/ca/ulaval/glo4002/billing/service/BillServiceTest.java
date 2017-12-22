package ca.ulaval.glo4002.billing.service;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.domain.billing.client.DueTerm;
import ca.ulaval.glo4002.billing.persistence.repository.account.BillNotFoundException;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainAccountAssembler;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainBillAssembler;
import ca.ulaval.glo4002.billing.service.dto.request.BillCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.ItemRequest;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.BillAcceptationResponseAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.BillCreationResponseAssembler;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.bill.BillRepository;
import ca.ulaval.glo4002.billing.service.repository.clock.ClockRepository;
import ca.ulaval.glo4002.billing.service.retriever.AccountRetriever;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BillServiceTest
{
    private static final long SOME_CLIENT_ID = 0;
    private static final Instant SOME_CREATION_DATE = Instant.parse("2017-08-21T16:59:20.142Z");
    private static final DueTerm SOME_DUE_TERM = DueTerm.IMMEDIATE;
    private static final long SOME_BILL_NUMBER = 1111;
    private static final int SOME_LIST_SIZE = 3;
    private static final Money SOME_AMOUNT = Money.valueOf(BigDecimal.TEN);
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private BillRepository billRepository;
    @Mock
    private BillCreationResponseAssembler billCreationResponseAssembler;
    @Mock
    private BillAcceptationResponseAssembler billAcceptationResponseAssembler;
    @Mock
    private DomainAccountAssembler domainAccountAssembler;
    @Mock
    private DomainBillAssembler domainBillAssembler;
    @Mock
    private Account account;
    @Mock
    private AccountRetriever accountRetriever;
    @Mock
    private ItemRequest itemRequest;
    @Mock
    private ClockRepository clockRepository;
    private Instant now;
    private BillService billService;

    @Before
    public void initializeEmptyBillServiceAndBillCreationRequest()
    {
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        this.now = Instant.now(clock);
        given(this.clockRepository.retrieveCurrentTime()).willReturn(this.now);
        this.billService = new BillService(this.accountRepository, this.clockRepository, billRepository, this
                .domainBillAssembler,
                this.billCreationResponseAssembler, this.billAcceptationResponseAssembler,
                this.accountRetriever);
    }

    @Test
    public void whenCreatingBill_thenRetrieveClientAccount()
    {
        given(this.accountRetriever.retrieveClientAccount(SOME_CLIENT_ID)).willReturn(this.account);
        BillCreationRequest billCreationRequest = createBillCreationRequest();

        this.billService.createBill(billCreationRequest);

        verify(this.accountRetriever).retrieveClientAccount(SOME_CLIENT_ID);
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

        verify(this.account).cancelBill(SOME_BILL_NUMBER, this.now);
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
        given(this.accountRepository.findByBillNumber(SOME_BILL_NUMBER)).willReturn(this.account);

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
        given(this.accountRepository.findByBillNumber(SOME_BILL_NUMBER)).willReturn(this.account);

        this.billService.acceptBill(SOME_BILL_NUMBER);

        verify(this.account).acceptBill(SOME_BILL_NUMBER, this.now);
    }

    @Test
    public void givenABillNumber_whenRetrievingRelatedClientId_thenShouldReturnRelatedBillClientId()
    {
        given(this.accountRepository.findByBillNumber(SOME_BILL_NUMBER)).willReturn(this.account);
        given(this.account.getClientId()).willReturn(SOME_CLIENT_ID);

        long clientId = this.billService.retrieveRelatedClientId(SOME_BILL_NUMBER);

        assertEquals(SOME_CLIENT_ID, clientId);
    }

    @Test
    public void givenABillNumber_whenRetrievingRelatedClientId_thenShouldGetRelatedBill()
    {
        given(this.accountRepository.findByBillNumber(SOME_BILL_NUMBER)).willReturn(this.account);

        this.billService.retrieveRelatedClientId(SOME_BILL_NUMBER);

        verify(this.accountRepository).findByBillNumber(SOME_BILL_NUMBER);
    }

    @Test
    public void givenABillNumber_whenRetrievingBillAmount_thenShouldReturnBillAmount()
    {
        given(this.billRepository.retrieveBillAmount(SOME_BILL_NUMBER)).willReturn(SOME_AMOUNT);

        Money amount = this.billService.retrieveBillAmount(SOME_BILL_NUMBER);

        assertEquals(SOME_AMOUNT, amount);
    }

    @Test
    public void givenABillNumber_whenRetrievingBillAmount_thenShouldGetRelatedBill()
    {
        given(this.billRepository.retrieveBillAmount(SOME_BILL_NUMBER)).willReturn(SOME_AMOUNT);

        this.billService.retrieveBillAmount(SOME_BILL_NUMBER);

        verify(this.billRepository).retrieveBillAmount(SOME_BILL_NUMBER);
    }

    @Test
    public void whenSuccessfullyAcceptingBill_thenAccountShouldBeSaved()
    {
        given(this.accountRepository.findByBillNumber(SOME_BILL_NUMBER)).willReturn(this.account);

        this.billService.acceptBill(SOME_BILL_NUMBER);

        verify(this.accountRepository).save(this.account);
    }

    private BillCreationRequest createBillCreationRequest()
    {
        return new BillCreationRequest(SOME_CLIENT_ID, SOME_CREATION_DATE, SOME_DUE_TERM.name(),
                Collections.nCopies(SOME_LIST_SIZE, this.itemRequest));
    }
}
