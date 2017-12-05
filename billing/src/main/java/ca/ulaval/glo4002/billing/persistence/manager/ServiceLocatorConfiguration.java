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
import ca.ulaval.glo4002.billing.service.repository.TransactionRepository;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.bill.BillRepository;
import ca.ulaval.glo4002.billing.service.repository.client.ClientRepository;
import ca.ulaval.glo4002.billing.service.repository.payment.PaymentRepository;
import ca.ulaval.glo4002.billing.service.repository.product.ProductRepository;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManagerFactory;

public class ServiceLocatorConfiguration
{
    public static final Class<ClientRepository> CLIENT_REPOSITORY = ClientRepository.class;
    public static final Class<ProductRepository> PRODUCT_REPOSITORY = ProductRepository.class;
    public static final Class<AccountRepository> ACCOUNT_REPOSITORY = AccountRepository.class;
    public static final Class<BillRepository> BILL_REPOSITORY = BillRepository.class;
    public static final Class<PaymentRepository> PAYMENT_REPOSITORY = PaymentRepository.class;
    public static final Class<TransactionRepository> TRANSACTION_REPOSITORY = TransactionRepository.class;

    public static void configure()
    {
        ServiceLocator locator = new ServiceLocator();
        ServiceLocator.load(locator);
        loadRepositories(locator);
    }

    private static void loadRepositories(ServiceLocator locator)
    {
        loadRestRepositories(locator);
        loadHibernateRepositories(locator);
    }

    private static void loadRestRepositories(ServiceLocator locator)
    {
        RestTemplate restTemplate = new RestTemplate();
        ClientAssembler clientAssembler = new ClientAssembler();
        ProductAssembler productAssembler = new ProductAssembler();

        locator.loadService(CLIENT_REPOSITORY, new ClientRestRepository(restTemplate, clientAssembler));
        locator.loadService(PRODUCT_REPOSITORY, new ProductRestRepository(restTemplate, productAssembler));
    }

    private static void loadHibernateRepositories(ServiceLocator locator)
    {
        EntityManagerFactory entityManagerFactory = new EntityManagerFactoryFactory().create();

        HibernateQueryHelper<AccountEntity> accountEntityHibernateQueryHelper =
                new HibernateQueryHelper<>(AccountEntity.class, entityManagerFactory);

        HibernateQueryHelper<TransactionEntity> transactionEntityHibernateQueryHelper =
                new HibernateQueryHelper<>(TransactionEntity.class, entityManagerFactory);

        AccountAssembler accountAssembler = new AccountAssemblerFactory().create();

        TransactionAssembler transactionAssembler = new TransactionAssembler();

        locator.loadService(ACCOUNT_REPOSITORY, new AccountHibernateRepository(accountAssembler, entityManagerFactory,
                accountEntityHibernateQueryHelper));
        locator.loadService(BILL_REPOSITORY, new BillHibernateRepository(entityManagerFactory));
        locator.loadService(PAYMENT_REPOSITORY, new PaymentHibernateRepository(entityManagerFactory));
        locator.loadService(TRANSACTION_REPOSITORY,
                new TransactionHibernateRepository(transactionEntityHibernateQueryHelper, entityManagerFactory,
                        transactionAssembler));
    }
}
