package ca.ulaval.glo4002.billing.resource.exceptionmapper;

import ca.ulaval.glo4002.billing.persistence.repository.NotFoundException;
import ca.ulaval.glo4002.billing.service.dto.response.ErrorResponse;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException>
{
    private static final String ERROR = "not found";
    private static final String DESCRIPTION_TEMPLATE = "%s %s not found";
    private final Logger logger;

    public NotFoundExceptionMapper()
    {
        this(Log.getLogger(NotFoundExceptionMapper.class));
    }

    private NotFoundExceptionMapper(Logger logger)
    {
        this.logger = logger;
    }

    @Override
    public Response toResponse(NotFoundException exception)
    {
        ErrorResponse response = new ErrorResponse(ERROR, String.format(DESCRIPTION_TEMPLATE, exception.entityName,
                exception.entityKey), exception.entityName);

        this.logger.info(exception);

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(response)
                .build();
    }
}
