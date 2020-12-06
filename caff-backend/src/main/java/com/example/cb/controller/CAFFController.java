package com.example.cb.controller;

import java.io.IOException;
import java.util.List;

import com.example.cb.model.CaffFile;
import com.example.cb.model.Comment;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.cb.model.Caff;
import com.example.cb.payload.CommentPayload;
import com.example.cb.service.CaffService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/caff")
public class CAFFController {
	@Autowired
	private CaffService caffService;

	
	@PutMapping("/{id}/comment")
	public Comment commentCAFF(@PathVariable Long id, @RequestBody CommentPayload commentPayload){
		Caff caff = caffService.getCAFFById(id);

		Comment comment = new Comment();
		comment.setUserName(commentPayload.getUserName());
		comment.setComment(commentPayload.getComment());

		return caffService.addCommentToCaff(caff, comment);
	}

	@GetMapping(value = "/{id}/preview", produces = MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody
	byte[] getPreviewImage(@PathVariable Long id) throws IOException {
		return caffService.getPreviewOfCaff(id);
	}

	@PostMapping("/upload")
	public Caff uploadCaff(@RequestParam("fileKey") MultipartFile file) {
		try {
			CaffFile caffFile = new CaffFile();
			caffFile.setData(file.getBytes());
			return caffService.uploadCaff(caffFile);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return null;
	}
	
	@GetMapping("/{id}/download")
	public ResponseEntity<?> downloadCAFF(@PathVariable String id){
		return null;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> viewCAFF(@PathVariable Long id){
		Caff caff = caffService.getCAFFById(id);
		return ResponseEntity.status(HttpStatus.OK).body(caff);
	}
	
	@GetMapping("")
	public ResponseEntity<List<Caff>> listCAFF(){
		return ResponseEntity.status(HttpStatus.OK).body(caffService.getAllCAFF());
	}

}
