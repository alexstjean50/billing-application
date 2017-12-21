package ca.ulaval.glo4002.billing.service.assembler.domain;

import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.domain.billing.client.Client;

public class DomainAccountAssembler
{
    public Account create(Client client)
    {
        return Account.create(client);
    }
}
