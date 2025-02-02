package com.example.cb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cb.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	public User findById(long id);
	
	public User findByUsername(String username);

	public Boolean existsByUsername(String username);

	public Boolean existsByEmail(String email);

	public User findByEmail(String email);

	public User save(User user);
}
 