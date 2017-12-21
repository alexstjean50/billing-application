package ca.ulaval.glo4002.billing.resource.exceptionmapper;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException>
{
    private final Logger logger;

    public ValidationExceptionMapper()
    {
        this(Log.getLogger(ValidationExceptionMapper.class));
    }

    private ValidationExceptionMapper(Logger logger)
    {
        this.logger = logger;
    }

    @Override
    public Response toResponse(ValidationException exception)
    {
        String errorMessage = generateErrorMessage((ConstraintViolationException) exception);
        return Response
                .status(Response.Status.BAD_REQUEST)
                .type(MediaType.TEXT_PLAIN)
                .entity(errorMessage)
                .build();
    }

    private String generateErrorMessage(ConstraintViolationException exception)
    {
        return exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors
                        .joining(", "));
    }
}