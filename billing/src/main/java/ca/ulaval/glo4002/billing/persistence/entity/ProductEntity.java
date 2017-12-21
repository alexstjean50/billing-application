package ca.ulaval.glo4002.billing.persistence.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductEntity
{
    private final long id;
    private final String name;
    private final BigDecimal unitPrice;

    @JsonCreator
    public ProductEntity(@JsonProperty("id") long id, @JsonProperty("name") String name,
                         @JsonProperty("unitPrice") BigDecimal unitPrice)
    {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
    }

    public long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public BigDecimal getUnitPrice()
    {
        return this.unitPrice;
    }
}
