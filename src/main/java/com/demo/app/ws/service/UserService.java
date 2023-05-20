package com.demo.app.ws.service;

import com.demo.app.ws.entities.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User updateUser(Long id, User user);

    User patchUpdateUser(Long id, User user);

    User getUserById(Long id);

    List<User> getUsers();

    void deleteUser(Long id);
}
