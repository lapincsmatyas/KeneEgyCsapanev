package com.example.cb.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.example.cb.model.CaffFile;
import com.example.cb.model.Comment;
import com.example.cb.repository.CaffFileRepository;
import com.example.cb.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cb.model.Caff;
import com.example.cb.repository.CaffRepository;

@Service
public class CaffService {
    @Autowired
    private CaffRepository caffRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CaffFileRepository caffFileRepository;

    public List<Caff> getAllCAFF() {
        return caffRepository.findAll();
    }

    public void delete(Caff caff) {
        caffRepository.delete(caff);
    }

    public Caff uploadCaff(CaffFile caffFile) {
        parseCaff(caffFile);
        /*
        Caff caff = new Caff();
        caff.setName("Új Caff");
        caff.setTags("new tag");

        caffRepository.save(caff);
        caffFileRepository.save(caffFile);

        caff.setFile(caffFile);
        caffFile.setCaff(caff);

        caffRepository.save(caff);
        caffFileRepository.save(caffFile);

        return caff;

         */
        return null;
    }

    private void parseCaff(CaffFile caffFile){
        try {
            Files.write(Paths.get("temp"), caffFile.getData());
        } catch (IOException e1) {}

        String command = "./caff_parser/src" + "/output ../caff-files/temp";
        try {
            Process p = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Caff getCAFFById(Long id) {
        return caffRepository.findById(id).get();
    }

    public Caff persistCAFF(Caff caff) {
        return caffRepository.save(caff);
    }

    public Comment addCommentToCaff(Caff caff, Comment comment) {
        comment.setCaff(caff);
        caffRepository.save(caff);
        return commentRepository.save(comment);
    }
}
