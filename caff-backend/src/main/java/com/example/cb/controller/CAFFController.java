package com.example.cb.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.cb.model.CAFF;
import com.example.cb.payload.CAFFDownLoad;
import com.example.cb.payload.CAFFPreview;
import com.example.cb.payload.CAFFUpLoad;
import com.example.cb.payload.CommentPayload;
import com.example.cb.payload.MessageResponse;
import com.example.cb.service.CAFFService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/caff")
public class CAFFController {
	@Autowired
	private CAFFService service;
	
	@PutMapping("/{caffid}/comment")
	public ResponseEntity<?> commentCAFF(@PathVariable String caffid, @RequestBody CommentPayload commentPayload){
		long id = Long.parseLong(caffid);

		try {
			CAFF caff = service.getCAFF(id);
			Comment comment = new Comment(commentPayload.getUsername(), commentPayload.getComment());
			caff.addCommenttoCommments(comment);

			return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Commenting was successful"));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("CAFF not found"));
		}
	}
	
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
		CAFF caff;
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
	
	@GetMapping("/{caffid}")
	public ResponseEntity<?> viewCAFF(@PathVariable String caffid){
		long id = Long.parseLong(caffid);
		CAFFPreview caffPreview;
		List<CommentPayload> payloadList = new ArrayList<>();
		try {
			CAFF caff = service.getCAFF(id);
			List<Comment> commentList = caff.getComments();
			for(Comment comment : commentList){
				CommentPayload payload = comment.commentToPayload(comment);
				payloadList.add(payload);
			}
			caffPreview = new CAFFPreview(caff.getId(), caff.getName(), caff.getImguri(), payloadList);
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("CAFF not found"));
		}
		return ResponseEntity.status(HttpStatus.OK).body(caffPreview);
	}
	
	@GetMapping("")
	public ResponseEntity<List<CAFFPreview>> listCAFF(){

		List<CAFFPreview> caffPreviews = service.getAllCAFF().map(dbFile -> {
			String fileDownloadUri = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.path("/files/")
					.path(String.valueOf(dbFile.getId()))
					.toUriString();

					List<CommentPayload> payloadList = new ArrayList<>();
					List<Comment> commentList = dbFile.getComments();

					for (Comment comment : commentList) {
						CommentPayload payload = comment.commentToPayload(comment);
						payloadList.add(payload);
					}

			return new CAFFPreview(
					dbFile.getId(),
					dbFile.getName(),
					dbFile.getImguri(),
					payloadList);
			}).collect(Collectors.toList());

		return ResponseEntity.status(HttpStatus.OK).body(caffPreviews);
	}
	
	@GetMapping("/find/{namefilter}")
	public ResponseEntity<List<CAFFPreview>> findCAFF(@PathVariable String namefilter){
		List<CAFFPreview> previews = new ArrayList<CAFFPreview>();
		List<CAFF> caffs = new ArrayList<CAFF>();
		caffs = service.getCAFFsByFilter(namefilter);
		for(CAFF c : caffs) {
			previews.add(new CAFFPreview(c.getId(), c.getName(), c.getImguri(), c.getCommentPayloads()));
		}
		return ResponseEntity.status(HttpStatus.OK).body(previews);
	}
	
	private File runParser(MultipartFile caffupload){
		String name = caffupload.getOriginalFilename();
		if(name.endsWith(".caff")) name = name.substring(0, name.length()-5);
		/*Path currentRelativePath = Paths.get("");
		String currentPath = currentRelativePath.toAbsolutePath().toString();*/
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
}
