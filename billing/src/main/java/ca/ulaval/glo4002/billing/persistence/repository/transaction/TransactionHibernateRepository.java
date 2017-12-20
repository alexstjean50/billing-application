package ca.ulaval.glo4002.billing.persistence.repository.transaction;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.transaction.Transaction;
import ca.ulaval.glo4002.billing.persistence.assembler.transaction.TransactionAssembler;
import ca.ulaval.glo4002.billing.persistence.entity.TransactionEntity;
import ca.ulaval.glo4002.billing.persistence.manager.HibernateQueryHelper;
import ca.ulaval.glo4002.billing.service.repository.TransactionRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionHibernateRepository implements TransactionRepository
{
    private final HibernateQueryHelper<TransactionEntity> transactionEntityHibernateQueryHelper;
    private final EntityManagerFactory entityManagerFactory;
    private final TransactionAssembler transactionAssembler;

    public TransactionHibernateRepository(
            HibernateQueryHelper<TransactionEntity> transactionEntityHibernateQueryHelper,
            EntityManagerFactory entityManagerFactory, TransactionAssembler transactionAssembler)
    {
        this.transactionEntityHibernateQueryHelper = transactionEntityHibernateQueryHelper;
        this.entityManagerFactory = entityManagerFactory;
        this.transactionAssembler = transactionAssembler;
    }

    @Override
    public synchronized void save(Transaction transaction)
    {
        TransactionEntity transactionEntity = this.transactionAssembler.toPersistenceModel(transaction);

        this.transactionEntityHibernateQueryHelper.save(transactionEntity);
    }

    @Override
    public synchronized List<Transaction> findByFilter(Optional<String> optionalStartMonth, Optional<String>
            optionalEndMonth, Optional<Long> optionalYear)
    {
        List<Transaction> transactions = findAll();

        if (optionalYear.isPresent())
        {
            if (optionalStartMonth.isPresent())
            {
                int startMonth = Integer.valueOf(optionalStartMonth.get());
                transactions = filterTransactionsByStartMonth(transactions, startMonth);
            }

            if (optionalEndMonth.isPresent())
            {
                int endMonth = Integer.valueOf(optionalEndMonth.get());
                transactions = filterTransactionsByEndMonth(transactions, endMonth);
            }

            transactions = filterTransactionsByYear(optionalYear.get(), transactions);
        }

        return transactions;
    }

    private synchronized List<Transaction> findAll()
    {
        return this.transactionEntityHibernateQueryHelper.findAll()
                .stream()
                .map(this.transactionAssembler::toDomainModel)
                .collect(Collectors.toList());
    }

    private List<Transaction> filterTransactionsByStartMonth(List<Transaction> transactions, int startMonth)
    {
        transactions = transactions.stream()
                .filter(transaction -> LocalDateTime.ofInstant(transaction.getDate(), ZoneId.systemDefault())
                        .getMonthValue() >= startMonth)
                .collect(Collectors.toList());
        return transactions;
    }

    private List<Transaction> filterTransactionsByYear(long year, List<Transaction> transactions)
    {
        transactions = transactions.stream()
                .filter(transaction -> LocalDateTime.ofInstant(transaction.getDate(), ZoneId.systemDefault())
                        .getYear() == year)
                .collect(Collectors.toList());
        return transactions;
    }

    private List<Transaction> filterTransactionsByEndMonth(List<Transaction> transactions, int endMonth)
    {
        transactions = transactions.stream()
                .filter(transaction -> LocalDateTime.ofInstant(transaction.getDate(), ZoneId.systemDefault())
                        .getMonthValue() <= endMonth)
                .collect(Collectors.toList());
        return transactions;
    }

    @Override
    public synchronized Money retrieveCurrentLedgerBalance()
    {
        //@formatter:off
        String query =
        "select " +
            "BALANCE " +
        "from " +
            "TRANSACTION " +
        "where " +
            "TRANSACTION_ID = " +
            "(" +
                "select " +
                    "max(TRANSACTION_ID) " +
                "from " +
                    "TRANSACTION" +
            ")";
        //@formatter:on

        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        BigDecimal currentLedgerBalance = (BigDecimal) (entityManager.createNativeQuery(query)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null));

        return currentLedgerBalance == null ? Money.ZERO : Money.valueOf(currentLedgerBalance);
    }
}
