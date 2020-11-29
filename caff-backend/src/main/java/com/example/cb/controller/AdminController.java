package com.example.cb.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.example.cb.model.CAFF;
import com.example.cb.model.MyUserDetails;
import com.example.cb.model.Role;
import com.example.cb.model.RoleEnum;
import com.example.cb.model.User;
import com.example.cb.payload.CAFFPreview;
import com.example.cb.payload.MessageResponse;
import com.example.cb.payload.UserPayload;
import com.example.cb.repository.RoleRepository;
import com.example.cb.service.CAFFService;
import com.example.cb.service.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private UserService userservice;
	@Autowired
	private CAFFService caffservice;
	@Autowired
	private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;
	
	@GetMapping("/user")
	public ResponseEntity<?> getAdminAllUser(){
		List<User> users = new ArrayList<User>();
		users = userservice.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}
	
	@GetMapping("/user/{userid}")
	public ResponseEntity<?> getAdminUserProfile(@PathVariable String userid){
		long id = Long.parseLong(userid);
		User user = null;
		user = userservice.findById(id);
		if(user==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("User not found"));
		UserPayload res = new UserPayload(
				user.getUser_id(),
				user.getUsername(),
				user.getEmail(),
				user.getPassword(),
				user.getStrRoles());
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}
	
	@PutMapping("/user/{userid}")
	public ResponseEntity<?> updateAdminUserProfile(@PathVariable String userid, @RequestBody UserPayload user){
		long id = Long.parseLong(userid);
		User current = null;
		current = userservice.findById(id);
		if(current==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("User not found"));
		User userdb = null;
		userdb = userservice.findByUsername(user.getUsername());
		if(userdb!=null && !current.getUsername().equals(user.getUsername())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Username is already taken"));
		userdb = userservice.findByEmail(user.getEmail());
		if(userdb!=null && !current.getEmail().equals(user.getEmail())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Email is already in use"));
		userdb = userservice.findById(id);
		List<String> strRoles = new ArrayList<>();
		strRoles = user.getRoles();
        List<Role> roles = new ArrayList<Role>();
		if (strRoles != null) {
            strRoles.forEach(role -> {
                //check if admin
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                }
                //check if user
                else if ("user".equals(role))  {
                    Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                //check if none of them
                } else {
                    throw new RuntimeException("Error: Wrong Role.");
                }
            });
        }
		userdb.setUsername(user.getUsername());
		userdb.setEmail(user.getEmail());
		userdb.setPassword(encoder.encode(user.getPassword()));
		userdb.setRoles(roles);
		userservice.save(userdb);
		MyUserDetails userDetails = (MyUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userDetails.getUsername().equals(current.getUsername())) return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("User was updated successfully, please log in again"));
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("User was updated successfully"));
	}
	
	@DeleteMapping("/user/{userid}")
	public ResponseEntity<?> deleteAdminUserProfile(@PathVariable String userid){
		long id = Long.parseLong(userid);
		User user = null;
		user = userservice.findById(id);
		if(user==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("User not found"));
		MyUserDetails userDetails = (MyUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userDetails.getUsername().equals(user.getUsername())) {
			List<User> users = new ArrayList<User>();
			users = userservice.findAll();
			int admins = 0;
			Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			for(User u : users) {
				if (u.getRoles().contains(adminRole)){
					admins++;
					if(admins==2) break;
				}
			}
			if(admins<2) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("You are the last admin, you cannot delete yourself"));
		}
		userservice.delete(user);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("User was deleted successfully"));
	}
	
	@PutMapping("/caff/{caffid}")//TODO
	public ResponseEntity<?> updateAdminCAFF(@PathVariable String caffid, @RequestBody CAFFPreview caff){
		long id = Long.parseLong(caffid);
		
		
		return null;
	}
	
	@DeleteMapping("/caff/{caffid}")
	public ResponseEntity<MessageResponse> deleteAdminCAFF(@PathVariable String caffid){
		long id = Long.parseLong(caffid);
		try {
			CAFF caff = caffservice.getCAFF(id);
			caffservice.delete(caff);
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("CAFF was deleted successfully"));
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("CAFF not found"));
		}
	}

    /*@GetMapping("/welcome")
    public String welcome() {
        return "Welcome admin";
    }*/
}