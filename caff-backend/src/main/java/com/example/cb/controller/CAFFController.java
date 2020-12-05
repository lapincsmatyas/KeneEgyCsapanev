package com.example.cb.controller;

import java.io.IOException;
import java.util.List;

import com.example.cb.model.CaffFile;
import com.example.cb.model.Comment;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

	/*
	@PostMapping("/upload")//TODO: parse image
	public ResponseEntity<MessageResponse> uploadCAFF(@RequestParam MultipartFile file){
		String message="";
		String imguri="";
		File imgfile = runParser(file);
		try {
			byte[] imgdata = Files.readAllBytes(imgfile.toPath());
			service.store(file, imgdata, imguri);
			message = "Upload was successful";
			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
		} catch (Exception e) {
			return null;
		}
		CAFF caff = service.findByName(caffupload.getName());
		imguri = ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path("/caffs/imgs/")
				.path(caffupload.getName())
				.toUriString();
	}
	*/

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

	/*
	private File runParser(MultipartFile caffupload){
		String name = caffupload.getOriginalFilename();
		if(name.endsWith(".caff")) name = name.substring(0, name.length()-5);
		Path currentRelativePath = Paths.get("");
		String currentPath = currentRelativePath.toAbsolutePath().toString();
		String currentPath = System.getProperty("user.dir");
		String processPath = currentPath + "/caff_parser/src";
		String caffPath = currentPath + "/caff_parser/caff-files";
		Path pathToSave = Paths.get(caffPath + "/" + name + ".caff");
		try {
			Files.write(pathToSave, caffupload.getBytes());
		} catch (IOException e1) {}
		String command = "." + processPath + "/output ../caff-files/" + name + ".caff " + name;
		try {
			Process p = Runtime.getRuntime().exec(command);
		} catch (IOException e) {}
		File img = new File(processPath + "/" + name + ".bmp");
		return img;
	}
	*/
}
