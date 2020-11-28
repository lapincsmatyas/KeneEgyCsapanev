package com.example.cb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.cb.payload.CAFFDownLoad;
import com.example.cb.payload.CAFFPreview;
import com.example.cb.payload.CommentPayload;
import com.example.cb.payload.MessageResponse;
import com.example.cb.service.CAFFService;

@CrossOrigin("*")
@RestController
@RequestMapping("/caff")
public class CAFFController {
	@Autowired
	private CAFFService service;
	
	@PutMapping("/{caffid}/comment")//TODO
	public ResponseEntity<MessageResponse> commentCAFF(@PathVariable String caffid, @RequestBody CommentPayload comment){
		return null;
	}
	
	@PostMapping("/upload")
	public ResponseEntity<MessageResponse> uploadCAFF(@RequestBody MultipartFile file){
		String message="";
		try {
			service.store(file);
			message = "Upload was successful";
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
		} catch (Exception e) {
			return null;
		}
	}
	
	@GetMapping("/{caffid}/download")//TODO
	public ResponseEntity<CAFFDownLoad> downloadCAFF(@PathVariable String caffid){
		return null;
	}
	
	@GetMapping("/{caffid}")//TODO
	public ResponseEntity<CAFFPreview> viewCAFF(@PathVariable String caffid){
		long id = Long.parseLong(caffid);
		return null;
	}
	
	@GetMapping("")//TODO
	public ResponseEntity<List<CAFFPreview>> listCAFF(){
		return null;
	}
	
	@GetMapping("/find/{namefilter}")//TODO
	public ResponseEntity<List<CAFFPreview>> findCAFF(@PathVariable String namefilter){
		return null;
	}
}
