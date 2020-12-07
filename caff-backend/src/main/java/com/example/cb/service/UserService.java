package com.example.cb.service;

import java.util.List;

import com.example.cb.model.MyUserDetails;
import com.example.cb.model.User;
import com.example.cb.payload.MessageResponse;
import com.example.cb.payload.UserPayload;
import com.example.cb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	private PasswordEncoder encoder;

	public List<User> findAll() {
		return userRepository.findAll();
	}


	public void save(User user) {
		userRepository.save(user);
	}


	public User findById(long id) {
		return userRepository.findById(id);
	}


	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}


	public void delete(User user) {
		userRepository.delete(user);
	}


	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public ResponseEntity<?> getUserProfile(String username) {
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if(!userDetails.getUsername().equals(username))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Other users are not available " + userDetails.getUsername()));

		User user = null;
		user = userRepository.findByUsername(username);

		if(user==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("User not found"));

		UserPayload res = new UserPayload(
				user.getUser_id(),
				user.getUsername(),
				user.getEmail(),
				user.getPassword(),
				user.getStrRoles());

		return ResponseEntity.status(HttpStatus.OK).body(res);
	}

	public ResponseEntity<?> updateUserProfile(String username, UserPayload user) {
		MyUserDetails userDetails = (MyUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if(!userDetails.getUsername().equals(username)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("Other users are not available"));

		User userdb = null;
		userdb = userRepository.findByUsername(user.getUsername());

		if(userdb!=null && !userDetails.getUsername().equals(user.getUsername())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Username is already taken"));
		userdb = userRepository.findByEmail(user.getEmail());

		if(userdb!=null && !userDetails.getUser().getEmail().equals(user.getEmail())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Email is already in use"));
		userdb = userRepository.findByUsername(username);

		if(userdb==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("User not found"));

		userdb.setUsername(user.getUsername());
		userdb.setEmail(user.getEmail());
		userdb.setPassword(encoder.encode(user.getPassword()));

		userRepository.save(userdb);

		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("User was updated successfully, please log in again"));
	}
}
