package ca.ulaval.glo4002.billing.service.retriever;

import ca.ulaval.glo4002.billing.domain.billing.account.Account;
import ca.ulaval.glo4002.billing.service.assembler.domain.DomainAccountAssembler;
import ca.ulaval.glo4002.billing.domain.billing.client.Client;
import ca.ulaval.glo4002.billing.persistence.repository.AccountClientNotFoundException;
import ca.ulaval.glo4002.billing.service.repository.account.AccountRepository;
import ca.ulaval.glo4002.billing.service.repository.client.ClientRepository;

public class AccountRetriever
{
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final DomainAccountAssembler domainAccountAssembler;

    public AccountRetriever(ClientRepository clientRepository, AccountRepository accountRepository,
                            DomainAccountAssembler

            domainAccountAssembler)
    {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.domainAccountAssembler = domainAccountAssembler;
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
            account = this.domainAccountAssembler.create(client);
        }
        return account;
    }
}
