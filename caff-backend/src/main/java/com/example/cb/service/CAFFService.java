package com.example.cb.service;

import java.util.List;

import com.example.cb.model.Comment;
import com.example.cb.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cb.model.CAFF;
import com.example.cb.repository.CAFFRepository;

@Service
public class CAFFService {
	@Autowired
	private CAFFRepository caffRepository;

	@Autowired
	private CommentRepository commentRepository;

	public List<CAFF> getAllCAFF(){
		return caffRepository.findAll();
	}

	public void delete(CAFF caff) {
		caffRepository.delete(caff);
		
	}

	public CAFF getCAFFById(Long id) {
		return caffRepository.findById(id).get();
	}

	public CAFF persistCAFF(CAFF caff){
		return caffRepository.save(caff);
	}

	public Comment addCommentToCaff(CAFF caff, Comment comment) {
		comment.setCaff(caff);
		caffRepository.save(caff);
		return commentRepository.save(comment);
	}
}
