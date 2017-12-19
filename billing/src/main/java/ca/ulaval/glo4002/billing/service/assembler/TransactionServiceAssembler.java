package ca.ulaval.glo4002.billing.service.assembler;

import ca.ulaval.glo4002.billing.contexts.ServiceLocator;
import ca.ulaval.glo4002.billing.service.TransactionService;
import ca.ulaval.glo4002.billing.service.repository.TransactionRepository;

public class TransactionServiceAssembler
{
    public TransactionService create()
    {
        TransactionRepository transactionRepository = ServiceLocator.getService(TransactionRepository.class);

        return new TransactionService(transactionRepository);
    }
}
