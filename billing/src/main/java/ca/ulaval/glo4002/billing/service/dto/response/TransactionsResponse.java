package ca.ulaval.glo4002.billing.service.dto.response;

import java.util.List;

public class TransactionsResponse
{
    private final long accountId = 0;
    private final List<TransactionEntryResponse> entries;

    public TransactionsResponse(List<TransactionEntryResponse> entries)
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
