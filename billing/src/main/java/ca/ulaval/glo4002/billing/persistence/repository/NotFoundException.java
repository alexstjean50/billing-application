package ca.ulaval.glo4002.billing.persistence.repository;

public abstract class NotFoundException extends RuntimeException
{
    private static final long serialVersionUID = -8599115175131571766L;
    public final String entityName;
    public final String entityKey;

    public NotFoundException(String message, String entityName, String entityKey)
    {
        super(message);
        this.entityName = entityName;
        this.entityKey = entityKey;
    }

    public NotFoundException(Throwable cause, String entityName, String entityKey)
    {
        super(cause);
        this.entityName = entityName;
        this.entityKey = entityKey;
    }

    public NotFoundException(String message, Throwable cause, String entityName, String entityKey)
    {
        super(message, cause);
        this.entityName = entityName;
        this.entityKey = entityKey;
    }

    public NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
                             String entityName, String entityKey)
    {
        super(message, cause, enableSuppression, writableStackTrace);
        this.entityName = entityName;
        this.entityKey = entityKey;
    }
}
