package ca.ulaval.glo4002.billing.domain.billing.payment;

import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import com.google.common.base.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class PaymentMethod
{
    public final Identity identity;
    public final String bankAccount;
    public final PaymentMethodSource source;

    public PaymentMethod(Identity identity, String bankAccount, PaymentMethodSource source)
    {
        this.identity = identity;
        this.bankAccount = bankAccount;
        this.source = source;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.bankAccount, this.source);
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

        PaymentMethod other = (PaymentMethod) obj;

        return new EqualsBuilder().append(this.bankAccount, other.bankAccount)
                .append(this.source, other.source)
                .isEquals();
    }
}
