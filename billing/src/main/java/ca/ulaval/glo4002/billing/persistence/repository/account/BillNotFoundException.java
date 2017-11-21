package ca.ulaval.glo4002.billing.persistence.repository.account;

import ca.ulaval.glo4002.billing.persistence.repository.NotFoundException;

public class BillNotFoundException extends NotFoundException
{
    private static final String ENTITY_NAME = "account";
    private static final long serialVersionUID = 6879631529058125382L;

    public BillNotFoundException(String message, String entityKey)
    {
        super(message, ENTITY_NAME, entityKey);
    }

    public BillNotFoundException(Throwable cause, String entityKey)
    {
        super(cause, ENTITY_NAME, entityKey);
    }

    public BillNotFoundException(String message, Throwable cause, String entityKey)
    {
        super(message, cause, ENTITY_NAME, entityKey);
    }

    public BillNotFoundException(String message, Throwable cause, boolean enableSuppression,
                                 boolean writableStackTrace, String entityKey)
    {
        super(message, cause, enableSuppression, writableStackTrace, ENTITY_NAME, entityKey);
    }
}
