package ca.ulaval.glo4002.billing.resource.exceptionmapper;

import ca.ulaval.glo4002.billing.service.dto.response.ErrorResponse;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

@Provider
public class JsonMappingExceptionMapper implements ExceptionMapper<JsonMappingException>
{
    private static final String ERROR = "unnamed";
    private static final String DESCRIPTION_TEMPLATE = "the following field(s) could not be parsed : %s";
    private static final String ENTITY_NAME = "unnamed";
    private final Logger logger;

    public JsonMappingExceptionMapper()
    {
        this(Log.getLogger(JsonMappingExceptionMapper.class));
    }

    private JsonMappingExceptionMapper(Logger logger)
    {
        this.logger = logger;
    }

    @Override
    public Response toResponse(JsonMappingException exception)
    {
        String fieldNames = exception.getPath()
                .stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining(", "));

        ErrorResponse response = new ErrorResponse(ERROR, String.format(DESCRIPTION_TEMPLATE, fieldNames),
                ENTITY_NAME);

        this.logger.info(exception);

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(response)
                .build();
    }
}
