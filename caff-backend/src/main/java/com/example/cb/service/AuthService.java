package com.example.cb.service;

import com.example.cb.model.MyUserDetails;
import com.example.cb.model.Role;
import com.example.cb.model.RoleEnum;
import com.example.cb.model.User;
import com.example.cb.payload.LoginRequest;
import com.example.cb.payload.LoginResponse;
import com.example.cb.payload.MessageResponse;
import com.example.cb.payload.SignupRequest;
import com.example.cb.repository.RoleRepository;
import com.example.cb.repository.UserRepository;
import com.example.cb.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest){
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        //create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        //TODO: admin function
        //set the roles
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(RoleEnum.ROLE_USER).orElseThrow(()->new RuntimeException("Roles not found")));

        //give the role to the user and save it
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    public ResponseEntity<?> loginUser(LoginRequest loginRequest) throws Exception {
        Authentication authentication;

        //check username and password
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        //if the credentials are correct, authenticate the user and generate a token for it
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwtToken(authentication);

        //cast the user to MyUserDetails and set the roles
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new LoginResponse(jwt, userDetails.getUser().getUser_id(), userDetails.getUsername(), userDetails.getUser().getEmail(), roles));
    }
}
