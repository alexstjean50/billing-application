package ca.ulaval.glo4002.billing.service.dto.response.validation;

import java.util.List;

public class ValidationErrorResponse
{
    private final List<ValidationErrorMessage> errors;

    public ValidationErrorResponse(List<ValidationErrorMessage> errors)
    {
        this.errors = errors;
    }
}
