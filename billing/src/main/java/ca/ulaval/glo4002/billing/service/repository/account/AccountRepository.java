package ca.ulaval.glo4002.billing.service.repository.account;

import ca.ulaval.glo4002.billing.domain.billing.account.Account;

public interface AccountRepository
{
    void save(Account account);

    Account findByClientId(long clientId);

    Account findByBillNumber(long billNumber);
}
