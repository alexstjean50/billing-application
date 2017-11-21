package ca.ulaval.glo4002.billing.persistence.repository.client.wrapper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientsJsonEmbeddedWrapper
{
    private final ClientsJsonWrapper clientsJsonWrapper;

    @JsonCreator
    public ClientsJsonEmbeddedWrapper(@JsonProperty("_embedded") ClientsJsonWrapper clientsJsonWrapper)
    {
        this.clientsJsonWrapper = clientsJsonWrapper;
    }

    public ClientsJsonWrapper getClientsJsonWrapper()
    {
        return this.clientsJsonWrapper;
    }
}