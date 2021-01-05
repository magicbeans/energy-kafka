package uk.me.ajv.energykafka.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.me.ajv.energykafka.api.DeviceData;
import uk.me.ajv.energykafka.api.Saying;
import uk.me.ajv.energykafka.filters.LoggingFilter;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {
    private static final Logger log = LoggerFactory.getLogger(HelloWorldResource.class);

    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public HelloWorldResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
        final String value = String.format(template, name.orElse(defaultName));
        return new Saying(counter.incrementAndGet(), value);
    }

    @POST
    @Timed
    @Path("{id}")
    public Response recordEvent(@PathParam("id") String id, String deviceData) {
        log.info("Device data received for: " + id);
        ObjectMapper mapper = Jackson.newObjectMapper();

        Arrays.stream(deviceData.split("\n")).forEach(line -> {
            try {
                DeviceData data = mapper.readValue(line, DeviceData.class);
                log.info("DATA *** " + data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return Response.ok().build();
    }
}