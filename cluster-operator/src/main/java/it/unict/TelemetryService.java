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
    @Path("/metrics/nodes/cpu-usage")
    Uni<Double> getNodeCpuUsage(@QueryParam("node") String node, String rangeWidth);


    @GET
    @Path("/metrics/nodes/available-memory")
    Uni<Double> getNodeAvailableMemory(@QueryParam("node") String node, @QueryParam("range-width") String rangeWidth);

    @GET
    @Path("/metrics/nodes/latencies")
    Uni<Map<String,Double>> getNodeLatencies(@QueryParam("node") String node, @QueryParam("range-width") String rangeWidth);
}
