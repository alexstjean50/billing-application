package ca.ulaval.glo4002.crm.repositories;

import ca.ulaval.glo4002.crm.domain.products.Product;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ProductRepository extends Repository<Product, Integer>
{
    Product findOne(Integer id);
}
