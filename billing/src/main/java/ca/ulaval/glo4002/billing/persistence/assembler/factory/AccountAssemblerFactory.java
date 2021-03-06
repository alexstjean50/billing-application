package ca.ulaval.glo4002.billing.persistence.assembler.factory;

import ca.ulaval.glo4002.billing.contexts.ServiceLocator;
import ca.ulaval.glo4002.billing.persistence.assembler.account.AccountAssembler;
import ca.ulaval.glo4002.billing.persistence.assembler.bill.BillAssembler;
import ca.ulaval.glo4002.billing.persistence.assembler.payment.PaymentAssembler;
import ca.ulaval.glo4002.billing.service.repository.client.ClientRepository;

public class AccountAssemblerFactory
{
    private final ClientRepository clientRepository;
    private final PaymentAssemblerFactory paymentAssemblerFactory;
    private final BillAssemblerFactory billAssemblerFactory;

    public AccountAssemblerFactory()
    {
        this(ServiceLocator.getService(ClientRepository.class), new PaymentAssemblerFactory(),
                new
                        BillAssemblerFactory());
    }

    private AccountAssemblerFactory(ClientRepository clientRepository, PaymentAssemblerFactory paymentAssemblerFactory,
                                    BillAssemblerFactory billAssemblerFactory)
    {
        this.clientRepository = clientRepository;
        this.paymentAssemblerFactory = paymentAssemblerFactory;
        this.billAssemblerFactory = billAssemblerFactory;
    }

    public AccountAssembler create()
    {
        BillAssembler billAssembler = this.billAssemblerFactory.create();
        PaymentAssembler paymentAssembler = this.paymentAssemblerFactory.create();

        return new AccountAssembler(this.clientRepository, paymentAssembler, billAssembler);
    }
}
