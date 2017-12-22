package ca.ulaval.glo4002.billing.service;

import ca.ulaval.glo4002.billing.domain.billing.transaction.Transaction;
import ca.ulaval.glo4002.billing.domain.billing.transaction.TransactionType;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainTransactionAssembler;
import ca.ulaval.glo4002.billing.service.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest
{
    private static final long SOME_CLIENT_ID = 1L;
    private static final BigDecimal SOME_AMOUNT = BigDecimal.TEN;
    private static final TransactionType SOME_TRANSACTION_TYPE = TransactionType.INVOICE;
    private static final Optional<String> SOME_MONTH = Optional.of("2");
    private static final Optional<Integer> SOME_YEAR = Optional.of(2017);
    @Mock
    private DomainTransactionAssembler domainTransactionAssembler;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private Transaction transaction;
    private TransactionService transactionService;

    @Before
    public void initializeTransactionService()
    {
        this.transactionService = new TransactionService(this.transactionRepository, this.domainTransactionAssembler);
    }

    @Test
    public void whenLoggingTransaction_thenShouldCreateNewTransaction()
    {
        this.transactionService.logTransaction(SOME_CLIENT_ID, SOME_AMOUNT, SOME_TRANSACTION_TYPE);

        verify(this.domainTransactionAssembler).toNewTransaction(SOME_CLIENT_ID, SOME_AMOUNT, SOME_TRANSACTION_TYPE);
    }

    @Test
    public void whenLoggingTransaction_thenShouldSaveNewTransaction()
    {
        given(this.domainTransactionAssembler.toNewTransaction(SOME_CLIENT_ID, SOME_AMOUNT, SOME_TRANSACTION_TYPE))
                .willReturn(this.transaction);

        this.transactionService.logTransaction(SOME_CLIENT_ID, SOME_AMOUNT, SOME_TRANSACTION_TYPE);

        verify(this.transactionRepository).save(this.transaction);
    }

    @Test
    public void whenRetrievingTransactions_thenShouldFindTransactionsByFilter()
    {
        this.transactionService.retrieveTransactions(SOME_MONTH, SOME_MONTH, SOME_YEAR);

        verify(this.transactionRepository).findByFilter(SOME_MONTH, SOME_MONTH, SOME_YEAR);
    }
}