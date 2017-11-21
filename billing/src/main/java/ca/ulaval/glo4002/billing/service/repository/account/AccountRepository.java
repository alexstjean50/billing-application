package ca.ulaval.glo4002.billing.service.repository.account;

import ca.ulaval.glo4002.billing.domain.billing.account.Account;

import java.util.List;

public interface AccountRepository
{
    void save(Account account);

    List<Account> findAll();

    Account findByClientId(long clientId);

    Account findByBillNumber(long billNumber);
}
