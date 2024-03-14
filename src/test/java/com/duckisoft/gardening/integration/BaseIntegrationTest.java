package com.duckisoft.gardening.integration;

import com.duckisoft.gardening.dto.auth.JwtResponse;
import com.duckisoft.gardening.dto.auth.RegisterRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseIntegrationTest {
    @LocalServerPort
    protected int port;

    @BeforeEach
    public void before() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.defaultParser = Parser.JSON;
    }

    protected String registerUser() {
        JwtResponse registerResponse = given().contentType(ContentType.JSON).body(new RegisterRequest(UUID.randomUUID() + "@test.com", "test"))
                .post("/auth/register")
                .then()
                .assertThat().statusCode(200).extract().as(JwtResponse.class);

        return registerResponse.getJwtToken();
    }
}
