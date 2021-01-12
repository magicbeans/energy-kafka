package uk.me.ajv.energykafka.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jackson.JsonSnakeCase;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@JsonSnakeCase
public class DeviceDataDto {

    @JsonProperty("charging_source")
    private String chargingSource;

    @JsonProperty
    private int charging;

    @JsonProperty("current_capacity")
    private int currentCapacity;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("inverter_state")
    private int inverterState;

    @JsonProperty("moduleL_temp")
    private int moduleLTemp;

    @JsonProperty("moduleR_temp")
    private int moduleRTemp;

    @JsonProperty("processor1_temp")
    private int processor1Temp;

    @JsonProperty("processor2_temp")
    private int processor2Temp;

    @JsonProperty("processor3_temp")
    private int processor3Temp;

    @JsonProperty("processor4_temp")
    private int processor4Temp;

    @JsonProperty("SoC_regulator")
    private double socRegulator;

}
