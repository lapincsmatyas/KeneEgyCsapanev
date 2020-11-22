package com.example.dl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dl.model.User;
import com.example.dl.repository.UserRepository;

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
		userRepository.save(user);
		return user;
	}

	@Override
	public User findById(Long id) {
		if(userRepository.findById(id).isPresent()) {
			return userRepository.findById(id).get();
		}
		return null;
	}

	@Override
	public void delete(Long id) {
		User user = findById(id);
		userRepository.delete(user);
	}

}
