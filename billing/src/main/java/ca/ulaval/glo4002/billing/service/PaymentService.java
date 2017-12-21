package ca.ulaval.glo4002.billing.service;

import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainPaymentAssembler;
import ca.ulaval.glo4002.billing.service.dto.request.PaymentCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.response.PaymentCreationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.PaymentCreationResponseAssembler;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.clock.ClockRepository;
import ca.ulaval.glo4002.billing.service.retriever.AccountRetriever;

import java.time.Instant;

public class PaymentService
{
    private final AccountRepository accountRepository;
    private final DomainPaymentAssembler domainPaymentAssembler;
    private final PaymentCreationResponseAssembler paymentCreationResponseAssembler;
    private final AccountRetriever accountRetriever;
    private final ClockRepository clockRepository;

    public PaymentService(AccountRepository accountRepository,
                          DomainPaymentAssembler domainPaymentAssembler,
                          PaymentCreationResponseAssembler paymentCreationResponseAssembler,
                          AccountRetriever accountRetriever, ClockRepository clockRepository)
    {
        this.accountRepository = accountRepository;
        this.domainPaymentAssembler = domainPaymentAssembler;
        this.paymentCreationResponseAssembler = paymentCreationResponseAssembler;
        this.accountRetriever = accountRetriever;
        this.clockRepository = clockRepository;
    }

    public PaymentCreationResponse createPayment(PaymentCreationRequest request)
    {
        Instant currentTime = this.clockRepository.retrieveCurrentTime();
        Account account = this.accountRetriever.retrieveClientAccount(request.getClientId());

        Payment payment = this.domainPaymentAssembler.toNewPayment(request);
        account.addPayment(payment, currentTime);

        this.accountRepository.save(account);

        return this.paymentCreationResponseAssembler.toResponse(payment.getPaymentNumber());
    }
}
