package ca.ulaval.glo4002.billing.resource.exceptionmapper;

import ca.ulaval.glo4002.billing.resource.InvalidClientIdException;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidClientIdExceptionMapper implements ExceptionMapper<InvalidClientIdException>
{
    private static final String EMPTY_JSON_VALUE = "{}";
    private final Logger logger;

    public InvalidClientIdExceptionMapper()
    {
        this(Log.getLogger(InvalidClientIdException.class));
    }

    private InvalidClientIdExceptionMapper(Logger logger)
    {
        this.logger = logger;
    }

    @Override
    public Response toResponse(InvalidClientIdException exception)
    {
        this.logger.info(exception);

        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON)
                .entity(EMPTY_JSON_VALUE)
                .build();
    }
}
