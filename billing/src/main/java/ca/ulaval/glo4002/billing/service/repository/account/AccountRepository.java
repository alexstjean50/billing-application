package ca.ulaval.glo4002.billing.service.repository.account;

import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.domain.billing.bill.Bill;
import ca.ulaval.glo4002.billing.service.dto.request.BillStatusParameter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AccountRepository
{
    void save(Account account);

    List<Account> findAll();

    Account findByClientId(long clientId);

    Account findByBillNumber(long billNumber);

    Map<Long,List<Bill>> retrieveFilteredBillsOfClients(Optional<Long> clientId, Optional<BillStatusParameter> status);
}
