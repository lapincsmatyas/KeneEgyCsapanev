package com.example.cb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cb.model.MyUserDetails;
import com.example.cb.model.User;
import com.example.cb.payload.MessageResponse;
import com.example.cb.payload.UserPayload;
import com.example.cb.service.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService service;
	
	@GetMapping("/{username}")
	public ResponseEntity<?> getUserProfile(@PathVariable String username){
		MyUserDetails userDetails = (MyUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!userDetails.getUsername().equals(username)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Other users are not available " + userDetails.getUsername()));
		User user = null;
		user = service.findByUsername(username);
		if(user==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("User not found"));
		UserPayload res = new UserPayload(user.getUser_id(), user.getUsername(), user.getEmail(), user.getPassword(), user.getRoles());
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}
	
	@PutMapping("/{username}")
	public ResponseEntity<?> updateUserProfile(@PathVariable String username, @RequestBody UserPayload user){
		MyUserDetails userDetails = (MyUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(!userDetails.getUsername().equals(username)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Other users are not available"));
		User userdb = null;
		userdb = service.findByUsername(username);
		if(userdb==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("User not found"));
		userdb.setUsername(user.getUsername());
		userdb.setEmail(user.getEmail());
		userdb.setPassword(user.getPassword());
		service.save(userdb);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("User was updated successfully"));
	}
	
    /*@GetMapping("/welcome")
    public String welcome() {
        return "Welcome user";
    }*/
}
