package com.example.cb.service;

import com.example.cb.CaffBackendApplication;
import com.example.cb.model.MyUserDetails;
import com.example.cb.model.User;
import com.example.cb.repository.UserRepository;
import com.example.cb.service.UserService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest(classes = CaffBackendApplication.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private MyUserDetails myUserDetails;
    @InjectMocks
    private UserService userService;
    @InjectMocks
    private User user;
    @Autowired
    private WebApplicationContext context;


    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

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

    /*@Test
    void testGetUserProfile(){
        Authentication auth = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        MyUserDetails myUserDetails = Mockito.mock(MyUserDetails.class);

        User profile = new User("user", "user@gmail.com", "psw");

        //when(myUserDetails.getUser()).thenReturn(profile);

        ResponseEntity<?> responseEntity = userService.getUserProfile(profile.getUsername());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }*/

   /* @Test
    void testUpdateUserProfile(){

    }*/

}
