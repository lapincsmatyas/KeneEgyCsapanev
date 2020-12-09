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

    public Comment getCommentById(Long id) {
		return this.commentRepository.getOne(id);
    }

    public void removeComment(Comment comment){
		this.commentRepository.delete(comment);
	}
}
