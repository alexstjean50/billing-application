package ca.ulaval.glo4002.billing.resource.exceptionmapper;

import ca.ulaval.glo4002.billing.persistence.repository.account.BillNotFoundException;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BillNotFoundExceptionMapper implements ExceptionMapper<BillNotFoundException>
{
    private static final String EMPTY_JSON_VALUE = "{}";
    private final Logger logger;

    public BillNotFoundExceptionMapper()
    {
        this(Log.getLogger(BillNotFoundException.class));
    }

    private BillNotFoundExceptionMapper(Logger logger)
    {
        this.logger = logger;
    }

    @Override
    public Response toResponse(BillNotFoundException exception)
    {
        this.logger.info(exception);

        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(EMPTY_JSON_VALUE)
                .build();
    }
}
