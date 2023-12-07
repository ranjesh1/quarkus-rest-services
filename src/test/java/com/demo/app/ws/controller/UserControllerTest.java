package com.demo.app.ws.controller;

import com.demo.app.ws.entities.User;
import com.demo.app.ws.service.UserService;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.core.
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@QuarkusTest
public class UserControllerTest {

    @InjectMock
    private UserService userService;

    private User user;
    private User user2;

    @Inject
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        user = new User();
        user.setFirstName("Steve");
        user.setLastName("Rob");
        user.setEmail("steverob@gmail.com");
        user.setId(1L);
        user.setFirstLineOfAddress("12 Avenue");
        user.setSecondLineOfAddress("Commercial street");
        user.setTown("London");
        user.setPostCode("HA6 0EW");

        user2 = new User();
        user2.setFirstName("Stev");
        user2.setLastName("Ro");
        user2.setEmail("stevob@gmail.com");
        user2.setId(2L);
        user2.setFirstLineOfAddress("13 Avenue");
        user2.setSecondLineOfAddress("Commercial street");
        user2.setTown("London");
        user2.setPostCode("HA7 0EW");

    }

    @Test
    void testGetUser() throws Exception {
        String expected = objectMapper.writeValueAsString(user);
        when(userService.getUserById(anyLong())).thenReturn(user);

        var response = given().contentType(ContentType.JSON)
                .when()
                .get("/api/users/" +user.getId())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract();

        assertEquals(expected, response.asString());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(user, user2);
        String expected = objectMapper.writeValueAsString(users);

        when(userService.getUsers()).thenReturn(users);
        var response = given().contentType(ContentType.JSON)
                .when()
                .get("/api/users")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract();
        assertEquals(expected, response.asString());
    }

   @Test
    void testCreateUser() throws Exception {
        String expected = objectMapper.writeValueAsString(user);
        String request = expected.replace("\"id\":1,","");
        when(userService.createUser(any(User.class))).thenReturn(user);

       var response = given().contentType(ContentType.JSON)
               .accept(ContentType.JSON)
               .body(request)
               .when()
               .post("/api/users")
               .then()
               .statusCode(Response.Status.OK.getStatusCode())
               .extract();
       assertEquals(expected, response.asString());
    }


    @Test
    void testUpdateUser() throws Exception {
        User updateUser = new User();
        updateUser.setFirstName("Steveupdated");
        updateUser.setLastName("Robupdated");
        updateUser.setEmail("steverobupdated@gmail.com");
        updateUser.setFirstLineOfAddress("12 Avenueupdated");
        updateUser.setSecondLineOfAddress("Commercial streetupdated");
        updateUser.setTown("London");
        updateUser.setPostCode("HA6 0EW");

        User updateUserWithId = new User();
        new ModelMapper().map(updateUser, updateUserWithId);
        updateUserWithId.setId(1L);
        //Exclude null values
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String putBody = objectMapper.writeValueAsString(updateUser);
        String expectedResponse = objectMapper.writeValueAsString(updateUserWithId);

        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(updateUserWithId);

        var response = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(putBody)
                .when()
                .put("/api/users/{userId}", updateUserWithId.getId())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract();
        assertEquals(expectedResponse, response.asString());

    }


    @Test
    void testPatchUpdateUser() throws Exception {
        User updateUser = new User();
        updateUser.setFirstName("Steveupdated");
        updateUser.setLastName("Robupdated");

        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());

        //Exclude null values
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String patchBody = objectMapper.writeValueAsString(updateUser);
        String expectedResponse = objectMapper.writeValueAsString(user);

        when(userService.patchUpdateUser(anyLong(), any(User.class))).thenReturn(user);

        var response = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(patchBody)
                .when()
                .patch("/api/users/{userId}", user.getId())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract();
        assertEquals(expectedResponse, response.asString());
    }
    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(anyLong());
        var response = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .delete("/api/users/{userId}", user.getId())
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode())
                .extract();
        assertEquals("", response.asString());
    }
}
