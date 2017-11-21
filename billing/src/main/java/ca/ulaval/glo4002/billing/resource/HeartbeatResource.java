package ca.ulaval.glo4002.billing.resource;

import ca.ulaval.glo4002.billing.domain.heartbeat.Heartbeat;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/heartbeat")
@Produces(MediaType.APPLICATION_JSON)
public class HeartbeatResource
{
    @GET
    public Response getHeartbeat(@QueryParam("token") String token)
    {
        return Response.ok()
                .entity(new Heartbeat(token))
                .build();
    }
}
