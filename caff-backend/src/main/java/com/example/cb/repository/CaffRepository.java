package com.example.cb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cb.model.Caff;

@Repository
public interface CaffRepository extends JpaRepository<Caff, Long> {
	public Caff findById(long id);
}
