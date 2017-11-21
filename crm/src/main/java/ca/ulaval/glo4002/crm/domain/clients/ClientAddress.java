package ca.ulaval.glo4002.crm.domain.clients;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ClientAddress
{
    @Column
    private String city;
    @Column
    private String country;
    @Column
    private String number;
    @Column
    private String postalCode;
    @Column
    private String province;
    @Column
    private String street;
    @Column
    private String unit;

    public String getCity()
    {
        return city;
    }

    public String getCountry()
    {
        return country;
    }

    public String getNumber()
    {
        return number;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public String getProvince()
    {
        return province;
    }

    public String getStreet()
    {
        return street;
    }

    public String getUnit()
    {
        return unit;
    }
}
