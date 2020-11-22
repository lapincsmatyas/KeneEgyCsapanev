package com.example.dl.service;

import java.util.List;
import java.util.Optional;

import com.example.dl.model.User;

public interface UserService {
	List<User> findAll();
	
	User save(User user);

	User findById(Long id);

	void delete(Long id);
}
