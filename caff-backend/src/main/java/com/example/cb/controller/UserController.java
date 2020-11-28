package com.example.cb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cb.payload.MessageResponse;
import com.example.cb.payload.UserPayload;
import com.example.cb.service.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService service;
	
	@GetMapping("/{username}")//TODO
	public ResponseEntity<UserPayload> getUserProfile(@PathVariable String username){
		return null;
	}
	
	@PutMapping("/{username}")//TODO
	public ResponseEntity<MessageResponse> updateUserProfile(@RequestBody UserPayload user){
		return null;
	}
	
    /*@GetMapping("/welcome")
    public String welcome() {
        return "Welcome user";
    }*/
}
