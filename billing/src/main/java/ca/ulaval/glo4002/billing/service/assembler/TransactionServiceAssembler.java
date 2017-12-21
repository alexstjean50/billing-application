package ca.ulaval.glo4002.billing.service.assembler;

import ca.ulaval.glo4002.billing.contexts.ServiceLocator;
import ca.ulaval.glo4002.billing.service.TransactionService;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainTransactionAssembler;
import ca.ulaval.glo4002.billing.service.repository.TransactionRepository;
import ca.ulaval.glo4002.billing.service.repository.clock.ClockRepository;

public class TransactionServiceAssembler
{
    public TransactionService create()
    {
        TransactionRepository transactionRepository = ServiceLocator.getService(TransactionRepository.class);
        ClockRepository clockRepository = ServiceLocator.getService(ClockRepository.class);
        DomainTransactionAssembler domainTransactionAssembler = new DomainTransactionAssembler(clockRepository,
                transactionRepository);

        return new TransactionService(transactionRepository, domainTransactionAssembler);
    }
}
