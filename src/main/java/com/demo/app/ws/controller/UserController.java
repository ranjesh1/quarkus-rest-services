package com.demo.app.ws.controller;

import com.demo.app.ws.entities.User;
import com.demo.app.ws.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.jboss.resteasy.reactive.RestPath;
//import org.jboss.resteasy.annotations.jaxrs.PathParam;


import java.util.List;


@Path("/api/users")
public class UserController {

    @Inject
    UserService userService;

    @GET
    @Path("/{id}")
    public User getUser(@PathParam("id")  Long id) {

        return userService.getUserById(id);
    }

    @GET
    public List<User> getAllUsers() {
        return userService.getUsers();
    }

    @POST
    public User createUser(final User user) {
        User createdUser = userService.createUser(user);
        return createdUser;
    }

    @PUT
    @Path("/{id}")
    public User updateUser(@RestPath Long id, User user) {
        return userService.updateUser(id, user);
    }

    @PATCH
    @Path("/{id}")
    public User patchUpdateUser(@RestPath Long id, User user) {
        return userService.patchUpdateUser(id, user);
    }

    @DELETE
    @Path("/{id}")
    public void deleteUser(@RestPath Long id) {
        userService.deleteUser(id);
    }

}
