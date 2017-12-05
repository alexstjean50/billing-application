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
import java.util.List;
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
    public synchronized List<Transaction> findAll()
    {
        return this.transactionEntityHibernateQueryHelper.findAll()
                .stream()
                .map(this.transactionAssembler::toDomainModel)
                .collect(Collectors.toList());
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
