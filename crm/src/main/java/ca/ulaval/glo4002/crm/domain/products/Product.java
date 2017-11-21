package ca.ulaval.glo4002.crm.domain.products;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Product
{
    @Id
    @GeneratedValue
    private Integer id;
    @Column
    private String name;
    @Column
    private BigDecimal unitPrice;

    public Integer getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public BigDecimal getUnitPrice()
    {
        return unitPrice;
    }
}
