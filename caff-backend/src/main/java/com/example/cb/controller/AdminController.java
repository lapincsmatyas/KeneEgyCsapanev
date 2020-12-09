package com.example.cb.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.example.cb.model.*;
import com.example.cb.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.example.cb.repository.RoleRepository;
import com.example.cb.service.CaffService;
import com.example.cb.service.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private UserService userservice;
	@Autowired
	private CaffService caffservice;
	@Autowired
	private CommentService commentService;
	@Autowired
	private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;

	@GetMapping("/user")
	public ResponseEntity<?> getAdminAllUser(){
		List<User> users;
		users = userservice.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}

	@GetMapping("/user/{userid}/admin")
	public ResponseEntity<User> grandUserAdmin(@PathVariable Long userid){
		User user = userservice.findById(userid);
		Role role = roleRepository.findByName(RoleEnum.ROLE_ADMIN).get();
		user.getRoles().add(role);
		userservice.save(user);
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}

	@DeleteMapping("/user/{userid}/admin")
	public ResponseEntity<User> revokeUserAdmin(@PathVariable Long userid){
		User user = userservice.findById(userid);
		Role role = roleRepository.findByName(RoleEnum.ROLE_ADMIN).get();
		user.getRoles().remove(role);
		userservice.save(user);
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}

	@DeleteMapping("/user/{userid}")
	public ResponseEntity<?> deleteAdminUserProfile(@PathVariable String userid){
		return null;
	}

	@DeleteMapping("/caff/{id}")
	public ResponseEntity<MessageResponse> deleteAdminCAFF(@PathVariable Long id){
		try {
			Caff caff = caffservice.getCAFFById(id);
			caffservice.delete(caff);
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("CAFF was deleted successfully"));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("CAFF not found"));
		}
	}

	@DeleteMapping("/caff/comment/{id}")
	public ResponseEntity<MessageResponse> deleteComment(@PathVariable Long id){
		try{
			this.commentService.removeComment(commentService.getCommentById(id));
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Comment was deleted successfully"));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Comment was not found"));
		}
	}

    /*@GetMapping("/welcome")
    public String welcome() {
        return "Welcome admin";
    }*/
}