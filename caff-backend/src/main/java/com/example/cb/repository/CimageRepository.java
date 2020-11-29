package com.example.cb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cb.model.Cimage;

public interface CimageRepository extends JpaRepository<Cimage, Long> {
	
}
