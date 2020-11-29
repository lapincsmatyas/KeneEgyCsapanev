package com.example.cb.payload;

import java.util.List;

import com.example.cb.model.Role;

public class UserPayload {
	private long user_id;
	private String username;
	private String email;
	private String password;
	private List<String> roles;
	
	public UserPayload(long user_id, String username, String email, String password, List<String> roles) {
		this.user_id = user_id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
