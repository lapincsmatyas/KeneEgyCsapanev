package com.example.caffbackend.service;

import com.example.cb.CaffBackendApplication;
import com.example.cb.model.Role;
import com.example.cb.model.RoleEnum;
import com.example.cb.model.User;
import com.example.cb.payload.LoginRequest;
import com.example.cb.payload.SignupRequest;
import com.example.cb.repository.UserRepository;
import com.example.cb.service.AuthService;
import com.example.cb.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CaffBackendApplication.class)
public class AuthServiceTests {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthService authService;
    @InjectMocks
    private User user;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;


    @Test
    void testRegisterWithExistedEmail() {
        SignupRequest signupRequest = new SignupRequest("username", "email@email.com", "psw");

        when(userRepository.existsByEmail("email@email.com")).thenReturn(true);

        ResponseEntity<?> responseEntity = authService.registerUser(signupRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testRegisterWithExistedPassword() {
        SignupRequest signupRequest = new SignupRequest("username", "email@email.com", "psw");

        when(userRepository.existsByUsername("username")).thenReturn(true);

        ResponseEntity<?> responseEntity = authService.registerUser(signupRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


   /* @Test
    void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest("username", "psw");
        Role role = new Role(RoleEnum.ROLE_USER);
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        user.setId(1);
        user.setEmail("email@email.com");
        user.setUsername("username");
        user.setRoles(roles);
        user.setPassword("psw");

        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        ResponseEntity<?> responseEntity = authService.loginUser(loginRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testLoginWithEmptyUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest("username", "psw");

        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(null);

        ResponseEntity<?> responseEntity = authService.loginUser(loginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }*/
}
