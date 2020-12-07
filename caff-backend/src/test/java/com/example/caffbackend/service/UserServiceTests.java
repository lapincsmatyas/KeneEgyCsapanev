package com.example.caffbackend.service;

import com.example.cb.CaffBackendApplication;
import com.example.cb.model.User;
import com.example.cb.payload.SignupRequest;
import com.example.cb.repository.UserRepository;
import com.example.cb.service.UserService;
import com.example.cb.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CaffBackendApplication.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
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

        ResponseEntity<?> responseEntity = userService.registerUser(signupRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void testRegisterWithExistedPassword() {
        SignupRequest signupRequest = new SignupRequest("username", "email@email.com", "psw");

        when(userRepository.existsByUsername("username")).thenReturn(true);

        ResponseEntity<?> responseEntity = userService.registerUser(signupRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    /*
    @Test
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

        ResponseEntity<?> responseEntity = userService.loginUser(loginRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testLoginWithEmptyUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest("username", "psw");

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(loginRequest.getUsername())).thenReturn(null);

        ResponseEntity<?> responseEntity = userService.loginUser(loginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }*/

    @Test
    void testFindById() {

        when(userRepository.findById(anyLong())).thenReturn(user);

        User foundUser = userService.findById(1);

        assertEquals(user, foundUser);
    }

    @Test
    void testFindByWrongId() {
        when(userRepository.findById(anyLong())).thenReturn(null);

        User foundUser = userService.findById(1);

        assertNull(foundUser);
    }

    @Test
    void testFindByUsername() {
        when(userRepository.findByUsername("username")).thenReturn(user);

        User foundUser = userService.findByUsername("username");

        assertEquals(user, foundUser);
    }

    @Test
    void testFindByWrongUsername() {
        when(userRepository.findByUsername("username")).thenReturn(null);

        User foundUser = userService.findByUsername("username");

        assertNull(foundUser);
    }

    @Test
    void testFindAll() {
        User anotherUser = new User();

        when(userRepository.findAll()).thenReturn(List.of(user, anotherUser));

        List<User> userList = userService.findAll();

        assertEquals(2, userList.size());
    }
}
