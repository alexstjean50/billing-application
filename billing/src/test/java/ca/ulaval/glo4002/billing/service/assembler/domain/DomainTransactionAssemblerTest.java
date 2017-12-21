package ca.ulaval.glo4002.billing.service.assembler.domain;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.transaction.OperationType;
import ca.ulaval.glo4002.billing.domain.billing.transaction.Transaction;
import ca.ulaval.glo4002.billing.domain.billing.transaction.TransactionType;
import ca.ulaval.glo4002.billing.service.repository.TransactionRepository;
import ca.ulaval.glo4002.billing.service.repository.clock.ClockRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class DomainTransactionAssemblerTest
{
    private static final long SOME_CLIENT_ID = 1L;
    private static final BigDecimal SOME_AMOUNT = BigDecimal.TEN;
    @Mock
    private ClockRepository clockRepository;
    @Mock
    private TransactionRepository transactionRepository;
    private DomainTransactionAssembler domainTransactionAssembler;

    @Before
    public void initializeDomainTransactionAssembler()
    {
        Clock fixed = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        given(this.clockRepository.retrieveCurrentTime()).willReturn(Instant.now(fixed));
        given(this.transactionRepository.retrieveCurrentLedgerBalance()).willReturn(Money.ZERO);
        this.domainTransactionAssembler = new DomainTransactionAssembler(this.clockRepository,
                this.transactionRepository);
    }

    @Test
    public void
    givenInvoiceTransactionType_whenAssemblingNewTransaction_thenShouldCreateTransactionWithCreditOperationType()
    {
        Transaction transaction =
                this.domainTransactionAssembler.toNewTransaction(SOME_CLIENT_ID, SOME_AMOUNT, TransactionType.INVOICE);

        assertEquals(transaction.getOperationType(), OperationType.CREDIT);
    }

    @Test
    public void
    givenInvoiceCancellationTransactionType_whenAssemblingNewTransaction_thenShouldCreateTransactionWithDebitOperationType()
    {
        Transaction transaction =
                this.domainTransactionAssembler.toNewTransaction(SOME_CLIENT_ID, SOME_AMOUNT,
                        TransactionType.INVOICE_CANCELLED);

        assertEquals(transaction.getOperationType(), OperationType.DEBIT);
    }

    @Test
    public void
    givenPaymentTransactionType_whenAssemblingNewTransaction_thenShouldCreateTransactionWithDebitOperationType()
    {
        Transaction transaction =
                this.domainTransactionAssembler.toNewTransaction(SOME_CLIENT_ID, SOME_AMOUNT,
                        TransactionType.PAYMENT);

        assertEquals(transaction.getOperationType(), OperationType.DEBIT);
    }
}