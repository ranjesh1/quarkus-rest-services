package com.demo.app.ws.controller;

import com.demo.app.ws.entities.Order;
import com.demo.app.ws.entities.User;
import com.demo.app.ws.service.OrderService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@QuarkusTest
public class OrderControllerTest {

    @InjectMock
    private OrderService orderService;

    private Order order;
    private User user;

    @Inject
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        user = new User();
        user.setFirstName("Steve");
        user.setLastName("Rob");
        user.setEmail("steverob@gmail.com");
        user.setId(1L);
        user.setFirstLineOfAddress("11 Avenue");
        user.setSecondLineOfAddress("Commercial street");
        user.setTown("London");
        user.setPostCode("HA7 0EW");

        order = new Order();
        order.setUser(user);
        order.setCompletedStatus(false);
        order.setId(2L);
        order.setDescription("Apple Phone");
        order.setPriceInPence(10000L);

    }

    @Test
    void testGetOrder() throws Exception {
        String expected = objectMapper.writeValueAsString(order);
        when(orderService.getOrder(anyLong(), anyLong())).thenReturn(order);

        var response = given().contentType(ContentType.JSON)
                .when()
                .get("/api/users/{userId}/orders/{orderId}", user.getId(), order.getId())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract();

        assertEquals(expected, response.asString());
    }

    @Test
    public void testGetAllOrders() throws Exception {
        Order order2 = new Order();
        order2.setUser(user);
        order2.setCompletedStatus(false);
        order2.setId(3L);
        order2.setDescription("Samsung Phone");
        order2.setPriceInPence(30000L);

        List<Order> orders = Arrays.asList(order, order2);
        String expected = objectMapper.writeValueAsString(orders);

        when(orderService.getAllOrdersByUserId(anyLong())).thenReturn(orders);

        var response = given().contentType(ContentType.JSON)
                .when()
                .get("/api/users/{userId}/orders", user.getId())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract();

        assertEquals(expected, response.asString());
    }

   @Test
    void testCreateOrder() throws Exception {
       Order createOrder = new Order();
       createOrder.setDescription(order.getDescription());
       createOrder.setPriceInPence(order.getPriceInPence());

       objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

       String createOrderString = objectMapper.writeValueAsString(createOrder);
       String expectedOrderString = objectMapper.writeValueAsString(order);

       when(orderService.createOrder(anyLong(), any(Order.class))).thenReturn(order);

       var response = given().contentType(ContentType.JSON)
               .accept(ContentType.JSON)
               .body(createOrderString)
               .when()
               .post("/api/users/{userId}/orders", user.getId())
               .then()
               .statusCode(Response.Status.OK.getStatusCode())
               .extract();
       assertEquals(expectedOrderString, response.asString());
    }


    @Test
    void testUpdateOrder() throws Exception {
        Order updateOrder = new Order();
        updateOrder.setDescription("Updated Apple Phone");
        updateOrder.setPriceInPence(20000L);
        order.setDescription(updateOrder.getDescription());
        order.setPriceInPence(updateOrder.getPriceInPence());

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String updateOrderString = objectMapper.writeValueAsString(updateOrder);
        String expectedOrderString = objectMapper.writeValueAsString(order);

        when(orderService.updateOrder(anyLong(), anyLong(), any(Order.class))).thenReturn(order);

        var response = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(updateOrderString)
                .when()
                .put("/api/users/{userId}/orders/{orderId}", user.getId(), order.getId())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract();
        assertEquals(expectedOrderString, response.asString());

    }



    @Test
    void testPatchUpdateOrder() throws Exception {
        Order updateOrder = new Order();
        updateOrder.setDescription("Updated Apple Phone");
        order.setDescription(updateOrder.getDescription());

        // exclude null fields
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        String updateOrderString = objectMapper.writeValueAsString(updateOrder);
        String expectedOrderString = objectMapper.writeValueAsString(order);

        when(orderService.patchUpdateOrder(anyLong(), anyLong(), any(Order.class))).thenReturn(order);

        var response = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(updateOrderString)
                .when()
                .patch("/api/users/{userId}/orders/{orderId}", user.getId(), order.getId())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract();
        assertEquals(expectedOrderString, response.asString());
    }


    @Test
    void testDeleteOrder() throws Exception {
        doNothing().when(orderService).deleteOrder(anyLong(), anyLong());
        var response = given().contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .delete("/api/users/{userId}/orders/{orderId}", user.getId(), order.getId())
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode())
                .extract();
        assertEquals("", response.asString());
    }
}
