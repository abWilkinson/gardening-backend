package com.duckisoft.gardening.service;

import com.duckisoft.gardening.dto.device.DeviceRequest;
import com.duckisoft.gardening.entities.Device;
import com.duckisoft.gardening.entities.User;
import com.duckisoft.gardening.exception.DeviceException;
import com.duckisoft.gardening.repository.DeviceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class DeviceService {
    private DeviceRepository deviceRepository;

    @Transactional
    public void addDevice(DeviceRequest deviceRequest, User user) {
        boolean deviceNameExists = deviceRepository.findDevicesByUserId(user.getId())
                .stream().anyMatch(device -> device.getName().equals(deviceRequest.getName()));
        if (deviceNameExists) {
            throw new DeviceException("You already have a device with this name.");
        }
        Device newDevice = new Device();
        newDevice.setUser(user);
        newDevice.setName(deviceRequest.getName());
        newDevice.setDescription(deviceRequest.getDescription());
        newDevice.setHumidity(deviceRequest.isHumidity());
        newDevice.setTemperature(deviceRequest.isTemperature());
        deviceRepository.save(newDevice);
    }
}
