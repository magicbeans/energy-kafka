package uk.me.ajv.energykafka.resources;

import com.codahale.metrics.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import uk.me.ajv.energykafka.api.DeviceDataDto;
import uk.me.ajv.energykafka.model.DeviceData;
import uk.me.ajv.energykafka.services.DeviceDataIngestService;
import uk.me.ajv.energykafka.services.DeviceDataService;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@Path("/devices")
@Produces(MediaType.APPLICATION_JSON)
public class DeviceResource {

    final DeviceDataIngestService deviceDataIngestService;
    final DeviceDataService deviceDataService;

    public DeviceResource(DeviceDataIngestService deviceDataIngestService, DeviceDataService deviceDataService) {
        this.deviceDataIngestService = deviceDataIngestService;
        this.deviceDataService = deviceDataService;
    }

    @GET
    @Timed
    @Path("{id}")
    public Response findLatestDeviceUpdate(@PathParam("id") String id) {
        Optional<DeviceData> maybeData = deviceDataService.findLatestDeviceUpdate(id);
        return maybeData.isPresent() ? Response.ok(maybeData.get().toDto()).build() : Response.noContent().build();
    }

    @POST
    @Timed
    @Path("{id}")
    public Response recordEvent(@PathParam("id") String id, @Valid DeviceDataDto data) throws InterruptedException, ExecutionException, TimeoutException {
        log.info("Data received via http resource: " + data);
        deviceDataIngestService.handleUpdateFromDevice(DeviceData.fromDeviceDataDto(data));
        return Response.ok().build();
    }
}