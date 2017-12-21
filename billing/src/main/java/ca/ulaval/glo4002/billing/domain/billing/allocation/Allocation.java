package ca.ulaval.glo4002.billing.domain.billing.allocation;

import ca.ulaval.glo4002.billing.domain.Money;
import ca.ulaval.glo4002.billing.persistence.identity.Identity;
import com.google.common.base.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

import java.time.Instant;

public class Allocation
{
    private final Identity allocationId;
    private final long billNumber;
    private final Instant creationDate;
    private final Money allocatedAmount;

    public Allocation(Identity allocationId, long billNumber, Money allocatedAmount,
                      Instant creationDate)
    {
        this.allocationId = allocationId;
        this.billNumber = billNumber;
        this.allocatedAmount = allocatedAmount;
        this.creationDate = creationDate;
    }

    public boolean isBillNumberDifferent(long billNumber)
    {
        return this.billNumber != billNumber;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(
                this.allocationId,
                this.billNumber,
                this.allocatedAmount,
                this.creationDate
        );
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || o.getClass() != this.getClass())
        {
            return false;
        }
        if (o == this)
        {
            return true;
        }

        Allocation other = (Allocation) o;
        return new EqualsBuilder().append(this.billNumber, other.billNumber)
                .append(this.allocatedAmount, other.allocatedAmount)
                .append(this.creationDate, other.creationDate)
                .isEquals();
    }

    public long getBillNumber()
    {
        return this.billNumber;
    }

    public Money getAllocatedAmount()
    {
        return this.allocatedAmount;
    }

    public Instant getCreationDate()
    {
        return this.creationDate;
    }

    public Identity getAllocationId()
    {
        return this.allocationId;
    }
}
