package com.demo.app.ws.service.impl;

import com.demo.app.ws.entities.Order;
import com.demo.app.ws.entities.User;
import com.demo.app.ws.repository.OrdersRepository;
import com.demo.app.ws.repository.UsersRepository;
import com.demo.app.ws.service.OrderService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class OrderServiceImpl implements OrderService {

    @Inject
    private OrdersRepository ordersRepository;

    @Inject
    private UsersRepository usersRepository;

    @Override
    public Order createOrder(Long userId, Order order) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " Not found"));

        order.setUser(user);

        return ordersRepository.save(order);
    }

    @Override
    public Order getOrder(Long userId, Long orderId) {

        return ordersRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " and order id " + orderId + " Not found"));
    }

    @Override
    public List<Order> getAllOrdersByUserId(Long userId) {

        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " Not found"));

        return ordersRepository.findAllByUser(user);

    }

    @Override
    public Order updateOrder(Long userId, Long orderId, Order order) {
        Order orderFound = ordersRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " and order id " + orderId + " Not found"));

        orderFound.setDescription(order.getDescription());
        orderFound.setPriceInPence(order.getPriceInPence());
        orderFound.setCompletedStatus(order.isCompletedStatus());

        return ordersRepository.save(orderFound);
    }

    @Override
    public Order patchUpdateOrder(Long userId, Long orderId, Order order) {

        Order orderFound = ordersRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " and order id " + orderId + " Not found"));

        if (order.getDescription() != null)
            orderFound.setDescription(order.getDescription());

        if (order.getPriceInPence() != 0)
            orderFound.setPriceInPence(order.getPriceInPence());

        return ordersRepository.save(orderFound);

    }

    @Override
    public void deleteOrder(Long userId, Long orderId) {
        Order order = ordersRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " and order id " + orderId + " Not found"));
        ordersRepository.deleteById(orderId);
    }
}
