package ca.ulaval.glo4002.billing.service;

import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;
import ca.ulaval.glo4002.billing.service.dto.request.PaymentCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.request.assembler.PaymentAssembler;
import ca.ulaval.glo4002.billing.service.dto.response.PaymentCreationResponse;
import ca.ulaval.glo4002.billing.service.dto.response.assembler.PaymentCreationResponseAssembler;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.payment.PaymentRepository;
import ca.ulaval.glo4002.billing.service.retriever.AccountRetriever;

import java.time.Instant;

public class PaymentService
{
    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentAssembler paymentAssembler;
    private final PaymentCreationResponseAssembler paymentCreationResponseAssembler;
    private final AccountRetriever accountRetriever;

    public PaymentService(AccountRepository accountRepository,
                          PaymentRepository paymentRepository,
                          PaymentAssembler paymentAssembler,
                          PaymentCreationResponseAssembler paymentCreationResponseAssembler, AccountRetriever
                                  accountRetriever)
    {
        this.accountRepository = accountRepository;
        this.paymentRepository = paymentRepository;
        this.paymentAssembler = paymentAssembler;
        this.paymentCreationResponseAssembler = paymentCreationResponseAssembler;
        this.accountRetriever = accountRetriever;
    }

    public PaymentCreationResponse createPayment(PaymentCreationRequest request)
    {
        Account account = this.accountRetriever.retrieveClientAccount(request.clientId);
        long paymentNumber = this.paymentRepository.retrieveNextPaymentNumber();
        Instant paymentDate = Instant.now();

        Payment payment = this.paymentAssembler.toPayment(request, paymentNumber, paymentDate);
        account.addPayment(payment);

        this.accountRepository.save(account);

        return this.paymentCreationResponseAssembler.toResponse(paymentNumber);
    }
}
