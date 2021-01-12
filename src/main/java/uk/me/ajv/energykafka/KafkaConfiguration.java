package uk.me.ajv.energykafka;

import lombok.Data;

@Data
public class KafkaConfiguration {

    private String bootstrapServers;
    private int acksRequiredOnWrite;
    private int retries;
    private String schemaRegistryUrl;
    private String streamsApplicationId;
    private String deviceDataTopic;
}
