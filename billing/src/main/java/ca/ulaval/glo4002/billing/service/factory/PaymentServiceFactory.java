package ca.ulaval.glo4002.billing.service.factory;

import ca.ulaval.glo4002.billing.domain.billing.account.AccountFactory;
import ca.ulaval.glo4002.billing.persistence.manager.ServiceLocator;
import ca.ulaval.glo4002.billing.service.PaymentService;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.client.ClientRepository;
import ca.ulaval.glo4002.billing.service.repository.payment.PaymentRepository;

public class PaymentServiceFactory
{
    public PaymentService create()
    {
        AccountRepository accountRepository = ServiceLocator.getService(ServiceLocator.ACCOUNT_REPOSITORY);
        PaymentRepository paymentRepository = ServiceLocator.getService(ServiceLocator.PAYMENT_REPOSITORY);
        ClientRepository clientRepository = ServiceLocator.getService(ServiceLocator.CLIENT_REPOSITORY);
        AccountFactory accountFactory = new AccountFactory();

        return new PaymentService(accountRepository, paymentRepository, clientRepository, accountFactory);
    }
}
