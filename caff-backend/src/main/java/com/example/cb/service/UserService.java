package com.example.cb.service;

import java.util.List;

import com.example.cb.model.User;

public interface UserService {
	List<User> findAll();
	
	User save(User user);

	User findById(Long id);

	void delete(Long id);
}
