package com.jovandjumic.isap_travel_experiences_app.services;

import com.jovandjumic.isap_travel_experiences_app.entities.AppUser;
import com.jovandjumic.isap_travel_experiences_app.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.jovandjumic.isap_travel_experiences_app.utils.TestUtils.setId;

public class AppUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        AppUser appUser = new AppUser();
        appUser.setUsername("johndoe");

        when(userRepository.save(appUser)).thenReturn(appUser);

        AppUser createdAppUser = userService.createUser(appUser);

        assertNotNull(createdAppUser);
        assertEquals("johndoe", createdAppUser.getUsername());
        verify(userRepository, times(1)).save(appUser);
    }

    @Test
    void testGetUserById() throws Exception {
        AppUser appUser = new AppUser();
        setId(appUser, 1L);
        appUser.setUsername("johndoe");

        when(userRepository.findById(1L)).thenReturn(Optional.of(appUser));

        Optional<AppUser> foundUser = userService.getUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("johndoe", foundUser.get().getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<AppUser> foundUser = userService.getUserById(1L);

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllUsers() throws Exception {
        AppUser appUser1 = new AppUser();
        setId(appUser1,1L);
        appUser1.setUsername("johndoe");

        AppUser appUser2 = new AppUser();
        setId(appUser2,2L);
        appUser2.setUsername("janedoe");

        when(userRepository.findAll()).thenReturn(Arrays.asList(appUser1, appUser2));

        List<AppUser> allAppUsers = userService.getAllUsers();

        assertNotNull(allAppUsers);
        assertEquals(2, allAppUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testUpdateUser() throws Exception {
        AppUser appUser = new AppUser();
        setId(appUser,1L);
        appUser.setUsername("johndoe");

        AppUser updatedAppUser = new AppUser();
        updatedAppUser.setUsername("johnsmith");

        when(userRepository.findById(1L)).thenReturn(Optional.of(appUser));
        when(userRepository.save(appUser)).thenReturn(appUser);

        AppUser result = userService.updateUser(1L, updatedAppUser);

        assertNotNull(result);
        assertEquals("johnsmith", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(appUser);
    }

    @Test
    void testUpdateUserNotFound() {
        AppUser updatedAppUser = new AppUser();
        updatedAppUser.setUsername("johnsmith");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        AppUser result = userService.updateUser(1L, updatedAppUser);

        assertNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(0)).save(updatedAppUser);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByUsername() {
        AppUser appUser = new AppUser();
        appUser.setUsername("johndoe");

        when(userRepository.findByUsername("johndoe")).thenReturn(appUser);

        AppUser foundAppUser = userService.findByUsername("johndoe");

        assertNotNull(foundAppUser);
        assertEquals("johndoe", foundAppUser.getUsername());
        verify(userRepository, times(1)).findByUsername("johndoe");
    }

    @Test
    void testFindByUsernameNotFound() {
        when(userRepository.findByUsername("johndoe")).thenReturn(null);

        AppUser foundAppUser = userService.findByUsername("johndoe");

        assertNull(foundAppUser);
        verify(userRepository, times(1)).findByUsername("johndoe");
    }
}
