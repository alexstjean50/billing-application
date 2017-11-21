package ca.ulaval.glo4002.billing.service;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.domain.billing.account.AccountFactory;
import ca.ulaval.glo4002.billing.domain.billing.client.Client;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;
import ca.ulaval.glo4002.billing.domain.billing.payment.PaymentMethod;
import ca.ulaval.glo4002.billing.domain.billing.payment.PaymentMethodSource;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import ca.ulaval.glo4002.billing.persistence.repository.AccountClientNotFoundException;
import ca.ulaval.glo4002.billing.service.dto.request.PaymentCreationRequest;
import ca.ulaval.glo4002.billing.service.dto.response.PaymentCreationResponse;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.client.ClientRepository;
import ca.ulaval.glo4002.billing.service.repository.payment.PaymentRepository;

import java.time.Instant;
import java.util.ArrayList;

public class PaymentService
{
    private final AccountRepository accountRepository;
    private final PaymentRepository paymentRepository;
    private final ClientRepository clientRepository;
    private final AccountFactory accountFactory;

    public PaymentService(AccountRepository accountRepository,
                          PaymentRepository paymentRepository,
                          ClientRepository clientRepository,
                          AccountFactory accountFactory)
    {
        this.accountRepository = accountRepository;
        this.paymentRepository = paymentRepository;
        this.clientRepository = clientRepository;
        this.accountFactory = accountFactory;
    }

    public PaymentCreationResponse createPayment(PaymentCreationRequest request)
    {
        Account account = this.retrieveClientAccount(request.clientId);
        long newPaymentNumber = this.paymentRepository.retrieveNextPaymentNumber();

        Instant paymentDate = Instant.now();
        PaymentMethod paymentMethod = new PaymentMethod(Identity.EMPTY, request.paymentMethod.account,
                PaymentMethodSource.valueOf(request.paymentMethod.source));
        Payment payment = new Payment(Identity.EMPTY, newPaymentNumber, Money.valueOf(request.amount),
                paymentDate, paymentMethod, new ArrayList<>());

        account.addPayment(payment);
        account.allocate();
        this.accountRepository.save(account);

        return PaymentCreationResponse.create(newPaymentNumber);
    }

    private Account retrieveClientAccount(long clientId)
    {
        Account account;
        try
        {
            account = this.accountRepository.findByClientId(clientId);
        }
        catch (AccountClientNotFoundException exception)
        {
            Client client = this.clientRepository.findById(clientId);
            account = this.accountFactory.create(client);
        }
        return account;
    }
}
