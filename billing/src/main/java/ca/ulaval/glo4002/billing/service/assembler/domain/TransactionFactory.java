package ca.ulaval.glo4002.billing.service.assembler.domain;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.domain.billing.transaction.OperationType;
import ca.ulaval.glo4002.billing.domain.billing.transaction.Transaction;
import ca.ulaval.glo4002.billing.domain.billing.transaction.TransactionType;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import ca.ulaval.glo4002.billing.service.repository.TransactionRepository;
import ca.ulaval.glo4002.billing.service.repository.clock.ClockRepository;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionFactory
{
    private ClockRepository clockRepository;
    private TransactionRepository transactionRepository;

    public TransactionFactory(ClockRepository clockRepository, TransactionRepository transactionRepository)
    {
        this.clockRepository = clockRepository;
        this.transactionRepository = transactionRepository;
    }

    public Transaction toNewTransaction(long clientId, BigDecimal amount, TransactionType transactionType)
    {
        Instant date = this.clockRepository.retrieveCurrentTime();

        OperationType operationType = retrieveOperationType(transactionType);

        BigDecimal amountAppliedToLedger = operationType == OperationType.CREDIT ? amount.negate() : amount;
        Money balance = calculateCurrentLedgerBalance(Money.valueOf(amountAppliedToLedger));
        return new Transaction(Identity.EMPTY, date, transactionType, clientId, operationType,
                Money.valueOf(amount), balance);
    }

    private Money calculateCurrentLedgerBalance(Money amount)
    {
        Money currentLedgerBalance = this.transactionRepository.retrieveCurrentLedgerBalance();
        return currentLedgerBalance.add(amount);
    }

    private OperationType retrieveOperationType(TransactionType transactionType)
    {
        switch (transactionType)
        {
            case INVOICE:
                return OperationType.CREDIT;
            case INVOICE_CANCELLED:
            case PAYMENT:
                return OperationType.DEBIT;
        }

        throw new UnsupportedOperationException();
    }
}
