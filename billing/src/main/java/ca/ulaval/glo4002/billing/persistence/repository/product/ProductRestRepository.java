package ca.ulaval.glo4002.billing.persistence.repository.product;

import ca.ulaval.glo4002.billing.domain.billing.product.Product;
import ca.ulaval.glo4002.billing.persistence.assembler.product.ProductAssembler;
import ca.ulaval.glo4002.billing.persistence.entity.ProductEntity;
import ca.ulaval.glo4002.billing.persistence.repository.ProductNotFoundException;
import ca.ulaval.glo4002.billing.service.repository.product.ProductRepository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

public class ProductRestRepository implements ProductRepository
{
    private static final String CRM_PRODUCT_API_URL = "http://localhost:8080/products";
    private final RestTemplate restTemplate;
    private final ProductAssembler productAssembler;

    public ProductRestRepository(RestTemplate restTemplate, ProductAssembler productAssembler)
    {
        this.restTemplate = restTemplate;
        this.productAssembler = productAssembler;
    }

    @Override
    public List<Product> findAll(List<Long> ids)
    {
        return ids.stream()
                .map(this::findById)
                .collect(Collectors.toList());
    }

    private Product findById(long id)
    {
        try
        {
            String requestURL = CRM_PRODUCT_API_URL + "/" + id;
            ProductEntity productEntity = this.restTemplate.getForObject(requestURL, ProductEntity.class);
            return this.productAssembler.toDomainModel(productEntity);
        }
        catch (RestClientException exception)
        {
            throw new ProductNotFoundException(exception, String.valueOf(id));
        }
    }
}
