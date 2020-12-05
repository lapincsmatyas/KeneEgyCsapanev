package com.example.cb.service;

import com.example.cb.model.Comment;
import com.example.cb.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
	@Autowired
	private CommentRepository commentRepository;

	public Comment persistComment(Comment comment){
		return commentRepository.save(comment);
	}
}
