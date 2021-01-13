package uk.me.ajv.energykafka.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Provider
public class LoggingFilter implements ContainerRequestFilter {
    static Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String text = new BufferedReader(
                new InputStreamReader(requestContext.getEntityStream(), StandardCharsets.UTF_8)).lines()
                .collect(Collectors.joining("\n"));
        log.info(text);
    }
}