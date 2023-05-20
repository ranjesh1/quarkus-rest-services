package com.demo.app.ws.service.impl;

import com.demo.app.ws.entities.Order;
import com.demo.app.ws.entities.User;
import com.demo.app.ws.repository.OrdersRepository;
import com.demo.app.ws.repository.UsersRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@QuarkusTest
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private UsersRepository usersRepository;

    private User user;

    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setId(1L);
        user.setFirstName("Steve");
        user.setLastName("Rob");
        user.setEmail("steverob@gmail.com");
        user.setFirstLineOfAddress("10 Oliver");
        user.setSecondLineOfAddress("Commercial street");
        user.setTown("London");
        user.setPostCode("HA4 7EW");

        order = new Order();
        order.setUser(user);
        order.setCompletedStatus(false);
        order.setId(2L);
        order.setDescription("Apple Phone");
        order.setPriceInPence(10000L);

    }

    @Test
    void testGetOrder() {
        when(ordersRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(order));

        Order orderRetrieved = orderService.getOrder(user.getId(), order.getId());
        assertEquals(order.getId(), orderRetrieved.getId());
        assertEquals(order.getDescription(), orderRetrieved.getDescription());
        assertEquals(order.getPriceInPence(), orderRetrieved.getPriceInPence());
        assertEquals(order.getUser().getId(), orderRetrieved.getUser().getId());

    }

    @Test
    void testGetOrder_InvalidOrderId() {
        when(ordersRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> orderService.getOrder(user.getId(), 99999L));
    }

    @Test
    void testGetAllOrdersByUserId() {

        Order order2 = new Order();
        order2.setUser(user);
        order2.setCompletedStatus(false);
        order2.setId(3L);
        order2.setDescription("Samsung Phone");
        order2.setPriceInPence(20000L);
        List<Order> orders = Arrays.asList(order, order2);

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(ordersRepository.findAllByUser(any(User.class))).thenReturn(orders);

        List<Order> ordersReceived = orderService.getAllOrdersByUserId(user.getId());

        assertEquals(orders.size(), ordersReceived.size());
        assertEquals(orders.get(0).getPriceInPence(), ordersReceived.get(0).getPriceInPence());
        assertEquals(orders.get(0).getDescription(), ordersReceived.get(0).getDescription());

    }

    @Test
    void testCreateOrder() {

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(ordersRepository.save(any(Order.class))).thenReturn(order);

        Order createNewOrder = new Order();
        createNewOrder.setDescription(order.getDescription());
        createNewOrder.setPriceInPence(order.getPriceInPence());

        Order createdOrder = orderService.createOrder(user.getId(), createNewOrder);

        assertNotNull(createdOrder);
        assertEquals(order.getDescription(), createdOrder.getDescription());
        assertEquals(order.getPriceInPence(), createdOrder.getPriceInPence());

    }

    @Test
    void testUpdateOrder() {

        when(ordersRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(order));

        when(ordersRepository.save(any(Order.class))).thenReturn(order);

        Order updateOrder = new Order();
        updateOrder.setDescription("Changed to Samsung Phone");
        updateOrder.setPriceInPence(30000L);
        order.setDescription(updateOrder.getDescription());
        order.setPriceInPence(updateOrder.getPriceInPence());

        Order updatedOrder = orderService.updateOrder(order.getUser().getId(), order.getId(), updateOrder);

        assertEquals(order.getDescription(), updatedOrder.getDescription());
        assertEquals(order.getPriceInPence(), updatedOrder.getPriceInPence());
        assertEquals(order.getId(), updatedOrder.getId());
        assertEquals(order.getUser().getId(), updatedOrder.getUser().getId());

    }

    @Test
    void testPatchUpdateOrder() {

        Order updateOrder = new Order();
        updateOrder.setDescription("Changed to Samsung Phone");
        order.setDescription(updateOrder.getDescription());
        when(ordersRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(order));
        when(ordersRepository.save(any(Order.class))).thenReturn(order);

        Order updatedOrder = orderService.patchUpdateOrder(order.getUser().getId(), order.getId(), updateOrder);

        assertEquals(order.getDescription(), updatedOrder.getDescription());
        assertEquals(order.getPriceInPence(), updatedOrder.getPriceInPence());
        assertEquals(order.getId(), updatedOrder.getId());
        assertEquals(order.getUser().getId(), updatedOrder.getUser().getId());

    }

    @Test
    void testDeleteOrder() {

        when(ordersRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(order));
        doNothing().when(ordersRepository).delete(any(Order.class));

        orderService.deleteOrder(user.getId(), order.getId());

        verify(ordersRepository, times(1)).findByIdAndUserId(order.getId(), user.getId());
        verify(ordersRepository, times(1)).deleteById(order.getId());

    }

}
