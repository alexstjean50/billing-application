package ca.ulaval.glo4002.billing.service.repository.client;

import ca.ulaval.glo4002.billing.domain.billing.client.Client;

public interface ClientRepository
{
    Client findById(long id);
}
