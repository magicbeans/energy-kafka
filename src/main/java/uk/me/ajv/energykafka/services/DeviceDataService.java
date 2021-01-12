package uk.me.ajv.energykafka.services;

import lombok.extern.slf4j.Slf4j;
import uk.me.ajv.energykafka.model.DeviceData;
import uk.me.ajv.energykafka.repository.DeviceDataDao;

import java.util.Optional;

@Slf4j
public class DeviceDataService {

    private final DeviceDataDao deviceDataDao;

    public DeviceDataService(DeviceDataDao deviceDataDao) {
        this.deviceDataDao = deviceDataDao;
    }

    public void save(DeviceData data) {
        log.info("Saving device data to persistent storage for device: " + data.getDeviceId());
        deviceDataDao.insert(data);
        log.info("Device data saved for device: " + data.getDeviceId());
    }

    public Optional<DeviceData> findLatestDeviceUpdate(String deviceId) {
        log.info("Finding device data for device: " + deviceId);
        return deviceDataDao.findLatestByDeviceId(deviceId);
    }
}
