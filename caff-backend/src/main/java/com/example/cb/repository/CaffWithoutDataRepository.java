package com.example.cb.repository;

import com.example.cb.model.Caff;
import com.example.cb.model.CaffWithoutData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaffWithoutDataRepository extends JpaRepository<CaffWithoutData, Long> {
}
