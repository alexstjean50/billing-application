package ca.ulaval.glo4002.billing.resource.exceptionmapper;

import ca.ulaval.glo4002.billing.domain.billing.bill.InvalidDiscountAmountException;
import ca.ulaval.glo4002.billing.service.dto.response.ErrorResponse;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidDiscountAmountExceptionMapper implements ExceptionMapper<InvalidDiscountAmountException>
{
    private static final String ERROR = "validation";
    private static final String DESCRIPTION = "rebate can't be higher than total of the invoice";
    private static final String ENTITY = "amount";
    private final Logger logger;

    public InvalidDiscountAmountExceptionMapper()
    {
        this(Log.getLogger(InvalidDiscountAmountException.class));
    }

    private InvalidDiscountAmountExceptionMapper(Logger logger)
    {
        this.logger = logger;
    }

    @Override
    public Response toResponse(InvalidDiscountAmountException exception)
    {
        ErrorResponse response = new ErrorResponse(ERROR, DESCRIPTION, ENTITY);

        this.logger.info(exception);

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(response)
                .build();
    }
}
