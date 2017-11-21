package ca.ulaval.glo4002.billing.service.factory;

import ca.ulaval.glo4002.billing.persistence.manager.ServiceLocator;
import ca.ulaval.glo4002.billing.service.TransactionService;
import ca.ulaval.glo4002.billing.service.repository.TransactionRepository;

public class TransactionServiceFactory
{
    public TransactionService create()
    {
        TransactionRepository transactionRepository = ServiceLocator.getService(ServiceLocator.TRANSACTION_REPOSITORY);

        return new TransactionService(transactionRepository);
    }
}
