package ca.ulaval.glo4002.billing.domain.billing.product;

import com.google.common.base.Objects;

import java.math.BigDecimal;

public class Product
{
    private final long id;
    private final String name;
    private final BigDecimal unitPrice;

    public Product(long id, String name, BigDecimal unitPrice)
    {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.id, this.name, this.unitPrice);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || this.getClass() != o.getClass())
        {
            return false;
        }

        Product product = (Product) o;

        return this.id == product.id;
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
