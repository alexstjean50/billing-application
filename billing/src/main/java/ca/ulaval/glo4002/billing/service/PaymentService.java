package ca.ulaval.glo4002.billing.service;

import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainPaymentAssembler;
import ca.ulaval.glo4002.billing.service.dto.request.PaymentCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.response.PaymentCreationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.PaymentCreationResponseAssembler;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.retriever.AccountRetriever;

public class PaymentService
{
    private final AccountRepository accountRepository;
    private final DomainPaymentAssembler domainPaymentAssembler;
    private final PaymentCreationResponseAssembler paymentCreationResponseAssembler;
    private final AccountRetriever accountRetriever;

    public PaymentService(AccountRepository accountRepository,
                          DomainPaymentAssembler domainPaymentAssembler,
                          PaymentCreationResponseAssembler paymentCreationResponseAssembler,
                          AccountRetriever accountRetriever)
    {
        this.accountRepository = accountRepository;
        this.domainPaymentAssembler = domainPaymentAssembler;
        this.paymentCreationResponseAssembler = paymentCreationResponseAssembler;
        this.accountRetriever = accountRetriever;
    }

    public PaymentCreationResponse createPayment(PaymentCreationRequest request)
    {
        Account account = this.accountRetriever.retrieveClientAccount(request.clientId);

        Payment payment = this.domainPaymentAssembler.toNewPayment(request);
        account.addPayment(payment);

        this.accountRepository.save(account);

        return this.paymentCreationResponseAssembler.toResponse(payment.getPaymentNumber());
    }
}
