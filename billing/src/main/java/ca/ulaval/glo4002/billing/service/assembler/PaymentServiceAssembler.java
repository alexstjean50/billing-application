package ca.ulaval.glo4002.billing.service.assembler;

import ca.ulaval.glo4002.billing.domain.billing.account.AccountFactory;
import ca.ulaval.glo4002.billing.persistence.manager.ServiceLocator;
import ca.ulaval.glo4002.billing.service.PaymentService;
import ca.ulaval.glo4002.billing.service.dto.request.assembler.PaymentAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.PaymentCreationResponseAssembler;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.client.ClientRepository;
import ca.ulaval.glo4002.billing.service.repository.payment.PaymentRepository;
import ca.ulaval.glo4002.billing.service.retriever.AccountRetriever;

public class PaymentServiceAssembler
{
    public PaymentService create()
    {
        AccountRepository accountRepository = ServiceLocator.getService(ServiceLocator.ACCOUNT_REPOSITORY);
        PaymentRepository paymentRepository = ServiceLocator.getService(ServiceLocator.PAYMENT_REPOSITORY);
        ClientRepository clientRepository = ServiceLocator.getService(ServiceLocator.CLIENT_REPOSITORY);
        AccountFactory accountFactory = new AccountFactory();
        PaymentAssembler paymentAssembler = new PaymentAssembler();
        PaymentCreationResponseAssembler paymentCreationResponseAssembler = new PaymentCreationResponseAssembler();
        AccountRetriever accountRetriever = new AccountRetriever(clientRepository, accountRepository, accountFactory);

        return new PaymentService(accountRepository, paymentRepository, paymentAssembler,
                paymentCreationResponseAssembler, accountRetriever);
    }
}
