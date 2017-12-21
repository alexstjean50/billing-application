package ca.ulaval.glo4002.billing.domain.billing.transaction;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;

import java.time.Instant;

public class Transaction
{
    private final Identity transactionId;
    private final Instant date;
    private final TransactionType transactionType;
    private final long clientId;
    private final OperationType operationType;
    private final Money amount;
    private final Money balance;

    public Transaction(Identity transactionId, Instant date, TransactionType transactionType, long clientId,
                       OperationType operationType, Money amount, Money balance)
    {
        this.transactionId = transactionId;
        this.date = date;
        this.transactionType = transactionType;
        this.clientId = clientId;
        this.operationType = operationType;
        this.amount = amount;
        this.balance = balance;
    }

    public Identity getTransactionId()
    {
        return this.transactionId;
    }

    public Instant getDate()
    {
        return this.date;
    }

    public TransactionType getTransactionType()
    {
        return this.transactionType;
    }

    public long getClientId()
    {
        return this.clientId;
    }

    public OperationType getOperationType()
    {
        return this.operationType;
    }

    public Money getAmount()
    {
        return this.amount;
    }

    public Money getBalance()
    {
        return this.balance;
    }
}
