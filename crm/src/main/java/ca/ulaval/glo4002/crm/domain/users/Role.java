package ca.ulaval.glo4002.crm.domain.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Role
{
    @Id
    @GeneratedValue
    private Integer id;
    @Column
    private String name;

    public Integer getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
