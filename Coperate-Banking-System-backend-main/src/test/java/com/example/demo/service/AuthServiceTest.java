package com.example.demo.service;


import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private JwtUtil jwtUtil;
    
    @InjectMocks
    private AuthService authService;
    
    private User testUser;
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;
    
    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setId("user123");
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole("RM");
        testUser.setActive(true);
        
        // Setup login request
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");
        
        // Setup register request
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setEmail("new@example.com");
        registerRequest.setPassword("newpass123");
        registerRequest.setRole("RM");
    }
    
    @Test
    void testLogin_Success() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString(), anyString())).thenReturn("jwt-token");
        
        // Act
        AuthResponse response = authService.login(loginRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("RM", response.getRole());
        assertEquals("user123", response.getUserId());
        
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, times(1)).matches("password123", "encodedPassword");
        verify(jwtUtil, times(1)).generateToken("testuser", "RM", "user123");
    }
    
    @Test
    void testLogin_UserNotFound() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        
        // Act & Assert
        UnauthorizedException exception = assertThrows(
            UnauthorizedException.class,
            () -> authService.login(loginRequest)
        );
        
        assertEquals("Invalid credentials", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
    
    @Test
    void testLogin_UserDeactivated() {
        // Arrange
        testUser.setActive(false);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        
        // Act & Assert
        UnauthorizedException exception = assertThrows(
            UnauthorizedException.class,
            () -> authService.login(loginRequest)
        );
        
        assertEquals("User account is deactivated", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("testuser");
    }
    
    @Test
    void testLogin_WrongPassword() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        
        // Act & Assert
        UnauthorizedException exception = assertThrows(
            UnauthorizedException.class,
            () -> authService.login(loginRequest)
        );
        
        assertEquals("Invalid credentials", exception.getMessage());
        verify(passwordEncoder, times(1)).matches("password123", "encodedPassword");
    }
    
    @Test
    void testRegister_Success() {
        // Arrange
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtUtil.generateToken(anyString(), anyString(), anyString())).thenReturn("jwt-token");
        
        // Act
        AuthResponse response = authService.register(registerRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertNotNull(response.getUsername());
        
        verify(userRepository, times(1)).existsByUsername("newuser");
        verify(userRepository, times(1)).existsByEmail("new@example.com");
        verify(passwordEncoder, times(1)).encode("newpass123");
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    void testRegister_UsernameExists() {
        // Arrange
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        
        // Act & Assert
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> authService.register(registerRequest)
        );
        
        assertEquals("Username already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByUsername("newuser");
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    void testRegister_EmailExists() {
        // Arrange
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        
        // Act & Assert
        BadRequestException exception = assertThrows(
            BadRequestException.class,
            () -> authService.register(registerRequest)
        );
        
        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByEmail("new@example.com");
        verify(userRepository, never()).save(any(User.class));
    }
}