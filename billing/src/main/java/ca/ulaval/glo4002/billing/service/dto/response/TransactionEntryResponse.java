package ca.ulaval.glo4002.billing.service.dto.response;

import ca.ulaval.glo4002.billing.domain.billing.transaction.Transaction;
import ca.ulaval.glo4002.billing.service.dto.serializer.InstantDeserializer;
import ca.ulaval.glo4002.billing.service.dto.serializer.InstantSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionEntryResponse
{
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    private final Instant date;
    private final String transactionType;
    private final long clientId;
    private final String typeOperation;
    private final BigDecimal amount;
    private final BigDecimal balance;

    public static TransactionEntryResponse create(Transaction transaction)
    {
        return new TransactionEntryResponse(transaction.getDate(), transaction.getTransactionType()
                .toString(), transaction.getClientId(), transaction.getOperationType()
                .toString(), transaction.getAmount()
                .asBigDecimal(), transaction.getBalance()
                .asBigDecimal());
    }

    public TransactionEntryResponse(Instant date, String transactionType, long clientId, String typeOperation,
                                    BigDecimal amount, BigDecimal balance)
    {
        this.date = date;
        this.transactionType = transactionType;
        this.clientId = clientId;
        this.typeOperation = typeOperation;
        this.amount = amount;
        this.balance = balance;
    }

    public Instant getDate()
    {
        return date;
    }

    public String getTransactionType()
    {
        return transactionType;
    }

    public long getClientId()
    {
        return clientId;
    }

    public String getTypeOperation()
    {
        return typeOperation;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public BigDecimal getBalance()
    {
        return balance;
    }
}
