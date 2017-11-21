package ca.ulaval.glo4002.crm;

import ca.ulaval.glo4002.crm.domain.clients.Client;
import ca.ulaval.glo4002.crm.domain.products.Product;
import ca.ulaval.glo4002.crm.domain.users.Role;
import ca.ulaval.glo4002.crm.domain.users.User;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.stereotype.Component;

@Component
public class ExposeEntityIdRestMvcConfiguration extends RepositoryRestConfigurerAdapter
{
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config)
    {
        config.exposeIdsFor(Client.class, User.class, Role.class, Product.class);
    }
}