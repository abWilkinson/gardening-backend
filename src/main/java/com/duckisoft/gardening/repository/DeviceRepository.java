package com.duckisoft.gardening.repository;

import com.duckisoft.gardening.entities.Device;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeviceRepository extends CrudRepository<Device, Integer> {
    public List<Device> findDevicesByUserId(int userId);
}
