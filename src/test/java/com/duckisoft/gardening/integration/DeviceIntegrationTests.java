package com.duckisoft.gardening.integration;

import com.duckisoft.gardening.dto.device.DeviceRequest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;


public class DeviceIntegrationTests extends BaseIntegrationTest {

    @Test
    void testCreateDeviceSuccess() {
        String jwtToken = registerUser();

        given().header("Authorization", "Bearer " + jwtToken)
                .contentType(ContentType.JSON)
                .body(new DeviceRequest("test", "test desc", true, true))
                .post("/device")
                .then()
                .assertThat().statusCode(201);
    }

}
