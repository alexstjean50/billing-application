package ca.ulaval.glo4002.billing.persistence.repository;

public class ProductNotFoundException extends NotFoundException
{
    private static final String ENTITY_NAME = "product";
    private static final long serialVersionUID = -3232080329938798360L;

    public ProductNotFoundException(Throwable cause, String productKey)
    {
        super(cause, ENTITY_NAME, productKey);
    }
}
