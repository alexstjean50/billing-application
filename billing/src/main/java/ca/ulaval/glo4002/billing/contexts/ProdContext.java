package ca.ulaval.glo4002.billing.contexts;

import ca.ulaval.glo4002.billing.persistence.assembler.account.AccountAssembler;
import ca.ulaval.glo4002.billing.persistence.assembler.bill.BillAssembler;
import ca.ulaval.glo4002.billing.persistence.assembler.client.ClientAssembler;
import ca.ulaval.glo4002.billing.persistence.assembler.factory.AccountAssemblerFactory;
import ca.ulaval.glo4002.billing.persistence.assembler.factory.BillAssemblerFactory;
import ca.ulaval.glo4002.billing.persistence.assembler.product.ProductAssembler;
import ca.ulaval.glo4002.billing.persistence.assembler.transaction.TransactionAssembler;
import ca.ulaval.glo4002.billing.persistence.entity.AccountEntity;
import ca.ulaval.glo4002.billing.persistence.entity.BillEntity;
import ca.ulaval.glo4002.billing.persistence.entity.TransactionEntity;
import ca.ulaval.glo4002.billing.persistence.manager.HibernateQueryHelper;
import ca.ulaval.glo4002.billing.persistence.manager.factory.EntityManagerFactoryProvider;
import ca.ulaval.glo4002.billing.persistence.repository.account.AccountHibernateRepository;
import ca.ulaval.glo4002.billing.persistence.repository.bill.BillHibernateRepository;
import ca.ulaval.glo4002.billing.persistence.repository.client.ClientRestRepository;
import ca.ulaval.glo4002.billing.persistence.repository.clock.DefaultClockRepository;
import ca.ulaval.glo4002.billing.persistence.repository.payment.PaymentHibernateRepository;
import ca.ulaval.glo4002.billing.persistence.repository.product.ProductRestRepository;
import ca.ulaval.glo4002.billing.persistence.repository.transaction.TransactionHibernateRepository;
import ca.ulaval.glo4002.billing.service.repository.TransactionRepository;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.bill.BillRepository;
import ca.ulaval.glo4002.billing.service.repository.client.ClientRepository;
import ca.ulaval.glo4002.billing.service.repository.clock.ClockRepository;
import ca.ulaval.glo4002.billing.service.repository.payment.PaymentRepository;
import ca.ulaval.glo4002.billing.service.repository.product.ProductRepository;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManagerFactory;

public class ProdContext implements Context
{
    @Override
    public void apply()
    {
        loadRepositories();
    }

    private static void loadRepositories()
    {
        ServiceLocator.loadService(ClockRepository.class, new DefaultClockRepository());
        loadRestRepositories();
        loadHibernateRepositories();
    }

    private static void loadRestRepositories()
    {
        RestTemplate restTemplate = new RestTemplate();
        ClientAssembler clientAssembler = new ClientAssembler();
        ProductAssembler productAssembler = new ProductAssembler();

        ServiceLocator.loadService(ClientRepository.class, new ClientRestRepository(restTemplate, clientAssembler));
        ServiceLocator.loadService(ProductRepository.class, new ProductRestRepository(restTemplate, productAssembler));
    }

    private static void loadHibernateRepositories()
    {
        EntityManagerFactory entityManagerFactory = EntityManagerFactoryProvider.getInstance();

        HibernateQueryHelper<AccountEntity> accountEntityHibernateQueryHelper =
                new HibernateQueryHelper<>(AccountEntity.class, entityManagerFactory);
        HibernateQueryHelper<BillEntity> billEntityHibernateQueryHelper =
                new HibernateQueryHelper<>(BillEntity.class, entityManagerFactory);
        HibernateQueryHelper<TransactionEntity> transactionEntityHibernateQueryHelper =
                new HibernateQueryHelper<>(TransactionEntity.class, entityManagerFactory);

        AccountAssembler accountAssembler = new AccountAssemblerFactory().create();
        BillAssembler billAssembler = new BillAssemblerFactory().create();

        TransactionAssembler transactionAssembler = new TransactionAssembler();

        ServiceLocator.loadService(AccountRepository.class, new AccountHibernateRepository(accountAssembler,
                entityManagerFactory,
                accountEntityHibernateQueryHelper));
        ServiceLocator.loadService(BillRepository.class, new BillHibernateRepository(entityManagerFactory,
                billAssembler, billEntityHibernateQueryHelper));
        ServiceLocator.loadService(PaymentRepository.class, new PaymentHibernateRepository(entityManagerFactory));
        ServiceLocator.loadService(TransactionRepository.class,
                new TransactionHibernateRepository(transactionEntityHibernateQueryHelper, entityManagerFactory,
                        transactionAssembler));
    }
}
