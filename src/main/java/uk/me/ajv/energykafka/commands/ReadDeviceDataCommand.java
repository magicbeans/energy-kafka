package uk.me.ajv.energykafka.commands;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.dropwizard.Application;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.jdbi.v3.core.Jdbi;
import uk.me.ajv.energykafka.EnergyAppConfiguration;
import uk.me.ajv.energykafka.adapters.DeviceDataMessageProcessor;
import uk.me.ajv.energykafka.avro.DeviceDataMessage;
import uk.me.ajv.energykafka.repository.DeviceDataDao;
import uk.me.ajv.energykafka.services.DeviceDataService;

import java.util.Collections;
import java.util.Properties;

public class ReadDeviceDataCommand extends EnvironmentCommand<EnergyAppConfiguration> {

    public ReadDeviceDataCommand(Application<EnergyAppConfiguration> application) {
        this(application, "read-device-data",
                "Reads device data from kafka, submitting incoming records to persistent storage");
    }

    protected ReadDeviceDataCommand(Application<EnergyAppConfiguration> application, String name, String description) {
        super(application, name, description);
    }

    @Override
    public void configure(Subparser subparser) {
        super.configure(subparser);
        // Not required immediately
    }

    @Override
    protected void run(Environment environment, Namespace namespace, EnergyAppConfiguration configuration) throws Exception {
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi db = factory.build(environment, configuration.getDatabase(), "mysql");

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, configuration.getKafka().getStreamsApplicationId());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, configuration.getKafka().getBootstrapServers());

        Serde<String> stringSerde = Serdes.String();
        Serde<DeviceDataMessage> deviceDataSerde = new SpecificAvroSerde<DeviceDataMessage>();
        deviceDataSerde.configure(Collections.singletonMap(
                AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
                configuration.getKafka().getSchemaRegistryUrl()
        ), false);

        StreamsBuilder builder = new StreamsBuilder();


        builder.stream(configuration.getKafka().getDeviceDataTopic(), Consumed.with(stringSerde, deviceDataSerde))
                .process(() -> deviceDataMessageProcessor(db));

        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), props);
        kafkaStreams.start();
    }

    private DeviceDataMessageProcessor deviceDataMessageProcessor(Jdbi db) {
        return new DeviceDataMessageProcessor(createDeviceDataService(db));
    }

    private DeviceDataService createDeviceDataService(Jdbi db) {
        DeviceDataDao deviceDataDao = new DeviceDataDao(db);
        return new DeviceDataService(deviceDataDao);
    }
}
