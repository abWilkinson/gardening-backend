package com.duckisoft.gardening.controller;

import com.duckisoft.gardening.dto.device.AddDeviceRequest;
import com.duckisoft.gardening.dto.device.DeviceResponse;
import com.duckisoft.gardening.dto.device.UpdateDeviceRequest;
import com.duckisoft.gardening.entities.User;
import com.duckisoft.gardening.service.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/device")
@AllArgsConstructor
public class DeviceController {
    private DeviceService deviceService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody AddDeviceRequest deviceRequest, Authentication authentication) {
        deviceService.addDevice(deviceRequest, (User) authentication.getPrincipal());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<DeviceResponse>> getDevices(Authentication authentication) {
        return ResponseEntity.ok(deviceService.getDevices((User) authentication.getPrincipal()));
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody UpdateDeviceRequest deviceRequest, Authentication authentication) {
        deviceService.updateDevice(deviceRequest, (User) authentication.getPrincipal());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
