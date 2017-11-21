package ca.ulaval.glo4002.billing.resource.exceptionmapper;

import ca.ulaval.glo4002.billing.domain.billing.bill.BillAlreadyAcceptedException;
import ca.ulaval.glo4002.billing.service.dto.response.ErrorResponse;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BillAlreadyAcceptedExceptionMapper implements ExceptionMapper<BillAlreadyAcceptedException>
{
    private static final String ERROR = "wrong status";
    private static final String DESCRIPTION = "Invoice already accepted";
    private static final String ENTITY = "invoice";
    private final Logger logger;

    public BillAlreadyAcceptedExceptionMapper()
    {
        this(Log.getLogger(BillAlreadyAcceptedException.class));
    }

    private BillAlreadyAcceptedExceptionMapper(Logger logger)
    {
        this.logger = logger;
    }

    @Override
    public Response toResponse(BillAlreadyAcceptedException exception)
    {
        ErrorResponse response = new ErrorResponse(ERROR, DESCRIPTION, ENTITY);

        this.logger.info(exception);

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(response)
                .build();
    }
}
