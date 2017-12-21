package ca.ulaval.glo4002.crm;

import ca.ulaval.glo4002.billing.persistence.entity.ClientEntity;
import ca.ulaval.glo4002.billing.persistence.entity.ProductEntity;
import ca.ulaval.glo4002.commons.RestTestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CrmIntegrationRestTest extends RestTestBase
{
    private static final String CRM_API_URL = "http://localhost:8080";
    private static final int PRODUCT_ID = 1;
    private static final int CLIENT_ID = 1;

    @Test
    public void givenAClientId_whenRetrievingClient_thenShouldReturnCorrespondingClient()
    {
        ClientEntity clientEntity = givenBaseRequest().baseUri(CRM_API_URL)
                .get("/clients/{id}", buildPathParams("id", CLIENT_ID))
                .as(ClientEntity.class);

        assertEquals(CLIENT_ID, clientEntity.getId());
    }

    @Test
    public void givenAProductId_whenRetrievingProduct_thenShouldReturnCorrespondingProduct()
    {
        ProductEntity productEntity = givenBaseRequest().baseUri(CRM_API_URL)
                .get("/products/{id}", buildPathParams("id", PRODUCT_ID))
                .as(ProductEntity.class);

        assertEquals(PRODUCT_ID, productEntity.getId());
    }
}
