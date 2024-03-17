package com.duckisoft.gardening.service;

import com.duckisoft.gardening.dto.device.AddDeviceRequest;
import com.duckisoft.gardening.dto.device.DeviceResponse;
import com.duckisoft.gardening.dto.device.UpdateDeviceRequest;
import com.duckisoft.gardening.entities.Device;
import com.duckisoft.gardening.entities.User;
import com.duckisoft.gardening.exception.DeviceException;
import com.duckisoft.gardening.exception.GenericExcepion;
import com.duckisoft.gardening.repository.DeviceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {
    @Mock
    private DeviceRepository deviceRepository;

    private DeviceService service;

    @BeforeEach
    public void before() {
        service = new DeviceService(deviceRepository);
    }

    @Test
    void shouldNotAddWhereNameExists() {
        Device testDevice = new Device();
        testDevice.setName("test");
        when(deviceRepository.findDevicesByUserId(anyInt())).thenReturn(List.of(testDevice));
        AddDeviceRequest request = createDeviceRequest();
        User user = new User();
        DeviceException thrown = Assertions.assertThrows(DeviceException.class, () ->
            service.addDevice(request, user)
        );
        assertEquals("You already have a device with this name.", thrown.getMessage());
    }

    @Test
    void shouldAddValidDevice() {
        Device testDevice = new Device();
        testDevice.setName("test1");
        when(deviceRepository.findDevicesByUserId(anyInt())).thenReturn(List.of(testDevice));
        assertDoesNotThrow(() -> service.addDevice(createDeviceRequest(), new User()));
    }

    @Test
    void shouldReturnDevices() {
        Device testDevice = new Device();
        testDevice.setName("test1");
        testDevice.setDescription("desc");
        testDevice.setTemperature(true);
        testDevice.setHumidity(true);
        testDevice.setId(1);
        when(deviceRepository.findDevicesByUserId(anyInt())).thenReturn(List.of(testDevice));
        List<DeviceResponse> response = service.getDevices(new User());
        assertEquals(1, response.size());
        DeviceResponse device = response.get(0);
        assertEquals("test1", device.getName());
        assertEquals("desc", device.getDescription());
        assertEquals(1, device.getId());
        assertTrue(device.isTemperature());
        assertTrue(device.isHumidity());
    }

    @Test
    void shouldUpdateValidDevice() {
        Device testDevice = new Device();
        User user = new User();
        user.setDevices(List.of());
        testDevice.setUser(user);
        testDevice.setName("test1");
        when(deviceRepository.findById(anyInt())).thenReturn(Optional.of(testDevice));
        service.updateDevice(new UpdateDeviceRequest(), new User());
        verify(deviceRepository, times(1)).save(any());
    }

    @Test
    void shouldFailUpdateOnDeviceNotFound() {
        User user = new User();
        when(deviceRepository.findById(anyInt())).thenReturn(Optional.empty());
        UpdateDeviceRequest request = updateDeviceRequest();
        GenericExcepion thrown = Assertions.assertThrows(GenericExcepion.class, () ->
                service.updateDevice(request, user)
        );
        assertEquals("There was a problem with this request.", thrown.getMessage());
    }

    @Test
    void shouldFailUpdateOnIdNotMatchingUser() {
        Device testDevice = new Device();
        User requestUser = new User();
        requestUser.setId(2);
        User deviceUser = new User();
        deviceUser.setId(1);
        deviceUser.setDevices(List.of());
        testDevice.setUser(deviceUser);
        testDevice.setName("test1");
        when(deviceRepository.findById(anyInt())).thenReturn(Optional.of(testDevice));
        UpdateDeviceRequest request = updateDeviceRequest();
        GenericExcepion thrown = Assertions.assertThrows(GenericExcepion.class, () ->
                service.updateDevice(request, requestUser)
        );
        assertEquals("There was a problem with this request.", thrown.getMessage());
    }

    @Test
    void shouldFailUpdateOnDuplicateName() {
        Device testDevice = new Device();
        Device otherDevice = new Device();
        otherDevice.setId(2);
        otherDevice.setName("test1");

        User deviceUser = new User();
        deviceUser.setId(1);
        deviceUser.setDevices(List.of(testDevice, otherDevice));
        testDevice.setUser(deviceUser);
        when(deviceRepository.findById(anyInt())).thenReturn(Optional.of(testDevice));
        UpdateDeviceRequest request = updateDeviceRequest();
        request.setName("test1");
        DeviceException thrown = Assertions.assertThrows(DeviceException.class, () ->
                service.updateDevice(request, deviceUser)
        );
        assertEquals("You already have a device with this name.", thrown.getMessage());
    }
    private UpdateDeviceRequest updateDeviceRequest() {
        return new UpdateDeviceRequest(1,"test", "test", true, true);
    }

    private AddDeviceRequest createDeviceRequest() {
        return new AddDeviceRequest("test", "test", true, true);
    }
}
