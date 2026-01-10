package com.example.demo.service;


import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("user123");
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole("RM");
        testUser.setActive(true);
    }
    
    @Test
    void testGetCurrentUser_Success() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(testUser));
        
        // Act
        User result = userService.getCurrentUser("testuser");
        
        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("RM", result.getRole());
        
        verify(userRepository, times(1)).findByUsername("testuser");
    }
    
    @Test
    void testGetCurrentUser_NotFound() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> userService.getCurrentUser("nonexistent")
        );
        
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }
    
    @Test
    void testGetAllUsers_Success() {
        // Arrange
        User user1 = new User();
        user1.setId("user1");
        user1.setUsername("user1");
        
        User user2 = new User();
        user2.setId("user2");
        user2.setUsername("user2");
        
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);
        
        // Act
        List<User> result = userService.getAllUsers();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
        
        verify(userRepository, times(1)).findAll();
    }
    
    @Test
    void testGetAllUsers_EmptyList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList());
        
        // Act
        List<User> result = userService.getAllUsers();
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }
    
    @Test
    void testUpdateUserStatus_Activate() {
        // Arrange
        testUser.setActive(false);
        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // Act
        User result = userService.updateUserStatus("user123", true);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.getActive());
        
        verify(userRepository, times(1)).findById("user123");
        verify(userRepository, times(1)).save(testUser);
    }
    
    @Test
    void testUpdateUserStatus_Deactivate() {
        // Arrange
        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        
        // Act
        User result = userService.updateUserStatus("user123", false);
        
        // Assert
        assertNotNull(result);
        assertFalse(result.getActive());
        
        verify(userRepository, times(1)).findById("user123");
        verify(userRepository, times(1)).save(testUser);
    }
    
    @Test
    void testUpdateUserStatus_UserNotFound() {
        // Arrange
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        
        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> userService.updateUserStatus("nonexistent", true)
        );
        
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById("nonexistent");
        verify(userRepository, never()).save(any(User.class));
    }
}