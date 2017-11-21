package ca.ulaval.glo4002.billing.persistence.assembler.client;

import ca.ulaval.glo4002.billing.domain.billing.client.Client;
import ca.ulaval.glo4002.billing.persistence.entity.ClientEntity;

public class ClientAssembler
{
    public Client toDomainModel(ClientEntity clientEntity)
    {
        return new Client(clientEntity.getId(), clientEntity.getCreationDate(), clientEntity.getDefaultTerm());
    }
}
