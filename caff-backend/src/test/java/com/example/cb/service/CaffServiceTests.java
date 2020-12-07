package com.example.cb.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.cb.controller.*;
import com.example.cb.model.*;
import com.example.cb.payload.*;
import com.example.cb.repository.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.List;

@SpringBootTest
public class CaffServiceTests {
	@Mock
	private UserRepository userRepo;
	@InjectMocks
	private UserServiceImpl userService;
	@Mock
	private CaffRepository caffRepo;
	@InjectMocks
	private CaffService caffService;
	@InjectMocks
	private Caff caff;
	@Mock
	private CaffFileRepository fileRepo;
	@Mock
	private CommentService commentService;
	@Mock
	private CommentRepository commentRepo;
	@BeforeEach
	public void setupMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	/*@Test
	public void testUploadCaff() {
		
	}*/
	
	@Test
	void testGetAllCAFF() {
		Caff anotherCaff = new Caff();
		when(caffRepo.findAll()).thenReturn(List.of(caff, anotherCaff));
		List<Caff> caffList = caffService.getAllCAFF();
		assertEquals(2, caffList.size());
	}
	
	@Test
	void testGetCAFFById() {
		when(caffRepo.findById(anyLong())).thenReturn(caff);
		Caff foundCaff = caffService.getCAFFById(1);
		assertEquals(caff, foundCaff);
	}
	
	@Test
	void testGetCAFFByWrongId() {
		when(caffRepo.findById(anyLong())).thenReturn(null);
		Caff foundCaff=caffService.getCAFFById(1);
		assertNull(foundCaff);
	}
	
	@Test
	void testPersistCAFF() {
		caffService.delete(caff);
		Caff newCaff = new Caff();
		when(caffRepo.save(newCaff)).thenReturn(newCaff);
		Caff foundCaff = caffService.persistCAFF(newCaff);
		assertEquals(newCaff, foundCaff);
	}
	
	@Test
	void testDeleteCAFF() {
		when(caffRepo.findById(anyLong())).thenReturn(null);
		caffService.delete(caff);
		Caff foundCaff = caffService.getCAFFById(1);
		assertNull(foundCaff);
	}
	
	@Test
	void testAddCommentToCaff() {
		Comment comment = new Comment("username", "comment");
		when(caffRepo.save(caff)).thenReturn(caff);
		when(commentRepo.save(comment)).thenReturn(comment);
		Comment savedComment = caffService.addCommentToCaff(caff, comment);
		assertEquals(savedComment, comment);
	}
}
