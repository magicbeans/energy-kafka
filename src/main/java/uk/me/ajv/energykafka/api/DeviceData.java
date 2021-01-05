package uk.me.ajv.energykafka.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jackson.JsonSnakeCase;

@JsonSnakeCase
public class DeviceData {

    private String chargingSource;
    private int charging;
    private int currentCapacity;
    private String deviceId;
    private int inverterState;
    private int moduleLTemp;
    private int moduleRTemp;
    private int processor1Temp;
    private int processor2Temp;
    private int processor3Temp;
    private int processor4Temp;
    private double socRegulator;

    public DeviceData() {
    }

    @JsonProperty("charging_source")
    public String getChargingSource() {
        return chargingSource;
    }

    @JsonProperty
    public int getCharging() {
        return charging;
    }

    @JsonProperty("current_capacity")
    public int getCurrentCapacity() {
        return currentCapacity;
    }

    @JsonProperty("device_id")
    public String getDeviceId() {
        return deviceId;
    }

    @JsonProperty("inverter_state")
    public int getInverterState() {
        return inverterState;
    }

    @JsonProperty("moduleL_temp")
    public int getModuleLTemp() {
        return moduleLTemp;
    }

    @JsonProperty("moduleR_temp")
    public int getModuleRTemp() {
        return moduleRTemp;
    }

    @JsonProperty("processor1_temp")
    public int getProcessor1Temp() {
        return processor1Temp;
    }

    @JsonProperty("processor2_temp")
    public int getProcessor2Temp() {
        return processor2Temp;
    }

    @JsonProperty("processor3_temp")
    public int getProcessor3Temp() {
        return processor3Temp;
    }

    @JsonProperty("processor4_temp")
    public int getProcessor4Temp() {
        return processor4Temp;
    }

    @JsonProperty("SoC_regulator")
    public double getSocRegulator() {
        return socRegulator;
    }

    @Override
    public String toString() {
        return "DeviceData{" +
                "chargingSource='" + chargingSource + '\'' +
                ", charging=" + charging +
                ", currentCapacity=" + currentCapacity +
                ", deviceId='" + deviceId + '\'' +
                ", inverterState=" + inverterState +
                ", moduleLTemp=" + moduleLTemp +
                ", moduleRTemp=" + moduleRTemp +
                ", processor1Temp=" + processor1Temp +
                ", processor2Temp=" + processor2Temp +
                ", processor3Temp=" + processor3Temp +
                ", processor4Temp=" + processor4Temp +
                ", socRegulator=" + socRegulator +
                '}';
    }
}
