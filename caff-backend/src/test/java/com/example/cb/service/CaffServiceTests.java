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
import com.example.cb.util.Md5Generator;

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
import java.util.NoSuchElementException;
import java.util.Optional;

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
		Optional<Caff> ocaff = Optional.of(new Caff());
		ocaff.get().setPreviewFile(caffPreview);
		when(caffRepo.findById(anyLong())).thenReturn(ocaff);
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
		Optional<Caff> ocaff = Optional.of(new Caff());
		CaffFile caffFile = new CaffFile();
		byte[] caffFileData = new byte[0];
		caffFile.setData(caffFileData);
		ocaff.get().setFileMd5(Md5Generator.getMd5OfByteArray(caffFileData));
		ocaff.get().setFile(caffFile);
		when(caffRepo.findById(anyLong())).thenReturn(ocaff);
		byte[] responseData = caffService.getCaffFileById("1");
		assertEquals(caffFileData, responseData);
	}
	
	@Test
	void testGetCaffFileByIdHashChanged() {
		Optional<Caff> ocaff = Optional.of(new Caff());
		CaffFile caffFile = new CaffFile();
		byte[] caffFileData = new byte[0];
		caffFile.setData(caffFileData);
		ocaff.get().setFileMd5("");
		ocaff.get().setFile(caffFile);
		when(caffRepo.findById(anyLong())).thenReturn(ocaff);
		Assertions.assertThrows(SecurityException.class, ()->caffService.getCaffFileById("1"));
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
		Optional<Caff> ocaff = Optional.of(new Caff());
		when(caffRepo.findById(anyLong())).thenReturn(ocaff);
		Caff foundCaff = caffService.getCAFFById(1);
		assertEquals(ocaff.get(), foundCaff);
	}
	
	@Test
	void testGetCAFFByWrongId() {
		Assertions.assertThrows(NoSuchElementException.class, ()->caffRepo.findById(null).get());
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
		//when(caffRepo.findById(anyLong())).thenReturn(null);
		caffService.delete(caff);
		Assertions.assertThrows(NoSuchElementException.class, ()->caffRepo.findById(caff.getId()).get());
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
