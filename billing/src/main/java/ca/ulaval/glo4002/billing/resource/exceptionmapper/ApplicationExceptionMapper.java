package ca.ulaval.glo4002.billing.resource.exceptionmapper;

import ca.ulaval.glo4002.billing.service.dto.response.ErrorResponse;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<Exception>
{
    private static final String ERROR = "unnamed";
    private static final String DESCRIPTION_TEMPLATE = "%s";
    private static final String ENTITY_NAME = "unnamed";
    private final Logger logger;

    public ApplicationExceptionMapper()
    {
        this(Log.getLogger(ApplicationExceptionMapper.class));
    }

    private ApplicationExceptionMapper(Logger logger)
    {
        this.logger = logger;
    }

    @Override
    public Response toResponse(Exception exception)
    {
        ErrorResponse response = new ErrorResponse(ERROR, String.format(DESCRIPTION_TEMPLATE, exception
                .getMessage()), ENTITY_NAME);

        this.logger.warn(exception);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(response)
                .build();
    }
}
