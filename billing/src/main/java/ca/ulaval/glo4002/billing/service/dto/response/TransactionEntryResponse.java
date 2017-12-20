package ca.ulaval.glo4002.billing.service.dto.response;

import ca.ulaval.glo4002.billing.domain.billing.transaction.Transaction;
import ca.ulaval.glo4002.billing.service.dto.serializer.InstantDeserializer;
import ca.ulaval.glo4002.billing.service.dto.serializer.InstantSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionEntryResponse
{
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    @JsonProperty("date")
    private Instant date;
    @JsonProperty("typeTransaction")
    private String typeTransaction;
    @JsonProperty("clientId")
    private long clientId;
    @JsonProperty("typeOperation")
    private String typeOperation;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("balance")
    private BigDecimal balance;

    public TransactionEntryResponse()
    {
    }

    public static TransactionEntryResponse create(Transaction transaction)
    {
        return new TransactionEntryResponse(transaction.getDate(), transaction.getTransactionType()
                .toString(), transaction.getClientId(), transaction.getOperationType()
                .toString(), transaction.getAmount()
                .asBigDecimal(), transaction.getBalance()
                .asBigDecimal());
    }

    public TransactionEntryResponse(Instant date, String typeTransaction, long clientId, String typeOperation,
                                    BigDecimal amount, BigDecimal balance)
    {
        this.date = date;
        this.typeTransaction = typeTransaction;
        this.clientId = clientId;
        this.typeOperation = typeOperation;
        this.amount = amount;
        this.balance = balance;
    }

    public void setDate(Instant date)
    {
        this.date = date;
    }

    public void setTypeTransaction(String typeTransaction)
    {
        this.typeTransaction = typeTransaction;
    }

    public void setClientId(long clientId)
    {
        this.clientId = clientId;
    }

    public void setTypeOperation(String typeOperation)
    {
        this.typeOperation = typeOperation;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
    }

    public Instant getDate()
    {
        return date;
    }

    public String getTypeTransaction()
    {
        return typeTransaction;
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
