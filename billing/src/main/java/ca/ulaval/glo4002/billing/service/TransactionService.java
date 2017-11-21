package ca.ulaval.glo4002.billing.service;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.transaction.OperationType;
import ca.ulaval.glo4002.billing.domain.billing.transaction.Transaction;
import ca.ulaval.glo4002.billing.domain.billing.transaction.TransactionType;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import ca.ulaval.glo4002.billing.service.dto.response.TransactionEntryResponse;
import ca.ulaval.glo4002.billing.service.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionService
{
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository)
    {
        this.transactionRepository = transactionRepository;
    }

    public void logPayment(long clientId, BigDecimal amount)
    {
        logTransaction(clientId, TransactionType.PAYMENT, OperationType.DEBIT, amount);
    }

    public void logInvoiceAcceptance(long clientId, BigDecimal amount)
    {
        logTransaction(clientId, TransactionType.INVOICE, OperationType.CREDIT, amount);
    }

    public void logInvoiceCancellation(long clientId, BigDecimal amount)
    {
        logTransaction(clientId, TransactionType.INVOICE_CANCELLED, OperationType.DEBIT, amount);
    }

    public void logDiscount(long clientId, BigDecimal amount)
    {
        logTransaction(clientId, TransactionType.REBATE, OperationType.DEBIT, amount);
    }

    private void logTransaction(long clientId, TransactionType transactionType, OperationType operationType, BigDecimal
            amount)
    {
        Instant date = Instant.now();

        BigDecimal amountAppliedToLedger = operationType == OperationType.CREDIT ? amount.multiply(new BigDecimal(-1)) :
                amount;

        Transaction transaction = new Transaction(Identity.EMPTY, date, transactionType, clientId, operationType,
                Money.valueOf(amount), calculateCurrentLedgerBalance(Money.valueOf(amountAppliedToLedger)));

        this.transactionRepository.save(transaction);
    }

    private Money calculateCurrentLedgerBalance(Money amount)
    {
        Money currentLedgerBalance = this.transactionRepository.retrieveCurrentLedgerBalance();
        return currentLedgerBalance.add(amount);
    }

    public List<TransactionEntryResponse> retrieveTransactions(Optional<String> optionalStartMonth,
                                                               Optional<String> optionalEndMonth,
                                                               Optional<Long> optionalYear)
    {
        List<Transaction> transactions = this.transactionRepository.findAll();

        if (optionalYear.isPresent())
        {
            if (optionalStartMonth.isPresent())
            {
                int startMonth = Integer.valueOf(optionalStartMonth.get());
                transactions = filterTransactionsByStartMonth(transactions, startMonth);
            }

            if (optionalEndMonth.isPresent())
            {
                int endMonth = Integer.valueOf(optionalEndMonth.get());
                transactions = filterTransactionsByEndMonth(transactions, endMonth);
            }

            transactions = filterTransactionsByYear(optionalYear.get(), transactions);
        }
        else
        {
            //TODO À ajuster.
            throw new RuntimeException();
        }

        return transactions.stream()
                .map(TransactionEntryResponse::create)
                .collect(Collectors.toList());
    }

    private List<Transaction> filterTransactionsByStartMonth(List<Transaction> transactions, int startMonth)
    {
        transactions = transactions.stream()
                .filter(transaction -> LocalDateTime.ofInstant(transaction.getDate(), ZoneId.systemDefault())
                        .getMonthValue() >= startMonth)
                .collect(Collectors.toList());
        return transactions;
    }

    private List<Transaction> filterTransactionsByYear(long year, List<Transaction> transactions)
    {
        transactions = transactions.stream()
                .filter(transaction -> LocalDateTime.ofInstant(transaction.getDate(), ZoneId.systemDefault())
                        .getYear() == year)
                .collect(Collectors.toList());
        return transactions;
    }

    private List<Transaction> filterTransactionsByEndMonth(List<Transaction> transactions, int endMonth)
    {
        transactions = transactions.stream()
                .filter(transaction -> LocalDateTime.ofInstant(transaction.getDate(), ZoneId.systemDefault())
                        .getMonthValue() <= endMonth)
                .collect(Collectors.toList());
        return transactions;
    }
}
