package uk.me.ajv.energykafka.model;

import lombok.*;
import uk.me.ajv.energykafka.api.DeviceDataDto;
import uk.me.ajv.energykafka.avro.DeviceDataMessage;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private LocalDateTime timeStamp;

    public static DeviceData fromDeviceDataDto(DeviceDataDto dto) {
        return DeviceData.builder()
                .chargingSource(dto.getChargingSource())
                .charging(dto.getCharging())
                .currentCapacity(dto.getCurrentCapacity())
                .deviceId(dto.getDeviceId())
                .inverterState(dto.getInverterState())
                .moduleLTemp(dto.getModuleLTemp())
                .moduleRTemp(dto.getModuleRTemp())
                .processor1Temp(dto.getProcessor1Temp())
                .processor2Temp(dto.getProcessor2Temp())
                .processor3Temp(dto.getProcessor3Temp())
                .processor4Temp(dto.getProcessor4Temp())
                .socRegulator(dto.getSocRegulator())
                .timeStamp(LocalDateTime.now()).build();
    }

    public static DeviceData fromDeviceDataMsg(DeviceDataMessage message) {
        return DeviceData.builder()
                .chargingSource(message.getChargingSource().toString())
                .charging(message.getCharging())
                .currentCapacity(message.getCurrentCapacity())
                .deviceId(message.getDeviceId().toString())
                .inverterState(message.getInverterState())
                .moduleLTemp(message.getModuleLTemp())
                .moduleRTemp(message.getModuleRTemp())
                .processor1Temp(message.getProcessor1Temp())
                .processor2Temp(message.getProcessor2Temp())
                .processor3Temp(message.getProcessor3Temp())
                .processor4Temp(message.getProcessor4Temp())
                .socRegulator(message.getSoCRegulator())
                .timeStamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(message.getTimestamp()), ZoneId.of("UTC"))).build();
    }

    public DeviceDataMessage toMessage() {
        return DeviceDataMessage.newBuilder()
                .setChargingSource(chargingSource)
                .setCharging(charging)
                .setCurrentCapacity(currentCapacity)
                .setDeviceId(deviceId)
                .setInverterState(inverterState)
                .setModuleLTemp(moduleLTemp)
                .setModuleRTemp(moduleRTemp)
                .setProcessor1Temp(processor1Temp)
                .setProcessor2Temp(processor2Temp)
                .setProcessor3Temp(processor3Temp)
                .setProcessor4Temp(processor4Temp)
                .setSoCRegulator(socRegulator)
                .setTimestamp(timeStamp.toInstant(ZoneOffset.UTC).toEpochMilli()).build();
    }

    public DeviceDataDto toDto() {
        DeviceDataDto dto = new DeviceDataDto();
        dto.setChargingSource(chargingSource);
        dto.setCharging(charging);
        dto.setCurrentCapacity(currentCapacity);
        dto.setDeviceId(deviceId);
        dto.setInverterState(inverterState);
        dto.setModuleLTemp(moduleLTemp);
        dto.setModuleRTemp(moduleRTemp);
        dto.setProcessor1Temp(processor1Temp);
        dto.setProcessor2Temp(processor2Temp);
        dto.setProcessor3Temp(processor3Temp);
        dto.setProcessor4Temp(processor4Temp);
        dto.setSocRegulator(socRegulator);

        //TODO - Timestamp? We have it now
        return dto;
    }
}
