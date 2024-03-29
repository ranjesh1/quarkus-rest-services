package com.demo.app.ws;

import com.demo.app.ws.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class UserRestIT {

    private User user;

    @Inject
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        user = new User();
        user.setFirstName("Steve");
        user.setLastName("Rob");
        user.setEmail(UUID.randomUUID().toString() + "@gmail.com");
        user.setId(1L);
        user.setFirstLineOfAddress("12 Avenue");
        user.setSecondLineOfAddress("Commercial street");
        user.setTown("London");
        user.setPostCode("HA6 0EW");

    }

    @Test
    public void testCreateUser() throws Exception {
        String expected = objectMapper.writeValueAsString(user);
        String request = expected.replace("\"id\":1,", "");

        given().contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/users")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("firstName", is(user.getFirstName()))
                .body("lastName", is(user.getLastName()))
                .body("email", is(user.getEmail()))
                .body("firstLineOfAddress", is(user.getFirstLineOfAddress()))
                .body("secondLineOfAddress", is(user.getSecondLineOfAddress()))
                .body("town", is(user.getTown()))
                .body("postCode", is(user.getPostCode()));
    }
}
