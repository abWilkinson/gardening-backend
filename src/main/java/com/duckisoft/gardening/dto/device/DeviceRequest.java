package com.duckisoft.gardening.dto.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequest {
    private String name;
    private String description;
    private boolean temperature;
    private boolean humidity;
}
