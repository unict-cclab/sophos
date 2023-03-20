package it.unict;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.Map;

@RegisterRestClient(configKey = "telemetry")
public interface TelemetryService {

    @GET
    @Path("/metrics/nodes/available-cpu")
    Uni<Double> getNodeAvailableCpu(@QueryParam("node") String node);


    @GET
    @Path("/metrics/nodes/available-memory")
    Uni<Double> getNodeAvailableMemory(@QueryParam("node") String node);

    @GET
    @Path("/metrics/nodes/latencies")
    Uni<Map<String,Double>> getNodeLatencies(@QueryParam("node") String node);
}
