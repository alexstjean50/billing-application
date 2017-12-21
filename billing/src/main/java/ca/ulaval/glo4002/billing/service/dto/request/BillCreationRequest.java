package ca.ulaval.glo4002.billing.service.dto.request;

import ca.ulaval.glo4002.billing.service.dto.serializer.InstantDeserializer;
import ca.ulaval.glo4002.billing.service.dto.serializer.InstantSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public class BillCreationRequest
{
    @NotNull(message = "clientId cannot be null")
    private long clientId;
    @NotNull(message = "creationDate cannot be null")
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    private Instant creationDate;
    private String dueTerm;
    @Valid
    @NotNull(message = "items cannot be null")
    @JsonProperty("items")
    private List<ItemRequest> itemRequests;

    public BillCreationRequest()
    {
    }

    public BillCreationRequest(long clientId, Instant creationDate, String dueTerm, List<ItemRequest> itemRequests)
    {
        this.clientId = clientId;
        this.creationDate = creationDate;
        this.dueTerm = dueTerm;
        this.itemRequests = itemRequests;
    }

    public long getClientId()
    {
        return this.clientId;
    }

    public void setClientId(long clientId)
    {
        this.clientId = clientId;
    }

    public Instant getCreationDate()
    {
        return this.creationDate;
    }

    public void setCreationDate(Instant creationDate)
    {
        this.creationDate = creationDate;
    }

    public String getDueTerm()
    {
        return this.dueTerm;
    }

    public void setDueTerm(String dueTerm)
    {
        this.dueTerm = dueTerm;
    }

    public List<ItemRequest> getItemRequests()
    {
        return this.itemRequests;
    }

    public void setItemRequests(List<ItemRequest> itemRequests)
    {
        this.itemRequests = itemRequests;
    }
}
