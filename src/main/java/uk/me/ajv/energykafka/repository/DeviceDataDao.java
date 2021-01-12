package uk.me.ajv.energykafka.repository;

import org.jdbi.v3.core.Jdbi;
import uk.me.ajv.energykafka.model.DeviceData;

import java.util.Optional;

public class DeviceDataDao {

    private static final String insertDeviceData =
            "insert into device_data(charging_source, charging, current_capacity, device_id, inverter_state, " +
                    "module_l_temp, module_r_temp, processor_1_temp, processor_2_temp, processor_3_temp, processor_4_temp, " +
                    "soc_regulator, timestamp) values (:charging_source, :charging, :current_capacity, :device_id, :inverter_state, " +
                    ":module_l_temp, :module_r_temp, :processor_1_temp, :processor_2_temp, :processor_3_temp, :processor_4_temp, " +
                    ":soc_regulator, :timestamp)";

    private static final String findLatestByDeviceId = "select * from device_data where device_id=:device_id order by timestamp desc";

    private final Jdbi dbi;

    public DeviceDataDao(Jdbi db) {
        this.dbi = db;
    }

    public void insert(DeviceData deviceData) {
        dbi.useHandle(handle -> {
            handle.createUpdate(insertDeviceData)
                    .bind("charging_source", deviceData.getChargingSource())
                    .bind("charging", deviceData.getCharging())
                    .bind("current_capacity", deviceData.getCurrentCapacity())
                    .bind("device_id", deviceData.getDeviceId())
                    .bind("inverter_state", deviceData.getInverterState())
                    .bind("module_l_temp", deviceData.getModuleLTemp())
                    .bind("module_r_temp", deviceData.getModuleRTemp())
                    .bind("processor_1_temp", deviceData.getProcessor1Temp())
                    .bind("processor_2_temp", deviceData.getProcessor2Temp())
                    .bind("processor_3_temp", deviceData.getProcessor3Temp())
                    .bind("processor_4_temp", deviceData.getProcessor4Temp())
                    .bind("soc_regulator", deviceData.getSocRegulator())
                    .bind("timestamp", deviceData.getTimeStamp())
                    .execute();
        });
    }

    public Optional<DeviceData> findLatestByDeviceId(String deviceId) {
        return dbi.withHandle(handle -> handle.createQuery(findLatestByDeviceId)
                .bind("device_id", deviceId)
                .mapToBean(DeviceData.class)
                .findFirst());
    }
}
