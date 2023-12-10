package com.demo.app.ws.repository;

import com.demo.app.ws.entities.Order;
import com.demo.app.ws.entities.User;
import com.demo.app.ws.service.OrderService;
import com.demo.app.ws.service.UserService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class OrderRepositoryTest {
    @Inject
    private OrdersRepository ordersRepository;

    @Inject
    UserService userService;

    @Inject
    OrderService orderService;


    @BeforeEach
    void setup() {
    }

    @Test
    void testFndAllByUser() {
        //Given
        User user = new User();
        user.setFirstName("Steve");
        user.setLastName("Rob");
        user.setEmail(UUID.randomUUID().toString() + "@gmail.com");
        user.setFirstLineOfAddress("10 Oliver");
        user.setSecondLineOfAddress("Commercial street");
        user.setTown("London");
        user.setPostCode("HA4 7EW");

        User createdUser = userService.createUser(user);

        Order order = new Order();
        order.setUser(user);
        order.setCompletedStatus(false);
        order.setDescription("Apple Phone");
        order.setPriceInPence(10000L);
        orderService.createOrder(createdUser.getId(), order);


        Order order2 = new Order();
        order2.setUser(user);
        order2.setCompletedStatus(false);
        order2.setDescription("Samsung galaxy Phone");
        order2.setPriceInPence(50000L);

        orderService.createOrder(createdUser.getId(), order2);

        //When
        List<Order> allOrders = ordersRepository.findAllByUser(user);
        //Then
        assertTrue(allOrders.size() > 0);
        assertEquals(order.getDescription(), allOrders.get(0).getDescription());
        assertEquals(order.getPriceInPence(), allOrders.get(0).getPriceInPence());
    }

    @Test
    void testFindByIdAndUserId() {
        //Given
        User user = new User();
        user.setFirstName("Steve");
        user.setLastName("Rob");
        user.setEmail(UUID.randomUUID().toString() + "@gmail.com");
        user.setFirstLineOfAddress("10 Oliver");
        user.setSecondLineOfAddress("Commercial street");
        user.setTown("London");
        user.setPostCode("HA4 7EW");

        User createdUser = userService.createUser(user);

        Order order = new Order();
        order.setUser(user);
        order.setCompletedStatus(false);
        order.setDescription("Apple Phone");
        order.setPriceInPence(10000L);
        orderService.createOrder(createdUser.getId(), order);

        //When
        Order orderFound = ordersRepository.findByIdAndUserId(order.getId(), user.getId()).get();
        //Then
        assertNotNull(orderFound);
        assertEquals(order.getDescription(), orderFound.getDescription());
        assertEquals(order.getPriceInPence(), orderFound.getPriceInPence());
        assertEquals(order.getUser().getId(), orderFound.getUser().getId());
    }
}
