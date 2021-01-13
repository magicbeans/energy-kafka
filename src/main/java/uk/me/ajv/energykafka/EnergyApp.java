package uk.me.ajv.energykafka;

import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.jdbi3.bundles.JdbiExceptionsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;
import uk.me.ajv.energykafka.adapters.DeviceDataMessageProducer;
import uk.me.ajv.energykafka.commands.ReadDeviceDataCommand;
import uk.me.ajv.energykafka.health.BasicHealthCheck;
import uk.me.ajv.energykafka.repository.DeviceDataDao;
import uk.me.ajv.energykafka.resources.DeviceResource;
import uk.me.ajv.energykafka.services.DeviceDataIngestService;
import uk.me.ajv.energykafka.services.DeviceDataService;

public class EnergyApp extends Application<EnergyAppConfiguration> {
    public static void main(String[] args) throws Exception {
        new EnergyApp().run(args);
    }

    @Override
    public String getName() {
        return "energy-kafka";
    }

    @Override
    public void initialize(Bootstrap<EnergyAppConfiguration> bootstrap) {
        bootstrap.addBundle(new JdbiExceptionsBundle());
        bootstrap.addCommand(new ReadDeviceDataCommand(bootstrap.getApplication()));
    }

    @Override
    public void run(EnergyAppConfiguration configuration, Environment environment) {
        registerResources(configuration, environment);
        registerHealthChecks(configuration, environment);
        //environment.jersey().register(LoggingFilter.class);
    }

    private void registerResources(EnergyAppConfiguration configuration, Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi db = factory.build(environment, configuration.getDatabase(), "mysql");

        // Services
        DeviceDataIngestService deviceDataIngestService = createDeviceDataIngestService(configuration);
        DeviceDataService deviceDataService = createDeviceDataService(db);

        // Resources
        final DeviceResource resource = new DeviceResource(deviceDataIngestService, deviceDataService);
        environment.jersey().register(resource);
    }

    private void registerHealthChecks(EnergyAppConfiguration configuration, Environment environment) {
        final BasicHealthCheck healthCheck =
                new BasicHealthCheck();
        environment.healthChecks().register("basic", healthCheck);
    }

    private DeviceDataIngestService createDeviceDataIngestService(EnergyAppConfiguration configuration) {
        DeviceDataMessageProducer deviceDataMessageProducer = new DeviceDataMessageProducer(configuration);
        return new DeviceDataIngestService(deviceDataMessageProducer);
    }

    private DeviceDataService createDeviceDataService(Jdbi db) {
        return new DeviceDataService(new DeviceDataDao(db));
    }

}