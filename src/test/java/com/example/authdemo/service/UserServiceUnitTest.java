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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    private static final String VALID_USERNAME = "testuser";
    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_PASSWORD = "Password123";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;
    @Test
    void signup_WhenEmailExists_ShouldThrowException() {
        when(userRepository.existsByUsername(VALID_USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.signup(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD));
        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
    @Test
    void signup_WithValidData_ShouldSaveUserSuccessfully() {
        when(userRepository.existsByUsername(VALID_USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(VALID_EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(VALID_PASSWORD)).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User savedUser = userService.signup(VALID_USERNAME, VALID_EMAIL, VALID_PASSWORD);

        assertNotNull(savedUser);
        assertEquals(VALID_USERNAME, savedUser.getUsername());
        assertEquals(VALID_EMAIL, savedUser.getEmail());
        assertEquals("hashedPassword", savedUser.getPassword());

        verify(passwordEncoder, times(1)).encode(VALID_PASSWORD);
        verify(userRepository, times(1)).save(any(User.class));
    }
}
