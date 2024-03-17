package com.duckisoft.gardening.integration;

import com.duckisoft.gardening.dto.device.AddDeviceRequest;
import com.duckisoft.gardening.dto.device.DeviceResponse;
import com.duckisoft.gardening.dto.device.UpdateDeviceRequest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


public class DeviceIntegrationTests extends BaseIntegrationTest {

    @Test
    void testCreateDeviceSuccess() {
        String jwtToken = registerUser();

        createDevice(jwtToken);
    }

    @Test
    void testGetDeviceSuccess() {
        String jwtToken = registerUser();

        createDevice(jwtToken);

        List<DeviceResponse> responseList = getDevices(jwtToken);
        assertEquals(1, responseList.size());
        DeviceResponse response = responseList.get(0);
        assertEquals("test", response.getName());
        assertEquals("test desc", response.getDescription());
        assertTrue(response.isHumidity());
        assertTrue(response.isTemperature());
    }

    @Test
    void testUpdateDeviceSuccess() {
        String jwtToken = registerUser();

        createDevice(jwtToken);

        List<DeviceResponse> responseList = getDevices(jwtToken);

        given().header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body(new UpdateDeviceRequest(responseList.get(0).getId(), "test2", "test desc 2", false, false))
                .put("/device")
                .then()
                .assertThat().statusCode(200);

        List<DeviceResponse> responseListUpdated = getDevices(jwtToken);
        assertEquals(1, responseListUpdated.size());

        DeviceResponse updatedDevice = responseListUpdated.get(0);
        assertEquals("test2", updatedDevice.getName());
        assertEquals("test desc 2", updatedDevice.getDescription());
        assertFalse(updatedDevice.isTemperature());
        assertFalse(updatedDevice.isHumidity());
    }


    private void createDevice(String jwtToken) {
        given().header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body(new AddDeviceRequest("test", "test desc", true, true))
                .post("/device")
                .then()
                .assertThat().statusCode(201);
    }

    private List<DeviceResponse> getDevices(String jwtToken) {
        return given().header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .get("/device")
                .then()
                .assertThat().statusCode(200).extract().jsonPath().getList(".", DeviceResponse.class);

    }
}
