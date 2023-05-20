package com.demo.app.ws.service.impl;

import com.demo.app.ws.entities.User;
import com.demo.app.ws.repository.UsersRepository;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UsersRepository usersRepository;

    private User user;
    private User user2;

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

        user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Tony");
        user2.setLastName("Taylor");
        user2.setEmail("tonytaylor@gmail.com");
        user2.setFirstLineOfAddress("12 Avenue");
        user2.setSecondLineOfAddress("Avenue street");
        user2.setTown("London");
        user2.setPostCode("HA6 0EW");

    }

    @Test
    void testGetUserById() {
        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User userFound = userService.getUserById(user.getId());
        assertNotNull(userFound);
        assertEquals(user.getId(), userFound.getId());
        assertEquals(user.getEmail(), userFound.getEmail());
        assertEquals(user.getFirstName(), userFound.getFirstName());
        assertEquals(user.getLastName(), userFound.getLastName());
        assertEquals(user.getPostCode(), userFound.getPostCode());
        assertEquals(user.getFirstLineOfAddress(), userFound.getFirstLineOfAddress());
        assertEquals(user.getSecondLineOfAddress(), userFound.getSecondLineOfAddress());
        assertEquals(user.getTown(), userFound.getTown());
    }

    @Test
    void shouldThrowExceptionWhenGetUserHasWrongId() {
        when(usersRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUserById(10000L));
    }

    @Test
    void testGetUsers() {

        List<User> users = Arrays.asList(user, user2);

        when(usersRepository.findAll()).thenReturn(users);

        List<User> usersReceived = userService.getUsers();

        assertEquals(users, usersReceived);
        assertEquals(users.size(), usersReceived.size());
        assertEquals(users.get(0).getId(), usersReceived.get(0).getId());
        assertEquals(users.get(0).getFirstName(), usersReceived.get(0).getFirstName());
        assertEquals(users.get(0).getLastName(), usersReceived.get(0).getLastName());
        assertEquals(users.get(0).getEmail(), usersReceived.get(0).getEmail());
        assertEquals(users.get(0).getFirstLineOfAddress(), usersReceived.get(0).getFirstLineOfAddress());
        assertEquals(users.get(0).getSecondLineOfAddress(), usersReceived.get(0).getSecondLineOfAddress());
        assertEquals(users.get(0).getSecondLineOfAddress(), usersReceived.get(0).getSecondLineOfAddress());
    }

    @Test
    void testCreateUser() {
        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(usersRepository.save(any(User.class))).thenReturn(user);

        User createNewUser = new User();
        createNewUser.setFirstName(user.getFirstName());
        createNewUser.setLastName(user.getLastName());
        createNewUser.setEmail(user.getEmail());
        createNewUser.setPostCode(user.getPostCode());
        createNewUser.setFirstLineOfAddress(user.getFirstLineOfAddress());
        createNewUser.setSecondLineOfAddress(user.getSecondLineOfAddress());
        createNewUser.setTown(user.getTown());

        User createdUser = userService.createUser(createNewUser);

        assertNotNull(createdUser);
        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getFirstName(), createdUser.getFirstName());
        assertEquals(user.getLastName(), createdUser.getLastName());
        assertEquals(user.getPostCode(), createdUser.getPostCode());
        assertEquals(user.getFirstLineOfAddress(), createdUser.getFirstLineOfAddress());
        assertEquals(user.getSecondLineOfAddress(), createdUser.getSecondLineOfAddress());
        assertEquals(user.getTown(), createdUser.getTown());
    }

    @Test
    void testUpdateUser() {

        User updateUser = new User();
        updateUser.setFirstName("Steve");
        updateUser.setLastName("Rob");
        updateUser.setEmail("steverob@gmail.com");
        updateUser.setPostCode("HA5 2EW");
        updateUser.setFirstLineOfAddress("10 Oliver");
        updateUser.setSecondLineOfAddress("Commercial street");
        updateUser.setTown("London");

        User updateUserWithId = new User();
        new ModelMapper().map(updateUser, updateUserWithId);
        updateUserWithId.setId(1L);

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(updateUserWithId));

        when(usersRepository.save(any(User.class))).thenReturn(updateUserWithId);

        User savedUser = userService.updateUser(updateUserWithId.getId(), updateUser);

        assertNotNull(savedUser);
        assertEquals(updateUserWithId.getId(), savedUser.getId());
        assertEquals(updateUserWithId.getEmail(), savedUser.getEmail());
        assertEquals(updateUserWithId.getFirstName(), savedUser.getFirstName());
        assertEquals(updateUserWithId.getLastName(), savedUser.getLastName());
        assertEquals(updateUserWithId.getPostCode(), savedUser.getPostCode());
        assertEquals(updateUserWithId.getFirstLineOfAddress(), savedUser.getFirstLineOfAddress());
        assertEquals(updateUserWithId.getSecondLineOfAddress(), savedUser.getSecondLineOfAddress());
        assertEquals(updateUserWithId.getTown(), savedUser.getTown());
    }

    @Test
    void testPatchUpdateUser() {

        User updateUser = new User();
        updateUser.setFirstName("Steveupdated");
        updateUser.setLastName("Robupdated");

        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));

        when(usersRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.patchUpdateUser(user.getId(), updateUser);

        assertNotNull(savedUser);
        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getFirstName(), savedUser.getFirstName());
        assertEquals(user.getLastName(), savedUser.getLastName());
        assertEquals(user.getPostCode(), savedUser.getPostCode());
        assertEquals(user.getFirstLineOfAddress(), savedUser.getFirstLineOfAddress());
        assertEquals(user.getSecondLineOfAddress(), savedUser.getSecondLineOfAddress());
        assertEquals(user.getTown(), savedUser.getTown());
    }

    @Test
    void testDeleteUser() {

        when(usersRepository.findById(anyLong())).thenReturn(Optional.of(user));
        doNothing().when(usersRepository).delete(any(User.class));

        userService.deleteUser(user.getId());

        verify(usersRepository, times(1)).deleteById(user.getId());

    }

}
