package com.demo.app.ws.controller;

import com.demo.app.ws.entities.User;
import com.demo.app.ws.service.UserService;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.*;
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
    public User updateUser(@PathParam Long id, User user) {
        return userService.updateUser(id, user);
    }

    @PATCH
    @Path("/{id}")
    public User patchUpdateUser(@PathParam Long id, User user) {
        return userService.patchUpdateUser(id, user);
    }

    @DELETE
    @Path("/{id}")
    public void deleteUser(@PathParam Long id) {
        userService.deleteUser(id);
    }

}
