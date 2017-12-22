package ca.ulaval.glo4002.billing.persistence.repository.bill;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.persistence.assembler.bill.BillAssembler;
import ca.ulaval.glo4002.billing.persistence.entity.BillEntity;
import ca.ulaval.glo4002.billing.persistence.manager.HibernateQueryHelper;
import ca.ulaval.glo4002.billing.persistence.repository.account.BillNotFoundException;
import ca.ulaval.glo4002.billing.service.repository.bill.BillRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

public class BillHibernateRepository implements BillRepository
{
    private final EntityManagerFactory entityManagerFactory;
    private final BillAssembler billAssembler;
    private final HibernateQueryHelper<BillEntity> billEntityHibernateQueryHelper;

    public BillHibernateRepository(EntityManagerFactory entityManagerFactory, BillAssembler billAssembler,
                                   HibernateQueryHelper<BillEntity> billEntityHibernateQueryHelper)
    {
        this.entityManagerFactory = entityManagerFactory;
        this.billAssembler = billAssembler;
        this.billEntityHibernateQueryHelper = billEntityHibernateQueryHelper;
    }

    @Override
    public synchronized long retrieveNextBillNumber()
    {
        Optional<BigInteger> maximumBillNumber = this.retrieveMaximumBillNumber();

        return maximumBillNumber.orElse(BigInteger.ZERO)
                .longValue() + 1;
    }

    @Override
    public Money retrieveBillAmount(long billNumber)
    {
        try
        {
            //@formatter:off
            String query =
                    "select " +
                        "sum(QUANTITY * PAID_PRICE) " +
                    "from " +
                        "BILL bill " +
                    "join " +
                        "ITEM item ON bill.BILL_ID = item.BILL_ID " +
                    "WHERE " +
                        "bill.BILL_NUMBER = :billNumber";
            //@formatter:on
            EntityManager entityManager = this.entityManagerFactory.createEntityManager();
            BigDecimal amount = (BigDecimal) entityManager
                    .createNativeQuery(query)
                    .setParameter("billNumber", billNumber)
                    .getSingleResult();
            return Money.valueOf(amount);
        }
        catch (NoResultException exception)
        {
            throw new BillNotFoundException(exception, String.valueOf(billNumber));
        }
    }

    private synchronized Optional<BigInteger> retrieveMaximumBillNumber()
    {
        String query = "select MAX(BILL_NUMBER) from BILL";
        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        BigInteger maximumBillNumber = (BigInteger) (entityManager.createNativeQuery(query)
                .getSingleResult());

        return Optional.ofNullable(maximumBillNumber);
    }
}
