package ca.ulaval.glo4002.billing.persistence.assembler.transaction;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.transaction.OperationType;
import ca.ulaval.glo4002.billing.domain.billing.transaction.Transaction;
import ca.ulaval.glo4002.billing.domain.billing.transaction.TransactionType;
import ca.ulaval.glo4002.billing.persistence.entity.TransactionEntity;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertEquals;

public class TransactionAssemblerTest
{
    private static final long SOME_ID = 0;
    private static final Identity SOME_IDENTITY = new Identity(SOME_ID);
    private static final Instant SOME_DATE = Instant.now();
    private static final long SOME_CLIENT_ID = 0;
    private static final Money SOME_AMOUNT = Money.valueOf(10);
    private static final Money SOME_BALANCE = Money.valueOf(15);
    private TransactionAssembler transactionAssembler;

    @Before
    public void initializeTransactionAssembler()
    {
        transactionAssembler = new TransactionAssembler();
    }

    @Test
    public void givenSomeTransaction_whenConvertedToTransactionEntity_thenTransactionEntityShouldHaveSameId()
    {
        Transaction transaction = createATransaction();

        TransactionEntity result = this.transactionAssembler.toPersistenceModel(transaction);

        assertEquals(SOME_CLIENT_ID, result.getClientId());
    }

    @Test
    public void givenSomeTransactionEntity_whenConvertedToTransaction_thenTransactionShouldHaveSameId()
    {
        TransactionEntity transactionEntity = createATransactionEntity();

        Transaction result = this.transactionAssembler.toDomainModel(transactionEntity);

        assertEquals(SOME_CLIENT_ID, result.getClientId());
    }

    @Test
    public void givenSomeTransaction_whenConvertedToTransactionEntity_thenTransactionEntityShouldHaveSameDate()
    {
        Transaction transaction = createATransaction();

        TransactionEntity result = this.transactionAssembler.toPersistenceModel(transaction);

        assertEquals(SOME_DATE, result.getDate());
    }

    @Test
    public void givenSomeTransactionEntity_whenConvertedToTransaction_thenTransactionShouldHaveSameDate()
    {
        TransactionEntity transactionEntity = createATransactionEntity();

        Transaction result = this.transactionAssembler.toDomainModel(transactionEntity);

        assertEquals(SOME_DATE, result.getDate());
    }

    @Test
    public void
    givenSomeTransaction_whenConvertedToTransactionEntity_thenTransactionEntityShouldHaveSameTransactionType()
    {
        Transaction transaction = createATransaction();

        TransactionEntity result = this.transactionAssembler.toPersistenceModel(transaction);

        assertEquals(transaction.getTransactionType(), result.getTransactionType());
    }

    @Test
    public void givenSomeTransactionEntity_whenConvertedToTransaction_thenTransactionShouldHaveSameTransactionType()
    {
        TransactionEntity transactionEntity = createATransactionEntity();

        Transaction result = this.transactionAssembler.toDomainModel(transactionEntity);

        assertEquals(transactionEntity.getTransactionType(), result.getTransactionType());
    }

    @Test
    public void givenSomeTransaction_whenConvertedToTransactionEntity_thenTransactionEntityShouldHaveSameOperationType()
    {
        Transaction transaction = createATransaction();

        TransactionEntity result = this.transactionAssembler.toPersistenceModel(transaction);

        assertEquals(transaction.getTransactionType(), result.getTransactionType());
    }

    @Test
    public void givenSomeTransactionEntity_whenConvertedToTransaction_thenTransactionShouldHaveSameOperationType()
    {
        TransactionEntity transactionEntity = createATransactionEntity();

        Transaction result = this.transactionAssembler.toDomainModel(transactionEntity);

        assertEquals(transactionEntity.getTransactionType(), result.getTransactionType());
    }

    @Test
    public void givenSomeTransaction_whenConvertedToTransactionEntity_thenTransactionEntityShouldHaveSameAmount()
    {
        Transaction transaction = createATransaction();

        TransactionEntity result = this.transactionAssembler.toPersistenceModel(transaction);

        assertEquals(transaction.getAmount(), Money.valueOf(result.getAmount()));
    }

    @Test
    public void givenSomeTransactionEntity_whenConvertedToTransaction_thenTransactionShouldHaveSameAmount()
    {
        TransactionEntity transactionEntity = createATransactionEntity();

        Transaction result = this.transactionAssembler.toDomainModel(transactionEntity);

        assertEquals(transactionEntity.getAmount(), result.getAmount()
                .asBigDecimal());
    }

    @Test
    public void givenSomeTransaction_whenConvertedToTransactionEntity_thenTransactionEntityShouldHaveSameBalance()
    {
        Transaction transaction = createATransaction();

        TransactionEntity result = this.transactionAssembler.toPersistenceModel(transaction);

        assertEquals(transaction.getBalance(), Money.valueOf(result.getBalance()));
    }

    @Test
    public void givenSomeTransactionEntity_whenConvertedToTransaction_thenTransactionShouldHaveSameBalance()
    {
        TransactionEntity transactionEntity = createATransactionEntity();

        Transaction result = this.transactionAssembler.toDomainModel(transactionEntity);

        assertEquals(transactionEntity.getBalance(), result.getBalance()
                .asBigDecimal());
    }

    private Transaction createATransaction()
    {
        return new Transaction(SOME_IDENTITY, SOME_DATE, TransactionType.INVOICE, SOME_CLIENT_ID,
                OperationType.CREDIT, SOME_AMOUNT, SOME_BALANCE);
    }

    private TransactionEntity createATransactionEntity()
    {
        return new TransactionEntity(SOME_ID, SOME_DATE, TransactionType.INVOICE, SOME_CLIENT_ID,
                OperationType.CREDIT, SOME_AMOUNT.asBigDecimal(), SOME_BALANCE.asBigDecimal());
    }
}