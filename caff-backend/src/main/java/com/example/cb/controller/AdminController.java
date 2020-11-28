package com.example.cb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cb.payload.CAFFPreview;
import com.example.cb.payload.MessageResponse;
import com.example.cb.payload.UserPayload;
import com.example.cb.service.CAFFService;
import com.example.cb.service.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private UserService userservice;
	@Autowired
	private CAFFService caffservice;
	
	@GetMapping("/user/{userid}")//TODO
	public ResponseEntity<UserPayload> getAdminUserProfile(@PathVariable String userid){
		return null;
	}
	
	@PutMapping("/user/{userid}")//TODO
	public ResponseEntity<MessageResponse> updateAdminUserProfile(@PathVariable String userid, @RequestBody UserPayload user){
		return null;
	}
	
	@DeleteMapping("/user/{userid}")//TODO
	public ResponseEntity<MessageResponse> deleteAdminUserProfile(@PathVariable String userid){
		return null;
	}
	
	@PutMapping("/caff/{caffid}")//TODO
	public ResponseEntity<MessageResponse> updateAdminCAFF(@PathVariable String caffid, @RequestBody CAFFPreview caff){
		return null;
	}
	
	@DeleteMapping("/caff/{caffid}")//TODO
	public ResponseEntity<MessageResponse> deleteAdminCAFF(@PathVariable String caffid){
		return null;
	}

    /*@GetMapping("/welcome")
    public String welcome() {
        return "Welcome admin";
    }*/
}