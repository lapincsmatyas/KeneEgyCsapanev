package com.example.cb.service;

import java.util.List;

import com.example.cb.model.User;

public interface UserService {
	public List<User> findAll();
	
	public User save(User user);

	public User findById(Long id);
	
	public User findByUsername(String username);

	public void delete(User user);
}
