package ca.ulaval.glo4002.billing.service;

import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.domain.billing.account.AccountFactory;
import ca.ulaval.glo4002.billing.domain.billing.client.Client;
import ca.ulaval.glo4002.billing.persistence.repository.AccountClientNotFoundException;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.client.ClientRepository;

public class AccountRetriever
{
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final AccountFactory accountFactory;

    public AccountRetriever(ClientRepository clientRepository, AccountRepository accountRepository, AccountFactory
            accountFactory)
    {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.accountFactory = accountFactory;
    }

    public Account retrieveClientAccount(long clientId)
    {
        Account account;
        try
        {
            account = this.accountRepository.findByClientId(clientId);
        }
        catch (AccountClientNotFoundException exception)
        {
            Client client = this.clientRepository.findById(clientId);
            account = this.accountFactory.create(client);
        }
        return account;
    }
}
