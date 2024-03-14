package com.duckisoft.gardening.controller;

import com.duckisoft.gardening.dto.device.DeviceRequest;
import com.duckisoft.gardening.entities.User;
import com.duckisoft.gardening.service.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/device")
@AllArgsConstructor
public class DeviceController {
    private DeviceService deviceService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody DeviceRequest deviceRequest, Authentication authentication) {
        deviceService.addDevice(deviceRequest, (User) authentication.getPrincipal());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
