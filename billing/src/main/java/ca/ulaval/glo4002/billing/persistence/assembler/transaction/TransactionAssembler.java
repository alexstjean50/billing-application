package ca.ulaval.glo4002.billing.persistence.assembler.transaction;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.transaction.Transaction;
import ca.ulaval.glo4002.billing.persistence.entity.TransactionEntity;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;

public class TransactionAssembler
{
    public Transaction toDomainModel(TransactionEntity transactionEntity)
    {
        return new Transaction(new Identity(transactionEntity.getTransactionId()), transactionEntity.getDate(),
                transactionEntity.getTransactionType(), transactionEntity.getClientId(),
                transactionEntity.getOperationType(), Money.valueOf(transactionEntity.getAmount()),
                Money.valueOf(transactionEntity.getBalance()));
    }

    public TransactionEntity toPersistenceModel(Transaction transaction)
    {
        return new TransactionEntity(transaction.getTransactionId()
                .getId(), transaction.getDate(), transaction.getTransactionType(), transaction.getClientId(),
                transaction.getOperationType(), transaction.getAmount()
                .asBigDecimal(), transaction.getBalance()
                .asBigDecimal());
    }
}
