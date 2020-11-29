package com.example.cb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cb.model.User;
import com.example.cb.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserRepository userRepository;

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public User findById(Long id) {
		return userRepository.findById(id).get();
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public void delete(User user) {
		userRepository.delete(user);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

}
