package ca.ulaval.glo4002.billing.service.repository;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.transaction.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository
{
    void save(Transaction transaction);

    List<Transaction> findByFilter(Optional<String> optionalStartMonth, Optional<String> optionalEndMonth, Optional
            <Long> optionalYear);

    Money retrieveCurrentLedgerBalance();
}
