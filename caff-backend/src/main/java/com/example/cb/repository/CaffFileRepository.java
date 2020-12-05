package com.example.cb.repository;

import com.example.cb.model.CaffFile;
import com.example.cb.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaffFileRepository extends JpaRepository<CaffFile, Long> {
}
