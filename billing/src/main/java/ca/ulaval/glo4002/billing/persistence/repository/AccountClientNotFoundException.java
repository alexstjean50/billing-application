package ca.ulaval.glo4002.billing.persistence.repository;

public class AccountClientNotFoundException extends NotFoundException
{
    private static final String ENTITY_NAME = "client account";
    private static final long serialVersionUID = -4338695842680742728L;

    public AccountClientNotFoundException(String accountKey)
    {
        super(ENTITY_NAME, accountKey);
    }

    public AccountClientNotFoundException(Throwable cause, String accountKey)
    {
        super(cause, ENTITY_NAME, accountKey);
    }
}
