package ca.ulaval.glo4002.billing.persistence.repository;

public class ClientNotFoundException extends NotFoundException
{
    private static final String ENTITY_NAME = "client";
    private static final long serialVersionUID = -2185797008804857932L;

    public ClientNotFoundException(Throwable cause, String clientKey)
    {
        super(cause, ENTITY_NAME, clientKey);
    }
}
