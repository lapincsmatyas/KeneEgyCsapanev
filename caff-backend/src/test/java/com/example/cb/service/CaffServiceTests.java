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
	@InjectMocks
	private CAFFController caffController;
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
	
	/*@Test
	void testPersistCAFF() {
		caffService.delete(caff);
		Caff newCaff = new Caff();
		caffService.persistCAFF(newCaff);
		when(caffRepo.findById(anyLong())).thenReturn(newCaff);
		Caff foundCaff = caffService.getCAFFById(newCaff.getId());
		assertEquals(newCaff, foundCaff);
	}
	
	@Test
	void testDeleteCAFF() {
		caffService.delete(caff);
		Caff foundCaff = caffService.getCAFFById(caff.getId());
		System.out.println(caff.getId());
		assertEquals(foundCaff, null);
	}*/
	
	@Test
	void testCommentCAFF() {
		CommentPayload comment = new CommentPayload("username", "comment");
		when(caffService.getCAFFById(anyLong())).thenReturn(caff);
		when(caffRepo.findById(anyLong())).thenReturn(caff);
		Comment responseComment = caffController.commentCAFF((long)1, comment);
		assertEquals(responseComment.getComment(), comment.getComment());
		
		/*Comment comment = new Comment("username", "comment");
		comment.setCaff(caff);
		Comment savedComment = caffService.addCommentToCaff(caff, comment);
		savedComment=commentRepo.save(comment);
		when(caffRepo.findById(anyLong())).thenReturn(caff);
		Caff foundCaff = caffService.getCAFFById(1);
		assertEquals(savedComment, comment);*/
	}
}
