package com.example.cb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cb.model.CAFF;

@Repository
public interface CAFFRepository extends JpaRepository<CAFF, Long> {
	
}
