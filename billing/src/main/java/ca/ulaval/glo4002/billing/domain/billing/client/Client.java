package ca.ulaval.glo4002.billing.domain.billing.client;

import java.time.Instant;

public class Client
{
    private final long id;
    private final Instant creationDate;
    private final DueTerm defaultTerm;

    public Client(long id, Instant creationDate, DueTerm defaultTerm)
    {
        this.id = id;
        this.defaultTerm = defaultTerm;
        this.creationDate = creationDate;
    }

    public long getId()
    {
        return this.id;
    }

    public Instant getCreationDate()
    {
        return this.creationDate;
    }

    public DueTerm getDefaultTerm()
    {
        return this.defaultTerm;
    }
}
