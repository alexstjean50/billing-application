package ca.ulaval.glo4002.billing.persistence.repository.bill;

import ca.ulaval.glo4002.billing.service.repository.bill.BillRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.math.BigInteger;
import java.util.Optional;

public class BillHibernateRepository implements BillRepository
{
    private final EntityManagerFactory entityManagerFactory;

    public BillHibernateRepository(EntityManagerFactory entityManagerFactory)
    {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public synchronized long retrieveNextBillNumber()
    {
        Optional<BigInteger> maximumBillNumber = this.retrieveMaximumBillNumber();

        return maximumBillNumber.orElse(BigInteger.ZERO)
                .longValue() + 1;
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
