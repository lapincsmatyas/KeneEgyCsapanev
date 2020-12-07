package com.example.cb.controller;

import java.io.IOException;
import java.sql.Blob;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import com.example.cb.model.CaffFile;
import com.example.cb.model.Comment;
import com.example.cb.payload.CaffListItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger logger = LoggerFactory.getLogger(CAFFController.class);

    @Autowired
    private CaffService caffService;


    @PutMapping("/{id}/comment")
    public Comment commentCAFF(@PathVariable Long id, @RequestBody CommentPayload commentPayload) {
        logger.info("Comment addition started to CAFF with id: " + id + " by user " + commentPayload.getUserName());
        Caff caff = caffService.getCAFFById(id);

        Comment comment = new Comment();
        comment.setUserName(commentPayload.getUserName());
        comment.setComment(commentPayload.getComment());

        return caffService.addCommentToCaff(caff, comment);
    }

    @GetMapping(value = "/{id}/preview", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] getPreviewImage(@PathVariable Long id) throws IOException {
        logger.info("Preview image requested for CAFF with id: " + id);

        return caffService.getPreviewOfCaff(id);
    }

    @PostMapping("/upload")
    public ResponseEntity<Caff> uploadCaff(@RequestParam("fileKey") MultipartFile file) {
        logger.info("CAFF file upload started: " + file.getName() + " size: " + file.getSize());
        try {
            CaffFile caffFile = new CaffFile();
            caffFile.setData(file.getBytes());
            return ResponseEntity.status(HttpStatus.OK).body(caffService.uploadCaff(caffFile));
        } catch (IOException | ParseException exception) {
            logger.error("Unsuccessful caff upload");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadCAFF(@PathVariable String id) {
        logger.info("CAFF download started with id: " + id);
        return ResponseEntity.status(HttpStatus.OK).body(caffService.getCaffFileById(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caff> getCAFF(@PathVariable Long id) {
        logger.info("CAFF requested with id: " + id);
        Caff caff = caffService.getCAFFById(id);
        return ResponseEntity.status(HttpStatus.OK).body(caff);
    }

    @GetMapping("")
    public ResponseEntity<List<CaffListItem>> listCAFF() {
        logger.info("CAFF list requested");
        List<CaffListItem> caffListItems = caffService.getAllCAFFWithoutData().stream()
                .map(caff -> {
                    CaffListItem caffListItem = new CaffListItem();
                    caffListItem.setId(caff.getId());
                    caffListItem.setName(caff.getName());
                    caffListItem.setTags(caff.getTags());
                    return caffListItem;
                }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(caffListItems);
    }

}
