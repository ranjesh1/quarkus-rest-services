package com.demo.app.ws.repository;

import com.demo.app.ws.entities.User;
import com.demo.app.ws.service.UserService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@Disabled
public class UserRepositoryTest {
    @Inject
    private UsersRepository usersRepository;

    @Inject
    UserService userService;


    @Test
    void testFndByEmail() {
        //Given
        User user = new User();
        user.setFirstName("Steve");
        user.setLastName("Rob");
        user.setEmail(UUID.randomUUID().toString() + "@gmail.com");
        user.setFirstLineOfAddress("10 Oliver");
        user.setSecondLineOfAddress("Commercial street");
        user.setTown("London");
        user.setPostCode("HA4 7EW");
        userService.createUser(user);

        //When
        User found = usersRepository.findByEmail(user.getEmail()).get();
        //Then
        assertEquals(user.getEmail(), found.getEmail());
    }
}
