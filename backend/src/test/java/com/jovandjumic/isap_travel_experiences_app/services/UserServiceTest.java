package com.jovandjumic.isap_travel_experiences_app.services;

import com.jovandjumic.isap_travel_experiences_app.entities.User;
import com.jovandjumic.isap_travel_experiences_app.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private void setId(Object entity, Long idValue) throws Exception {
        Field idField = entity.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(entity, idValue);
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setUsername("johndoe");

        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("johndoe", createdUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUserById() throws Exception {
        User user = new User();
        setId(user, 1L);
        user.setUsername("johndoe");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("johndoe", foundUser.get().getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserById(1L);

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllUsers() throws Exception {
        User user1 = new User();
        setId(user1,1L);
        user1.setUsername("johndoe");

        User user2 = new User();
        setId(user2,2L);
        user2.setUsername("janedoe");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> allUsers = userService.getAllUsers();

        assertNotNull(allUsers);
        assertEquals(2, allUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testUpdateUser() throws Exception {
        User user = new User();
        setId(user,1L);
        user.setUsername("johndoe");

        User updatedUser = new User();
        updatedUser.setUsername("johnsmith");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser(1L, updatedUser);

        assertNotNull(result);
        assertEquals("johnsmith", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserNotFound() {
        User updatedUser = new User();
        updatedUser.setUsername("johnsmith");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.updateUser(1L, updatedUser);

        assertNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(0)).save(updatedUser);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByUsername() {
        User user = new User();
        user.setUsername("johndoe");

        when(userRepository.findByUsername("johndoe")).thenReturn(user);

        User foundUser = userService.findByUsername("johndoe");

        assertNotNull(foundUser);
        assertEquals("johndoe", foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername("johndoe");
    }

    @Test
    void testFindByUsernameNotFound() {
        when(userRepository.findByUsername("johndoe")).thenReturn(null);

        User foundUser = userService.findByUsername("johndoe");

        assertNull(foundUser);
        verify(userRepository, times(1)).findByUsername("johndoe");
    }
}
