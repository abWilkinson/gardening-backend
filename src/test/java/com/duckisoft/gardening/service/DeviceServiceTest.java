package com.duckisoft.gardening.service;

import com.duckisoft.gardening.dto.device.DeviceRequest;
import com.duckisoft.gardening.entities.Device;
import com.duckisoft.gardening.entities.User;
import com.duckisoft.gardening.exception.DeviceException;
import com.duckisoft.gardening.repository.DeviceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {
    @Mock
    private DeviceRepository deviceRepository;

    private DeviceService service;

    @BeforeEach
    public void before() {
        service = new DeviceService(deviceRepository);
    }

    @Test
    public void shouldNotAddWhereNameExists() {
        Device testDevice = new Device();
        testDevice.setName("test");
        when(deviceRepository.findDevicesByUserId(anyInt())).thenReturn(List.of(testDevice));
        DeviceException thrown = Assertions.assertThrows(DeviceException.class, () ->
            service.addDevice(createDeviceRequest(), new User())
        );
        assertEquals("You already have a device with this name.", thrown.getMessage());
    }

    @Test
    public void shouldAddValidDevice() {
        Device testDevice = new Device();
        testDevice.setName("test1");
        when(deviceRepository.findDevicesByUserId(anyInt())).thenReturn(List.of(testDevice));
        assertDoesNotThrow(() -> service.addDevice(createDeviceRequest(), new User()));
    }


    private DeviceRequest createDeviceRequest() {
        return new DeviceRequest("test", "test", true, true);
    }
}
