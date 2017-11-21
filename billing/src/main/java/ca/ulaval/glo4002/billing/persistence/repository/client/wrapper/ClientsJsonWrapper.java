package ca.ulaval.glo4002.billing.persistence.repository.client.wrapper;

import ca.ulaval.glo4002.billing.persistence.entity.ClientEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ClientsJsonWrapper
{
    private final List<ClientEntity> clients;

    @JsonCreator
    public ClientsJsonWrapper(@JsonProperty("clients") List<ClientEntity> clients)
    {
        this.clients = clients;
    }

    public List<ClientEntity> getClients()
    {
        return this.clients;
    }
}