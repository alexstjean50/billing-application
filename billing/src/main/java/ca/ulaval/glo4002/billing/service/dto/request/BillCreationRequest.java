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
    public long clientId;
    @NotNull(message = "creationDate cannot be null")
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    public Instant creationDate;
    public String dueTerm;
    @Valid
    @NotNull(message = "items cannot be null")
    @JsonProperty("items")
    public List<ItemRequest> itemRequests;

    public static BillCreationRequest create(long clientId, Instant creationDate, String dueTerm, List<ItemRequest>
            itemRequests)
    {
        BillCreationRequest billCreationRequest = new BillCreationRequest();
        billCreationRequest.clientId = clientId;
        billCreationRequest.creationDate = creationDate;
        billCreationRequest.dueTerm = dueTerm;
        billCreationRequest.itemRequests = itemRequests;
        return billCreationRequest;
    }
}
