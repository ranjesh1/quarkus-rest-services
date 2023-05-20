package com.demo.app.ws.controller;


import com.demo.app.ws.entities.Order;
import com.demo.app.ws.service.OrderService;
import org.jboss.resteasy.annotations.jaxrs.PathParam;


import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@Path("/api/users")
public class OrderController {

    @Inject
    private OrderService orderService;
    
    @POST
    @Path("/{userId}/orders")
    public Order createOrder(@PathParam("userId") Long userId, Order order) {

        Order createdOrder = orderService.createOrder(userId, order);
        return createdOrder;
    }
    
    @GET
    @Path("/{userId}/orders/{orderId}")
    public Order getOrder(@PathParam("userId") Long userId, @PathParam("orderId") Long orderId) {

        return orderService.getOrder(userId, orderId);
    }
    
    @PUT
    @Path("/{userId}/orders/{orderId}")
    public Order updateOrder(@PathParam("userId") Long userId, @PathParam("orderId") Long orderId, Order order) {
        return orderService.updateOrder(userId, orderId, order);
    }
    
    @PATCH
    @Path("/{userId}/orders/{orderId}")
    public Order patchUpdateOrder(@PathParam("userId") Long userId, @PathParam("orderId") Long orderId, Order order) {
        return orderService.patchUpdateOrder(userId, orderId, order);
    }
    
    @GET
    @Path("/{userId}/orders")
    public List<Order> getAllOrders(@PathParam("userId") Long userId) {
        return orderService.getAllOrdersByUserId(userId);
    }
    
    @DELETE
    @Path("/{userId}/orders/{orderId}")
    public void deleteOrder(@PathParam("userId") Long userId, @PathParam("orderId") Long orderId) {
        orderService.deleteOrder(userId, orderId);
    }
}
