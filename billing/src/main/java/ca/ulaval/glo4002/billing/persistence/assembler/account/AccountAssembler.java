package ca.ulaval.glo4002.billing.persistence.assembler.account;

import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.domain.billing.client.Client;
import ca.ulaval.glo4002.billing.domain.billing.payment.Payment;
import ca.ulaval.glo4002.billing.domain.strategy.DefaultAllocationStrategy;
import ca.ulaval.glo4002.billing.persistence.assembler.bill.BillAssembler;
import ca.ulaval.glo4002.billing.persistence.assembler.payment.PaymentAssembler;
import ca.ulaval.glo4002.billing.persistence.entity.AccountEntity;
import ca.ulaval.glo4002.billing.persistence.entity.BillEntity;
import ca.ulaval.glo4002.billing.persistence.entity.PaymentEntity;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import ca.ulaval.glo4002.billing.service.repository.client.ClientRepository;

import java.util.List;
import java.util.stream.Collectors;

public class AccountAssembler
{
    private final ClientRepository clientRepository;
    private final PaymentAssembler paymentAssembler;
    private final BillAssembler billAssembler;

    public AccountAssembler(ClientRepository clientRepository, PaymentAssembler paymentAssembler, BillAssembler
            billAssembler)
    {
        this.clientRepository = clientRepository;
        this.paymentAssembler = paymentAssembler;
        this.billAssembler = billAssembler;
    }

    public Account toDomainModel(AccountEntity accountEntity)
    {
        Client client = this.clientRepository.findById(accountEntity.getClientId());
        List<Payment> payments = accountEntity.getPaymentEntities()
                .stream()
                .map(this.paymentAssembler::toDomainModel)
                .collect(Collectors.toList());

        List<Bill> bills = accountEntity.getBillEntities()
                .stream()
                .map(this.billAssembler::toDomainModel)
                .collect(Collectors.toList());

        return Account.create(new Identity(accountEntity.getAccountId()), client,
                new DefaultAllocationStrategy(), payments, bills);
    }

    public AccountEntity toPersistenceModel(Account account)
    {
        List<BillEntity> billEntities = account.getBills()
                .stream()
                .map(this.billAssembler::toPersistenceModel)
                .collect(Collectors.toList());

        List<PaymentEntity> paymentEntities = account.getPayments()
                .stream()
                .map(this.paymentAssembler::toPersistenceModel)
                .collect(Collectors.toList());

        return new AccountEntity((account.getAccountId()).getId(),
                account.getClient()
                        .getId(), billEntities, paymentEntities);
    }
}
