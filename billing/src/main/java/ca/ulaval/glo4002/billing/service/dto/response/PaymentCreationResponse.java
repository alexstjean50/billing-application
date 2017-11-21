package ca.ulaval.glo4002.billing.service.dto.response;

public class PaymentCreationResponse
{
    private static final String PAYMENT_TEMPLATE_URL = "/payments/%d";
    public final long id;
    public final String url;

    private PaymentCreationResponse(long id, String url)
    {
        this.id = id;
        this.url = url;
    }

    public static PaymentCreationResponse create(long id)
    {
        return new PaymentCreationResponse(id, String.format(PAYMENT_TEMPLATE_URL, id));
    }
}
