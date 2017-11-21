package ca.ulaval.glo4002.billing.domain.billing.account;

import ca.ulaval.glo4002.billing.domain.billing.client.Client;

public class AccountFactory
{
    public Account create(Client client)
    {
        return Account.create(client);
    }
}
