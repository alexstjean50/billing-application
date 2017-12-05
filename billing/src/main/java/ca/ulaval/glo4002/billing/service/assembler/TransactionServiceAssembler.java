package ca.ulaval.glo4002.billing.service.assembler;

import ca.ulaval.glo4002.billing.persistence.manager.ServiceLocator;
import ca.ulaval.glo4002.billing.persistence.manager.ServiceLocatorConfiguration;
import ca.ulaval.glo4002.billing.service.TransactionService;
import ca.ulaval.glo4002.billing.service.repository.TransactionRepository;

public class TransactionServiceAssembler
{
    public TransactionService create()
    {
        TransactionRepository transactionRepository = ServiceLocator.getService(ServiceLocatorConfiguration.TRANSACTION_REPOSITORY);

        return new TransactionService(transactionRepository);
    }
}
