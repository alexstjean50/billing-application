package ca.ulaval.glo4002.billing.service.assembler;

import ca.ulaval.glo4002.billing.contexts.ServiceLocator;
import ca.ulaval.glo4002.billing.service.PaymentService;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainAccountAssembler;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainPaymentAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.PaymentCreationResponseAssembler;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.client.ClientRepository;
import ca.ulaval.glo4002.billing.service.repository.payment.PaymentRepository;
import ca.ulaval.glo4002.billing.service.retriever.AccountRetriever;

public class PaymentServiceAssembler
{
    public PaymentService create()
    {
        AccountRepository accountRepository = ServiceLocator.getService(AccountRepository.class);
        PaymentRepository paymentRepository = ServiceLocator.getService(PaymentRepository.class);
        ClientRepository clientRepository = ServiceLocator.getService(ClientRepository.class);
        DomainAccountAssembler domainAccountAssembler = new DomainAccountAssembler();
        DomainPaymentAssembler domainPaymentAssembler = new DomainPaymentAssembler(paymentRepository);
        PaymentCreationResponseAssembler paymentCreationResponseAssembler = new PaymentCreationResponseAssembler();
        AccountRetriever accountRetriever = new AccountRetriever(clientRepository, accountRepository,
                domainAccountAssembler);

        return new PaymentService(accountRepository, domainPaymentAssembler,
                paymentCreationResponseAssembler, accountRetriever);
    }
}