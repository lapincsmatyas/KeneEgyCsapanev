package com.example.cb.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.cb.model.Comment;
import com.example.cb.repository.CommentRepository;

@SpringBootTest
public class CommentServiceTests {
	@InjectMocks
	private CommentService commentService;
	@Mock
	private CommentRepository commentRepo;
	
	@Test
	void testPersistComment() {
		Comment comment = new Comment("username", "comment");
		when(commentRepo.save(comment)).thenReturn(comment);
		Comment foundComment = commentService.persistComment(comment);
		assertNotNull(foundComment);
	}
}
