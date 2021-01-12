package uk.me.ajv.energykafka.adapters;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import uk.me.ajv.energykafka.avro.DeviceDataMessage;
import uk.me.ajv.energykafka.model.DeviceData;
import uk.me.ajv.energykafka.services.DeviceDataService;

@Slf4j
public class DeviceDataMessageProcessor implements Processor<String, DeviceDataMessage> {

    private ProcessorContext processorContext;

    private final DeviceDataService deviceDataService;

    public DeviceDataMessageProcessor(DeviceDataService deviceDataService) {
        this.deviceDataService = deviceDataService;
    }

    @Override
    public void init(ProcessorContext processorContext) {
        this.processorContext = processorContext;
    }

    @Override
    public void process(String key, DeviceDataMessage deviceDataMessage) {
        log.info("Reading message from topic: " + processorContext.topic() + ", partition: " +
                processorContext.partition() + ", offset: " + processorContext.offset());
        deviceDataService.save(DeviceData.fromDeviceDataMsg(deviceDataMessage));
        processorContext.commit();
    }

    @Override
    public void close() {
    }
}
