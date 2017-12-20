package ca.ulaval.glo4002.billing.service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TransactionsResponse
{
    @JsonProperty("accountId")
    private long accountId = 0;
    @JsonProperty("entries")
    private List<TransactionEntryResponse> entries;

    public TransactionsResponse()
    {
    }

    public TransactionsResponse(List<TransactionEntryResponse> entries)
    {
        this.entries = entries;
    }

    public void setAccountId(long accountId)
    {
        this.accountId = accountId;
    }

    public void setEntries(List<TransactionEntryResponse> entries)
    {
        this.entries = entries;
    }

    public long getAccountId()
    {
        return accountId;
    }

    public List<TransactionEntryResponse> getEntries()
    {
        return entries;
    }
}
