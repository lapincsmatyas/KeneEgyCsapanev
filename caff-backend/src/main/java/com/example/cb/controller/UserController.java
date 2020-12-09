package com.example.cb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cb.payload.UserPayload;
import com.example.cb.service.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	
	@GetMapping("/{username}")
	public ResponseEntity<?> getUserProfile(@PathVariable String username){
		return userService.getUserProfile(username);
	}
	
	@PutMapping("/{username}")
	public ResponseEntity<?> updateUserProfile(@PathVariable String username, @RequestBody UserPayload user){
		return userService.updateUserProfile(username, user);
	}
}
