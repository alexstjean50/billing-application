package ca.ulaval.glo4002.billing.persistence.repository.payment;

import ca.ulaval.glo4002.billing.service.repository.payment.PaymentRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.math.BigInteger;
import java.util.Optional;

public class PaymentHibernateRepository implements PaymentRepository
{
    private final EntityManagerFactory entityManagerFactory;

    public PaymentHibernateRepository(EntityManagerFactory entityManagerFactory)
    {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public long retrieveNextPaymentNumber()
    {
        Optional<BigInteger> maximumPaymentNumber = this.retrieveMaximumPaymentNumber();

        return maximumPaymentNumber.orElse(BigInteger.ZERO)
                .longValue() + 1;
    }

    private Optional<BigInteger> retrieveMaximumPaymentNumber()
    {
        String query = "select MAX(PAYMENT_NUMBER) from PAYMENT";
        EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        BigInteger maximumPaymentNumber = (BigInteger) (entityManager.createNativeQuery(query)
                .getSingleResult());

        return Optional.ofNullable(maximumPaymentNumber);
    }
}
