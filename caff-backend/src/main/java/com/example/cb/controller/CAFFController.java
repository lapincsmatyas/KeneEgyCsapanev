package com.example.cb.controller;

import java.util.List;
import java.util.NoSuchElementException;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.cb.model.CAFF;
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
		long id = Long.parseLong(caffid);
		return null;
	}
	
	@PostMapping("/upload")//TODO: parse image
	public ResponseEntity<MessageResponse> uploadCAFF(@RequestBody MultipartFile file){
		String message="";
		String imguri="";
		//TODO: parse file
		try {
			service.store(file, imguri);
			message = "Upload was successful";
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
		} catch (Exception e) {
			return null;
		}
	}
	
	@GetMapping("/{caffid}/download")//TODO
	public ResponseEntity<?> downloadCAFF(@PathVariable String caffid){
		long id = Long.parseLong(caffid);
		CAFF caff = new CAFF();
		try {
			caff = service.getCAFF(id);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("CAFF not found"));
		}
		String uri = ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path("/caffs/")
				.path(((Long)caff.getId()).toString())
				.toUriString();
		CAFFDownLoad res = new CAFFDownLoad(caff.getName(), uri, caff.getType());
		return ResponseEntity.status(HttpStatus.OK).body(res);
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
