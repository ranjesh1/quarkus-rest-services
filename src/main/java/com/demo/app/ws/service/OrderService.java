package com.demo.app.ws.service;

import com.demo.app.ws.entities.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(Long userId, Order order);

    Order updateOrder(Long userId, Long orderId, Order order);

    Order patchUpdateOrder(Long userId, Long orderId, Order order);

    Order getOrder(Long userId, Long orderId);

    List<Order> getAllOrdersByUserId(Long userId);

    void deleteOrder(Long userId, Long orderId);
}
