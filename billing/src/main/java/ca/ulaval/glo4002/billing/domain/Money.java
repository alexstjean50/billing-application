package ca.ulaval.glo4002.billing.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.math.BigDecimal;

public class Money
{
    public static final Money ZERO = valueOf(0);
    private final BigDecimal amount;

    private Money(BigDecimal amount)
    {
        this.amount = amount;
    }

    public static Money valueOf(long value)
    {
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money valueOf(double value)
    {
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money valueOf(BigDecimal value)
    {
        return new Money(value);
    }

    public BigDecimal asBigDecimal()
    {
        return this.amount;
    }

    public Money multiply(BigDecimal multiplier)
    {
        return valueOf(this.amount.multiply(multiplier));
    }

    public Money add(Money money)
    {
        return valueOf(this.amount.add(money.amount));
    }

    public Money subtract(Money money)
    {
        return valueOf(this.amount.subtract(money.amount));
    }

    public boolean isGreaterThan(Money money)
    {
        return this.amount.compareTo(money.amount) > 0;
    }

    public boolean isGreaterOrEqualTo(Money money)
    {
        return this.amount.compareTo(money.amount) >= 0;
    }

    public Money max(Money money)
    {
        return valueOf(this.amount.max(money.amount));
    }

    public Money min(Money money)
    {
        return valueOf(this.amount.min(money.amount));
    }

    public boolean equals(BigDecimal amount)
    {
        return this.equals(valueOf(amount));
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

        Money other = (Money) obj;
        return new EqualsBuilder().append(this.amount, other.amount)
                .isEquals();
    }
}
