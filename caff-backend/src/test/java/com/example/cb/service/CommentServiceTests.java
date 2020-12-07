package com.example.cb.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.cb.repository.CommentRepository;

@SpringBootTest
public class CommentServiceTests {
	@Mock
	private CommentService commentService;
	@InjectMocks
	private CommentRepository commentRepo;
	
	@Test
	void testPersistComment() {
		
	}
}
