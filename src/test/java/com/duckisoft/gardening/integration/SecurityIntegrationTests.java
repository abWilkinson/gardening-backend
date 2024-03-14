package com.duckisoft.gardening.integration;

import com.duckisoft.gardening.dto.auth.JwtRequest;
import com.duckisoft.gardening.dto.auth.JwtResponse;
import com.duckisoft.gardening.dto.auth.RegisterRequest;
import com.duckisoft.gardening.dto.error.ErrorResponse;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityIntegrationTests extends BaseIntegrationTest{

	@Test
	void testNoAuth() {
		when().get("/hello")
				.then()
				.assertThat().statusCode(401);
	}

	@Test
	void testRegister() {
		JwtResponse registerResponse = given().contentType(ContentType.JSON).body(new RegisterRequest("test@test.com", "test"))
			.post("/auth/register")
				.then()
				.assertThat().statusCode(200).extract().as(JwtResponse.class);

		assertThat(registerResponse.getJwtToken()).isNotNull();

		given().header("Authorization", "Bearer " + registerResponse.getJwtToken()).get("/hello")
				.then()
				.assertThat().statusCode(200);
	}

	@Test
	void testDuplicateUserRegistration() {
		given().contentType(ContentType.JSON).body(new RegisterRequest("testUnique@test.com", "test"))
				.post("/auth/register")
				.then()
				.assertThat().statusCode(200);

		ErrorResponse error = given().contentType(ContentType.JSON).body(new RegisterRequest("testUnique@test.com", "test"))
				.post("/auth/register")
				.then()
				.assertThat().statusCode(409).extract().as(ErrorResponse.class);

		assertThat(error.getMsg()).isEqualTo("This email address is already registered.");
	}

	@Test
	void testInvalidToken() {
		given().header("Authorization", "Bearer " + "sadasdasd").get("/hello")
				.then()
				.assertThat().statusCode(401);
	}

	@Test
	void testLoginSuccess() {
		given().contentType(ContentType.JSON).body(new RegisterRequest("testLoginSuccess@test.com", "test"))
				.post("/auth/register")
				.then()
				.assertThat().statusCode(200);

		JwtResponse loginResponse = given().contentType(ContentType.JSON).body(new JwtRequest("testLoginSuccess@test.com", "test"))
				.post("/auth/jwt")
				.then()
				.assertThat().statusCode(200).extract().as(JwtResponse.class);

		assertThat(loginResponse.getJwtToken()).isNotNull();
	}

	@Test
	void testLoginInvalidEmail() {
		ErrorResponse loginResponse = given().contentType(ContentType.JSON).body(new JwtRequest("invalid@test.com", "test"))
				.post("/auth/jwt")
				.then()
				.assertThat().statusCode(401).extract().as(ErrorResponse.class);

		assertThat(loginResponse.getMsg()).isEqualTo("Invalid credentials.");
	}

	@Test
	void testLoginInvalidPassword() {
		given().contentType(ContentType.JSON).body(new RegisterRequest("invalidPass@test.com", "test"))
				.post("/auth/register")
				.then()
				.assertThat().statusCode(200);

		ErrorResponse loginResponse = given().contentType(ContentType.JSON).body(new JwtRequest("invalidPass@test.com", "wrong"))
				.post("/auth/jwt")
				.then()
				.assertThat().statusCode(401).extract().as(ErrorResponse.class);

		assertThat(loginResponse.getMsg()).isEqualTo("Invalid credentials.");
	}
}
