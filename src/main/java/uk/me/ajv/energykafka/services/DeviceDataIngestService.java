package uk.me.ajv.energykafka.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import uk.me.ajv.energykafka.adapters.DeviceDataMessageProducer;
import uk.me.ajv.energykafka.model.DeviceData;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class DeviceDataIngestService {

    private final DeviceDataMessageProducer deviceDataMessageProducer;

    public DeviceDataIngestService(DeviceDataMessageProducer deviceDataMessageProducer) {
        this.deviceDataMessageProducer = deviceDataMessageProducer;
    }

    public void handleUpdateFromDevice(DeviceData data) throws ExecutionException, InterruptedException, TimeoutException {
        // wait for completed publish before returning
        RecordMetadata metadata = deviceDataMessageProducer.publishUpdate(data).get(2, TimeUnit.SECONDS);
        log.info("Device data for device: " + data.getDeviceId() + " published to topic: " + metadata.topic());
    }
}
