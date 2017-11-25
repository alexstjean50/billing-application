package ca.ulaval.glo4002.billing.persistence.manager;

import ca.ulaval.glo4002.billing.persistence.assembler.account.AccountAssembler;
import ca.ulaval.glo4002.billing.persistence.assembler.client.ClientAssembler;
import ca.ulaval.glo4002.billing.persistence.assembler.factory.AccountAssemblerFactory;
import ca.ulaval.glo4002.billing.persistence.assembler.product.ProductAssembler;
import ca.ulaval.glo4002.billing.persistence.assembler.transaction.TransactionAssembler;
import ca.ulaval.glo4002.billing.persistence.entity.AccountEntity;
import ca.ulaval.glo4002.billing.persistence.entity.TransactionEntity;
import ca.ulaval.glo4002.billing.persistence.manager.factory.EntityManagerFactoryFactory;
import ca.ulaval.glo4002.billing.persistence.repository.account.AccountHibernateRepository;
import ca.ulaval.glo4002.billing.persistence.repository.bill.BillHibernateRepository;
import ca.ulaval.glo4002.billing.persistence.repository.client.ClientRestRepository;
import ca.ulaval.glo4002.billing.persistence.repository.payment.PaymentHibernateRepository;
import ca.ulaval.glo4002.billing.persistence.repository.product.ProductRestRepository;
import ca.ulaval.glo4002.billing.persistence.repository.transaction.TransactionHibernateRepository;
import ca.ulaval.glo4002.billing.service.filter.BillsFilterFactory;
import ca.ulaval.glo4002.billing.service.repository.TransactionRepository;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.bill.BillRepository;
import ca.ulaval.glo4002.billing.service.repository.client.ClientRepository;
import ca.ulaval.glo4002.billing.service.repository.payment.PaymentRepository;
import ca.ulaval.glo4002.billing.service.repository.product.ProductRepository;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

public class ServiceLocator
{
    public static final Class<ClientRepository> CLIENT_REPOSITORY = ClientRepository.class;
    public static final Class<ProductRepository> PRODUCT_REPOSITORY = ProductRepository.class;
    public static final Class<AccountRepository> ACCOUNT_REPOSITORY = AccountRepository.class;
    public static final Class<BillRepository> BILL_REPOSITORY = BillRepository.class;
    public static final Class<PaymentRepository> PAYMENT_REPOSITORY = PaymentRepository.class;
    public static final Class<TransactionRepository> TRANSACTION_REPOSITORY = TransactionRepository.class;
    private static ServiceLocator instance;

    static
    {
        if (instance == null)
        {
            instance = new ServiceLocator();
            instance.loadRepositories();
        }
    }

    private final Map<Class<?>, Object> services;

    private ServiceLocator()
    {
        this.services = new HashMap<>();
    }

    public static <T> T getService(Class<T> key)
    {
        Object requestedRepository = instance.services.get(key);
        return key.cast(requestedRepository);
    }

    private void loadRepositories()
    {
        loadRestRepositories();
        loadHibernateRepositories();
    }

    private void loadRestRepositories()
    {
        RestTemplate restTemplate = new RestTemplate();
        ClientAssembler clientAssembler = new ClientAssembler();
        ProductAssembler productAssembler = new ProductAssembler();

        this.loadService(CLIENT_REPOSITORY, new ClientRestRepository(restTemplate, clientAssembler));
        this.loadService(PRODUCT_REPOSITORY, new ProductRestRepository(restTemplate, productAssembler));
    }

    private void loadHibernateRepositories()
    {
        EntityManagerFactory entityManagerFactory = new EntityManagerFactoryFactory().create();

        HibernateQueryHelper<AccountEntity> accountEntityHibernateQueryHelper =
                new HibernateQueryHelper<>(AccountEntity.class, entityManagerFactory);

        HibernateQueryHelper<TransactionEntity> transactionEntityHibernateQueryHelper =
                new HibernateQueryHelper<>(TransactionEntity.class, entityManagerFactory);

        AccountAssembler accountAssembler = new AccountAssemblerFactory().create();

        TransactionAssembler transactionAssembler = new TransactionAssembler();

        BillsFilterFactory billsFilterFactory = new BillsFilterFactory();

        this.loadService(ACCOUNT_REPOSITORY, new AccountHibernateRepository(accountAssembler, entityManagerFactory,
                accountEntityHibernateQueryHelper, billsFilterFactory));
        this.loadService(BILL_REPOSITORY, new BillHibernateRepository(entityManagerFactory));
        this.loadService(PAYMENT_REPOSITORY, new PaymentHibernateRepository(entityManagerFactory));
        this.loadService(TRANSACTION_REPOSITORY,
                new TransactionHibernateRepository(transactionEntityHibernateQueryHelper, entityManagerFactory,
                        transactionAssembler));
    }

    private void loadService(Class key, Object service)
    {
        this.services.put(key, service);
    }
}
