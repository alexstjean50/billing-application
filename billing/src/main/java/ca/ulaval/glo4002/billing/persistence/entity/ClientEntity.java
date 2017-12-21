package ca.ulaval.glo4002.billing.persistence.entity;

import ca.ulaval.glo4002.billing.domain.billing.client.DueTerm;
import ca.ulaval.glo4002.billing.service.dto.serializer.InstantDeserializer;
import ca.ulaval.glo4002.billing.service.dto.serializer.InstantSerializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientEntity
{
    private final long id;
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    private final Instant creationDate;
    private final DueTerm defaultTerm;

    @JsonCreator
    public ClientEntity(@JsonProperty("id") long id, @JsonProperty("creationDate") Instant creationDate,
                        @JsonProperty("defaultTerm") DueTerm defaultTerm)
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
