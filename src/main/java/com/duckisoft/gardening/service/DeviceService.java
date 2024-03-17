package com.duckisoft.gardening.service;

import com.duckisoft.gardening.dto.device.AddDeviceRequest;
import com.duckisoft.gardening.dto.device.DeviceResponse;
import com.duckisoft.gardening.dto.device.UpdateDeviceRequest;
import com.duckisoft.gardening.entities.Device;
import com.duckisoft.gardening.entities.User;
import com.duckisoft.gardening.exception.DeviceException;
import com.duckisoft.gardening.exception.GenericExcepion;
import com.duckisoft.gardening.repository.DeviceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DeviceService {
    private DeviceRepository deviceRepository;

    @Transactional
    public void addDevice(AddDeviceRequest deviceRequest, User user) {
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

    @Transactional
    public List<DeviceResponse> getDevices(User user) {
        List<Device> devices = deviceRepository.findDevicesByUserId(user.getId());
        return devices
                .stream()
                .map(device ->
                new DeviceResponse(device.getId(),device.getName(), device.getDescription(), device.isTemperature(), device.isHumidity()))
                .toList();
    }

    @Transactional
    public void updateDevice(UpdateDeviceRequest deviceRequest, User user) {
        Optional<Device> deviceOptional = deviceRepository.findById(deviceRequest.getId());
        if (deviceOptional.isEmpty()) {
            throw new GenericExcepion();
        }
        Device device = deviceOptional.get();
        List<Device> allDevices = device.getUser().getDevices();
        if (device.getUser().getId() != user.getId()) {
            throw new GenericExcepion();
        }
        if (allDevices.stream()
                .filter(existing -> existing.getId() != device.getId())
                .anyMatch(existing -> existing.getName().equals(deviceRequest.getName()))) {
            throw new DeviceException("You already have a device with this name.");
        }
        device.setName(deviceRequest.getName());
        device.setDescription(deviceRequest.getDescription());
        device.setHumidity(deviceRequest.isHumidity());
        device.setTemperature(deviceRequest.isTemperature());
        deviceRepository.save(device);
    }
}
