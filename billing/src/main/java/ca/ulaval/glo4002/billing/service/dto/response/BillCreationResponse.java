package ca.ulaval.glo4002.billing.service.dto.response;

import java.math.BigDecimal;

public class BillCreationResponse
{
    private static final String BILL_TEMPLATE_URL = "/bills/%d";
    private long id;
    private BigDecimal total;
    private String dueTerm;
    private String url;

    public BillCreationResponse()
    {
    }

    public BillCreationResponse(long id, BigDecimal total, String dueTerm, String url)
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

    public void setId(long id)
    {
        this.id = id;
    }

    public void setTotal(BigDecimal total)
    {
        this.total = total;
    }

    public void setDueTerm(String dueTerm)
    {
        this.dueTerm = dueTerm;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public long getId()
    {
        return this.id;
    }

    public BigDecimal getTotal()
    {
        return this.total;
    }

    public String getDueTerm()
    {
        return this.dueTerm;
    }

    public String getUrl()
    {
        return this.url;
    }
}
