package ca.ulaval.glo4002.billing.persistence.repository.client;

import ca.ulaval.glo4002.billing.domain.billing.client.Client;
import ca.ulaval.glo4002.billing.persistence.assembler.client.ClientAssembler;
import ca.ulaval.glo4002.billing.persistence.entity.ClientEntity;
import ca.ulaval.glo4002.billing.persistence.repository.ClientNotFoundException;
import ca.ulaval.glo4002.billing.persistence.repository.client.wrapper.ClientsJsonEmbeddedWrapper;
import ca.ulaval.glo4002.billing.service.repository.client.ClientRepository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class ClientRestRepository implements ClientRepository
{
    private static final String CRM_CLIENT_API_URL = "http://localhost:8080/clients";
    private final RestTemplate restTemplate;
    private final ClientAssembler clientAssembler;

    public ClientRestRepository(RestTemplate restTemplate,
                                ClientAssembler clientAssembler)
    {
        this.restTemplate = restTemplate;
        this.clientAssembler = clientAssembler;
    }

    @Override
    public Client findById(long id)
    {
        try
        {
            String requestURL = CRM_CLIENT_API_URL + "/" + id;
            ClientEntity clientEntity = this.restTemplate.getForObject(requestURL, ClientEntity.class);

            return this.clientAssembler.toDomainModel(clientEntity);
        }
        catch (RestClientException exception)
        {
            throw new ClientNotFoundException(exception, String.valueOf(id));
        }
    }

    @Override
    public List<Client> findAll()
    {
        return this.restTemplate.getForObject(CRM_CLIENT_API_URL, ClientsJsonEmbeddedWrapper.class)
                .getClientsJsonWrapper()
                .getClients()
                .stream()
                .map(this.clientAssembler::toDomainModel)
                .collect(Collectors.toList());
    }
}
