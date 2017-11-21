package ca.ulaval.glo4002.billing.service.dto.response;

import java.math.BigDecimal;

public class BillCreationResponse
{
    private static final String BILL_TEMPLATE_URL = "/bills/%d";
    public final long id;
    public final BigDecimal total;
    public final String dueTerm;
    public final String url;

    private BillCreationResponse(long id, BigDecimal total, String dueTerm, String url)
    {
        this.id = id;
        this.total = total;
        this.dueTerm = dueTerm;
        this.url = url;
    }

    public static BillCreationResponse create(long id, BigDecimal total, String dueTerm)
    {
        return new BillCreationResponse(id, total, dueTerm, String.format(BILL_TEMPLATE_URL, id));
    }
}
