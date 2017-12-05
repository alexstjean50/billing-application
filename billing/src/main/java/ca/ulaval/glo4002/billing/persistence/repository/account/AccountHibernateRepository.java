package ca.ulaval.glo4002.billing.persistence.repository.account;

import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.persistence.assembler.account.AccountAssembler;
import ca.ulaval.glo4002.billing.persistence.entity.AccountEntity;
import ca.ulaval.glo4002.billing.persistence.manager.HibernateQueryHelper;
import ca.ulaval.glo4002.billing.persistence.repository.AccountClientNotFoundException;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AccountHibernateRepository implements AccountRepository
{
    private final EntityManagerFactory entityManagerFactory;
    private final HibernateQueryHelper<AccountEntity> hibernateQueryHelper;
    private final AccountAssembler accountAssembler;
    private static final Map<Long, Account> accountsByClientIdCache = Collections.synchronizedMap(new HashMap<>());
    private static final Map<Long, Account> accountsByBillNumberCache = Collections.synchronizedMap(new HashMap<>());

    public AccountHibernateRepository(AccountAssembler accountAssembler, EntityManagerFactory entityManagerFactory,
                                      HibernateQueryHelper<AccountEntity>
                                              hibernateQueryHelper)
    {
        this.entityManagerFactory = entityManagerFactory;
        this.hibernateQueryHelper = hibernateQueryHelper;
        this.accountAssembler = accountAssembler;
    }

    @Override
    public synchronized void save(Account account)
    {
        AccountEntity accountEntity = this.accountAssembler.toPersistenceModel(account);
        this.hibernateQueryHelper.save(accountEntity);

        updateCache(account.getClientId());
    }

    private void updateCache(long clientId)
    {

        Account account = findByClientIdInDatabase(clientId);
        accountsByClientIdCache.put(clientId, account);
        account.getBills()
                .forEach(bill -> accountsByBillNumberCache.put(bill.getBillNumber(), account));
    }

    @Override
    public Account findByClientId(long clientId)
    {
        return findByClientIdInCache(clientId).orElse(findByClientIdInDatabase(clientId));
    }

    private Optional<Account> findByClientIdInCache(long clientId)
    {
        return Optional.ofNullable(accountsByClientIdCache.get(clientId));
    }

    private Account findByClientIdInDatabase(long clientId)
    {
        AccountEntity accountEntity;
        try
        {
            //@formatter:off
            String query =
                    "select " +
                            "accountEntity " +
                            "from " +
                            "AccountEntity accountEntity " +
                            "where " +
                            "accountEntity.clientId = :clientId";
            //@formatter:on
            EntityManager entityManager = this.entityManagerFactory.createEntityManager();
            accountEntity = entityManager
                    .createQuery(query, AccountEntity.class)
                    .setParameter("clientId", clientId)
                    .getSingleResult();
            return this.accountAssembler.toDomainModel(accountEntity);
        }
        catch (NoResultException exception)
        {
            throw new AccountClientNotFoundException(exception, String.valueOf(clientId));
        }
    }

    @Override
    public synchronized Account findByBillNumber(long billNumber)
    {
        return findByBillNumberInCache(billNumber).orElse(findByBillNumberInDatabase(billNumber));
    }

    private Optional<Account> findByBillNumberInCache(long billNumber)
    {
        return Optional.ofNullable(accountsByBillNumberCache.get(billNumber));
    }

    private Account findByBillNumberInDatabase(long billNumber)
    {
        try
        {
            //@formatter:off
            String query =
                    "select " +
                            "accountEntity " +
                            "from " +
                            "AccountEntity accountEntity " +
                            "join " +
                            "accountEntity.billEntities bills " +
                            "where " +
                            "bills.billNumber = :billNumber";
            //@formatter:on
            EntityManager entityManager = this.entityManagerFactory.createEntityManager();
            AccountEntity accountEntity = entityManager
                    .createQuery(query, AccountEntity.class)
                    .setParameter("billNumber", billNumber)
                    .getSingleResult();
            return this.accountAssembler.toDomainModel(accountEntity);
        }
        catch (NoResultException exception)
        {
            throw new BillNotFoundException(exception, String.valueOf(billNumber));
        }
    }
}
