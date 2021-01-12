package uk.me.ajv.energykafka.adapters;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import uk.me.ajv.energykafka.EnergyAppConfiguration;
import uk.me.ajv.energykafka.KafkaConfiguration;
import uk.me.ajv.energykafka.avro.DeviceDataMessage;
import uk.me.ajv.energykafka.model.DeviceData;

import java.util.Properties;
import java.util.concurrent.Future;

@Slf4j
public class DeviceDataMessageProducer {

    private final KafkaProducer<String, DeviceDataMessage> deviceDataMessageProducer;
    private final String deviceDataTopic;

    public DeviceDataMessageProducer(EnergyAppConfiguration configuration) {
        deviceDataTopic = configuration.getKafka().getDeviceDataTopic();
        deviceDataMessageProducer = new KafkaProducer<String, DeviceDataMessage>(kafkaProperties(configuration));
    }

    public Future<RecordMetadata> publishUpdate(DeviceData data) {
        ProducerRecord<String, DeviceDataMessage> record = new ProducerRecord<String, DeviceDataMessage>(deviceDataTopic, data.toMessage());
        log.info("Writing device data to kafka: " + record);
        return deviceDataMessageProducer.send(record, ((recordMetadata, ex) -> {
            if (ex == null) {
                log.info("Successfully published message to kafka: " + recordMetadata);
            } else {
                log.error("Failed to publish message to kafka: " + record, ex);
            }
        }));
    }

    private static Properties kafkaProperties(EnergyAppConfiguration configuration) {
        KafkaConfiguration kafkaConfiguration = configuration.getKafka();
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", kafkaConfiguration.getBootstrapServers());
        properties.setProperty("acks", "" + kafkaConfiguration.getAcksRequiredOnWrite());
        properties.setProperty("retries", "" + kafkaConfiguration.getRetries());

        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", KafkaAvroSerializer.class.getName());
        properties.setProperty("schema.registry.url", kafkaConfiguration.getSchemaRegistryUrl());
        return properties;
    }
}
