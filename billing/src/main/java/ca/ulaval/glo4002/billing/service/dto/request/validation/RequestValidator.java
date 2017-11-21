package ca.ulaval.glo4002.billing.service.dto.request.validation;

import ca.ulaval.glo4002.billing.service.dto.response.validation.ValidationErrorMessage;
import ca.ulaval.glo4002.billing.service.dto.response.validation.ValidationErrorResponse;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestValidator<T>
{
    private final Validator validator;
    private final T request;

    public RequestValidator(T request)
    {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        this.request = request;
    }

    public Response generateValidationErrorResponse()
    {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ValidationErrorResponse(generateErrorMessages()))
                .build();
    }

    private List<ValidationErrorMessage> generateErrorMessages()
    {
        Set<ConstraintViolation<T>> violations = this.validator.validate(this.request);

        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .distinct()
                .map(ValidationErrorMessage::new)
                .collect(Collectors.toList());
    }

    public boolean isRequestValid()
    {
        return generateErrorMessages().isEmpty();
    }
}
