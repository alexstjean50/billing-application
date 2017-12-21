package ca.ulaval.glo4002.billing.service;

import ca.ulaval.glo4002.billing.domain.billing.transaction.Transaction;
import ca.ulaval.glo4002.billing.domain.billing.transaction.TransactionType;
import ca.ulaval.glo4002.billing.service.assembler.domain.TransactionFactory;
import ca.ulaval.glo4002.billing.service.dto.response.TransactionEntryResponse;
import ca.ulaval.glo4002.billing.service.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionService
{
    private final TransactionRepository transactionRepository;
    private final TransactionFactory transactionFactory;

    public TransactionService(TransactionRepository transactionRepository, TransactionFactory transactionFactory)
    {
        this.transactionRepository = transactionRepository;
        this.transactionFactory = transactionFactory;
    }

    public void logTransaction(long clientId, BigDecimal amount, TransactionType transactionType)
    {
        Transaction transaction = this.transactionFactory.toNewTransaction(clientId, amount, transactionType);

        this.transactionRepository.save(transaction);
    }

    public List<TransactionEntryResponse> retrieveTransactions(Optional<String> optionalStartMonth,
                                                               Optional<String> optionalEndMonth,
                                                               Optional<Long> optionalYear)
    {
        List<Transaction> transactions = this.transactionRepository.findByFilter(optionalStartMonth,
                optionalEndMonth, optionalYear);

        return transactions.stream()
                .map(TransactionEntryResponse::create)
                .collect(Collectors.toList());
    }
}
