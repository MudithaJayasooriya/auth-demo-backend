package com.example.authdemo.service;

import com.example.authdemo.model.User;
import com.example.authdemo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final String VALID_USERNAME = "testuser";
    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_PASSWORD = "Password123";
    private static final String HASHED_PASSWORD = "hashed123";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void signup_WhenEmailAlreadyExists_ShouldThrowException() {
        when(userRepository.existsByUsername(VALID_USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.signup(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD));

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void signup_WhenUsernameAlreadyExists_ShouldThrowException() {
        when(userRepository.existsByUsername(VALID_USERNAME)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.signup(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD));

        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

//    @Test
//    void signup_WhenPasswordTooShort_ShouldThrowException() {
//        String shortPassword = "short";
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//                () -> userService.signup(VALID_USERNAME, VALID_EMAIL, shortPassword));
//
//        assertEquals("Password must be at least 8 characters", exception.getMessage());
//    }

    @Test
    void signup_WithValidData_ShouldSaveUserSuccessfully() {
        when(userRepository.existsByUsername(VALID_USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(VALID_PASSWORD)).thenReturn(HASHED_PASSWORD);

        User expectedUser = new User(VALID_USERNAME, VALID_EMAIL, HASHED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        User savedUser = userService.signup(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD);

        assertNotNull(savedUser);
        assertEquals(VALID_EMAIL, savedUser.getEmail());
        assertEquals(HASHED_PASSWORD, savedUser.getPassword());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(VALID_PASSWORD);
    }

    @Test
    void login_WithCorrectCredentials_ShouldReturnTrue() {
        User mockUser = new User(VALID_USERNAME, VALID_EMAIL, HASHED_PASSWORD);
        when(userRepository.findByUsername(VALID_USERNAME)).thenReturn(java.util.Optional.of(mockUser));
        when(passwordEncoder.matches(VALID_PASSWORD, HASHED_PASSWORD)).thenReturn(true);

        boolean result = userService.login(VALID_USERNAME, VALID_PASSWORD);

        assertTrue(result);
    }

    @Test
    void login_WithWrongPassword_ShouldReturnFalse() {
        User mockUser = new User(VALID_USERNAME, VALID_EMAIL, HASHED_PASSWORD);
        when(userRepository.findByUsername(VALID_USERNAME)).thenReturn(java.util.Optional.of(mockUser));
        when(passwordEncoder.matches("wrongpass", HASHED_PASSWORD)).thenReturn(false);

        boolean result = userService.login(VALID_USERNAME, "wrongpass");

        assertFalse(result);
    }

    @Test
    void login_WithNonExistentUsername_ShouldReturnFalse() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(java.util.Optional.empty());

        boolean result = userService.login("nonexistent", VALID_PASSWORD);

        assertFalse(result);
    }
}