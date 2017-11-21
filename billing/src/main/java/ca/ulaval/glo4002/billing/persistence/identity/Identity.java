package ca.ulaval.glo4002.billing.persistence.identity;

import com.google.common.base.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class Identity
{
    public static final Identity EMPTY = new Identity(0);
    private final long id;

    public Identity(long id)
    {
        this.id = id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.id);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || obj.getClass() != this.getClass())
        {
            return false;
        }
        if (obj == this)
        {
            return true;
        }

        Identity other = (Identity) obj;
        return new EqualsBuilder().append(this.id, other.id)
                .isEquals();
    }

    public long getId()
    {
        return this.id;
    }
}