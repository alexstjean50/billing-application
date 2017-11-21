package ca.ulaval.glo4002.billing.service.repository;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.transaction.Transaction;

import java.util.List;

public interface TransactionRepository
{
    void save(Transaction transaction);

    List<Transaction> findAll();

    Money retrieveCurrentLedgerBalance();
}
