package com.example.cb.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.example.cb.controller.*;
import com.example.cb.model.*;
import com.example.cb.payload.*;
import com.example.cb.repository.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
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
	private CaffFileRepository caffFileRepo;
	@Mock
	private CaffWithoutDataRepository caffWithoutDataRepo;
	@Mock
	private CommentService commentService;
	@Mock
	private CommentRepository commentRepo;
	@BeforeEach
	public void setupMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test //output nélkül hibával elszáll
	void testUploadCaff() throws ParseException, IOException {
		CaffFile caffFile = new CaffFile();
		String path = "src/test/java/com/example/cb/service/test_caff/1.caff";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		byte[] fileBytes = is.readAllBytes();
		MockMultipartFile multipartFile = new MockMultipartFile("caffFile", "1.caff", MediaType.TEXT_PLAIN_VALUE, fileBytes);
		caffFile.setData(multipartFile.getBytes());
		when(caffRepo.save(caff)).thenReturn(caff);
		when(caffFileRepo.save(caffFile)).thenReturn(caffFile);
		Caff responseCaff = caffService.uploadCaff(caffFile);
		assertNotNull(responseCaff);
	}
	
	@Test //output nélkül hibával elszáll
	void testUploadInvalidCaff() throws ParseException, IOException {
		CaffFile caffFile = new CaffFile();
		String path = "src/test/java/com/example/cb/service/test_caff/invalid.caff";
		File file = new File(path);
		InputStream is = new FileInputStream(file);
		byte[] fileBytes = is.readAllBytes();
		MockMultipartFile multipartFile = new MockMultipartFile("caffFile", "invalid.caff", MediaType.TEXT_PLAIN_VALUE, fileBytes);
		caffFile.setData(multipartFile.getBytes());
		when(caffService.uploadCaff(caffFile)).thenThrow(new ParseException("error", 0));
		when(caffRepo.save(caff)).thenReturn(caff);
		when(caffFileRepo.save(caffFile)).thenReturn(caffFile);
		//Caff responseCaff = caffService.uploadCaff(caffFile);
		Assertions.assertThrows(ParseException.class, ()->caffService.uploadCaff(caffFile));
	}
	
	@Test
	void testGetPreviewOfCaff() {
		byte[] caffPreview = new byte[0];
		caff.setPreviewFile(caffPreview);
		when(caffRepo.findById(anyLong())).thenReturn(caff);
		byte[] responseCaffPreview = caffService.getPreviewOfCaff(1);
		assertEquals(caffPreview, responseCaffPreview);
	}
	
	@Test
	void testGetAllCAFFWithoutData() {
		CaffWithoutData cwd1 = new CaffWithoutData();
		CaffWithoutData cwd2 = new CaffWithoutData();
		when(caffWithoutDataRepo.findAll()).thenReturn(List.of(cwd1, cwd2));
		List<CaffWithoutData> list = caffService.getAllCAFFWithoutData();
		assertEquals(2, list.size());
	}
	
	@Test
	void testGetCaffFileById() {
		CaffFile caffFile = new CaffFile();
		byte[] caffFileData = new byte[0];
		caffFile.setData(caffFileData);
		caff.setFile(caffFile);
		when(caffRepo.findById(anyLong())).thenReturn(caff);
		byte[] responseData = caffService.getCaffFileById("1");
		assertEquals(caffFileData, responseData);
	}
	
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
