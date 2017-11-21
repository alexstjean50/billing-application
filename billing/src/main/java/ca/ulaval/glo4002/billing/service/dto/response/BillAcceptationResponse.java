package ca.ulaval.glo4002.billing.service.dto.response;

import ca.ulaval.glo4002.billing.domain.billing.client.DueTerm;
import ca.ulaval.glo4002.billing.service.dto.serializer.InstantDeserializer;
import ca.ulaval.glo4002.billing.service.dto.serializer.InstantSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Instant;

public class BillAcceptationResponse
{
    private static final String BILL_TEMPLATE_URL = "/bills/%d";
    public final long id;
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    public final Instant effectiveDate;
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    public final Instant expectedPayment;
    public final DueTerm dueTerm;
    public final String url;

    private BillAcceptationResponse(long id, Instant effectiveDate, Instant expectedPayment, DueTerm dueTerm,
                                    String url)
    {
        this.id = id;
        this.effectiveDate = effectiveDate;
        this.expectedPayment = expectedPayment;
        this.dueTerm = dueTerm;
        this.url = url;
    }

    public static BillAcceptationResponse create(long id, Instant effectiveDate, Instant expectedPayment,
                                                 DueTerm dueTerm)
    {
        return new BillAcceptationResponse(id, effectiveDate, expectedPayment, dueTerm,
                String.format(BILL_TEMPLATE_URL, id));
    }
}
