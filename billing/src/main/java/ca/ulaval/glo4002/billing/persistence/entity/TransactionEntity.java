package ca.ulaval.glo4002.billing.persistence.entity;

import ca.ulaval.glo4002.billing.domain.billing.transaction.OperationType;
import ca.ulaval.glo4002.billing.domain.billing.transaction.TransactionType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "TRANSACTION")
public class TransactionEntity implements Serializable
{
    private static final long serialVersionUID = -8947380822310069731L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TRANSACTION_ID")
    private long transactionId;
    @Column
    private Instant date;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Column
    private long clientId;
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    @Column
    private BigDecimal amount;
    @Column(name = "BALANCE")
    private BigDecimal balance;

    public TransactionEntity()
    {
    }

    public TransactionEntity(long transactionId, Instant date, TransactionType transactionType, long clientId,
                             OperationType
                                     operationType, BigDecimal amount, BigDecimal balance)
    {
        this.transactionId = transactionId;
        this.date = date;
        this.transactionType = transactionType;
        this.clientId = clientId;
        this.operationType = operationType;
        this.amount = amount;
        this.balance = balance;
    }

    public long getTransactionId()
    {
        return this.transactionId;
    }

    public void setTransactionId(long transactionId)
    {
        this.transactionId = transactionId;
    }

    public Instant getDate()
    {
        return this.date;
    }

    public void setDate(Instant date)
    {
        this.date = date;
    }

    public TransactionType getTransactionType()
    {
        return this.transactionType;
    }

    public void setTransactionType(TransactionType transactionType)
    {
        this.transactionType = transactionType;
    }

    public long getClientId()
    {
        return this.clientId;
    }

    public void setClientId(long clientId)
    {
        this.clientId = clientId;
    }

    public OperationType getOperationType()
    {
        return this.operationType;
    }

    public void setOperationType(OperationType operationType)
    {
        this.operationType = operationType;
    }

    public BigDecimal getAmount()
    {
        return this.amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public BigDecimal getBalance()
    {
        return this.balance;
    }

    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
    }
}
