package ca.ulaval.glo4002.crm.repositories;

import ca.ulaval.glo4002.crm.domain.users.User;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends Repository<User, Integer>
{
    User findOne(Integer id);
}
