package com.example.cb.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import com.example.cb.payload.CAFFUpLoad;
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
	public ResponseEntity<MessageResponse> uploadCAFF(@RequestBody CAFFUpLoad caffupload){
		String message="";
		String imguri="";
		File imgfile = runParser(caffupload);
		try {
			byte[] imgdata = Files.readAllBytes(imgfile.toPath());
			service.store(caffupload.getFile(), imgdata, imguri);
			message = "Upload was successful";
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
		} catch (Exception e) {
			return null;
		}
		/*CAFF caff = service.findByName(caffupload.getName());
		imguri = ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path("/caffs/imgs/")
				.path(caffupload.getName())
				.toUriString();*/
		
	}
	
	@GetMapping("/{caffid}/download")
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
	
	private File runParser(CAFFUpLoad caffupload){
		String name = caffupload.getName();
		if(name.endsWith(".caff")) name = name.substring(0, name.length()-5);
		MultipartFile file = caffupload.getFile();
		/*Path currentRelativePath = Paths.get("");
		String currentPath = currentRelativePath.toAbsolutePath().toString();*/
		String currentPath = System.getProperty("user.dir");
		String processPath = currentPath + "/caff_parser/src";
		String caffPath = currentPath + "/caff_parser/caff-files";
		Path pathToSave = Paths.get(caffPath + name + ".caff");
		try {
			Files.write(pathToSave, file.getBytes());
		} catch (IOException e1) {}
		String command = "." + processPath + "/output ../caff-files/" + name + ".caff " + name;
		try {
			Process p = Runtime.getRuntime().exec(command);
		} catch (IOException e) {}
		File img = new File(processPath + name + "bmp");
		return img;
	}
}
