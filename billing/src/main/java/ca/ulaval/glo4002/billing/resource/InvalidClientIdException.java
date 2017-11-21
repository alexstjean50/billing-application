package ca.ulaval.glo4002.billing.resource;

import ca.ulaval.glo4002.billing.persistence.repository.NotFoundException;

public class InvalidClientIdException extends NotFoundException
{
    private static final String ENTITY_NAME = "client";
    private static final long serialVersionUID = 5818697488436538253L;

    public InvalidClientIdException(Throwable cause, String clientKey)
    {
        super(cause, ENTITY_NAME, clientKey);
    }
}
