package ca.ulaval.glo4002.billing.service.dto.response.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class ValidationErrorMessage
{
    @JsonProperty("validationError")
    public final String message;

    public ValidationErrorMessage(String message)
    {
        this.message = message;
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.message);
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

        ValidationErrorMessage other = (ValidationErrorMessage) obj;
        return new EqualsBuilder().append(this.message, other.message)
                .isEquals();
    }
}
