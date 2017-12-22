package ca.ulaval.glo4002.billing.persistence.repository.transaction;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.transaction.Transaction;
import ca.ulaval.glo4002.billing.persistence.assembler.transaction.TransactionAssembler;
import ca.ulaval.glo4002.billing.persistence.entity.TransactionEntity;
import ca.ulaval.glo4002.billing.persistence.manager.HibernateQueryHelper;
import ca.ulaval.glo4002.billing.service.repository.TransactionRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    public List<Transaction> findByFilter(Optional<String> optionalStartMonth, Optional<String>
            optionalEndMonth, Optional<Integer> optionalYear)
    {
        try
        {
            //@formatter:off
            String query =
                    "select " +
                        "transactionEntity " +
                    "from " +
                        "TransactionEntity transactionEntity " +
                    (optionalYear.isPresent() ? generateMonthFilterString(optionalStartMonth,
                            optionalEndMonth) : "");
            //@formatter:on
            return executeFilteredQuery(query, optionalStartMonth, optionalEndMonth,
                    optionalYear)
                    .stream()
                    .map(this.transactionAssembler::toDomainModel)
                    .collect(Collectors.toList());
        }
        catch (NoResultException exception)
        {
            return new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    private List<TransactionEntity> executeFilteredQuery(String query, Optional<String> optionalStartMonth,
                                                         Optional<String>
                                                                 optionalEndMonth, Optional<Integer> optionalYear)
    {
        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        Query hibernateQuery = entityManager
                .createQuery(query, TransactionEntity.class);

        if (optionalYear.isPresent())
        {
            hibernateQuery.setParameter("year", optionalYear.get());

            if (optionalStartMonth.isPresent())
            {
                int startMonth = Integer.valueOf(optionalStartMonth.get());
                hibernateQuery.setParameter("startMonth", startMonth);
            }

            if (optionalEndMonth.isPresent())
            {
                int endMonth = Integer.valueOf(optionalEndMonth.get());
                hibernateQuery.setParameter("endMonth", endMonth);
            }
        }

        return (List<TransactionEntity>) hibernateQuery.getResultList();
    }

    private String generateMonthFilterString(Optional<String> optionalStartMonth, Optional<String> optionalEndMonth)
    {
        String generatedFilter = "WHERE YEAR(transactionEntity.date) = :year ";
        if (optionalStartMonth.isPresent())
        {
            generatedFilter += "AND MONTH(transactionEntity.date) >= :startMonth ";
        }

        if (optionalEndMonth.isPresent())
        {
            generatedFilter += "AND MONTH(transactionEntity.date) <= :endMonth ";
        }

        return generatedFilter;
    }

    @SuppressWarnings("unchecked")
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
