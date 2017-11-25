package ca.ulaval.glo4002.billing.persistence.repository.account;

import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.persistence.assembler.account.AccountAssembler;
import ca.ulaval.glo4002.billing.persistence.entity.AccountEntity;
import ca.ulaval.glo4002.billing.persistence.manager.HibernateQueryHelper;
import ca.ulaval.glo4002.billing.persistence.repository.AccountClientNotFoundException;
import ca.ulaval.glo4002.billing.service.dto.request.BillStatusParameter;
import ca.ulaval.glo4002.billing.service.filter.BillsFilter;
import ca.ulaval.glo4002.billing.service.filter.BillsFilterFactory;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import com.google.common.collect.ImmutableMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.*;
import java.util.stream.Collectors;

public class AccountHibernateRepository implements AccountRepository
{
    private final EntityManagerFactory entityManagerFactory;
    private final HibernateQueryHelper<AccountEntity> hibernateQueryHelper;
    private final AccountAssembler accountAssembler;
    private final BillsFilterFactory billsFilterFactory;

    public AccountHibernateRepository(AccountAssembler accountAssembler, EntityManagerFactory entityManagerFactory,
                                      HibernateQueryHelper<AccountEntity>
                                              hibernateQueryHelper, BillsFilterFactory billsFilterFactory)
    {
        this.entityManagerFactory = entityManagerFactory;
        this.hibernateQueryHelper = hibernateQueryHelper;
        this.accountAssembler = accountAssembler;
        this.billsFilterFactory = billsFilterFactory;
    }

    @Override
    public synchronized void save(Account account)
    {
        AccountEntity accountEntity = this.accountAssembler.toPersistenceModel(account);
        this.hibernateQueryHelper.save(accountEntity);
    }

    @Override
    public synchronized List<Account> findAll()
    {
        return this.hibernateQueryHelper.findAll()
                .stream()
                .map(accountAssembler::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized Account findByClientId(long clientId)
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

    @Override
    public Map<Long, List<Bill>> retrieveFilteredBillsOfClients(Optional<Long> optionalClientId, Optional<BillStatusParameter> status)
    {
        Map<Long, List<Bill>> billsByClientId = retrieveBillsOfClients(optionalClientId);

        if (status.isPresent())
        {
            billsByClientId = filterBills(status.get(), billsByClientId);
        }

        return billsByClientId;
    }

    private Map<Long, List<Bill>> filterBills(BillStatusParameter status, Map<Long, List<Bill>> billsByClientId)
    {
        Map<Long, List<Bill>> filteredBillsByClientId = new HashMap<>();
        BillsFilter billsFilter = this.billsFilterFactory.create(status);

        for (Map.Entry<Long, List<Bill>> bill : billsByClientId.entrySet())
        {
            List<Bill> filteredBills = billsFilter.filter(bill.getValue());
            filteredBillsByClientId.put(bill.getKey(), filteredBills);
        }

        return filteredBillsByClientId;
    }

    private Map<Long, List<Bill>> retrieveBillsOfClients(Optional<Long> optionalClientId)
    {
        return optionalClientId.map(this::retrieveClientBills)
                .orElseGet(this::retrieveAllClientBills);
    }

    private Map<Long, List<Bill>> retrieveAllClientBills()
    {
        return findAll()
                .stream()
                .collect(Collectors.toMap(Account::getClientId, Account::retrieveAcceptedBills));
    }

    private Map<Long, List<Bill>> retrieveClientBills(long clientId)
    {
        try {
            Account account = findByClientId(clientId);
            return ImmutableMap.of(clientId, account.retrieveAcceptedBills());
        } catch (AccountClientNotFoundException exception) {
            return ImmutableMap.of(clientId, Collections.emptyList());
        }

    }
}
