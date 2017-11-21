package ca.ulaval.glo4002.crm.repositories;

import ca.ulaval.glo4002.crm.domain.clients.Client;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClientRepository extends Repository<Client, Integer>
{
    Client findOne(Integer id);

    Iterable<Client> findAll();

    Iterable<Client> findAll(Iterable<Integer> ids);
}
