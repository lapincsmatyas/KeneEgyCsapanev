package com.example.cb.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

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
        Caff caff = parseCaff(caffFile);
        caffRepository.save(caff);
        caffFileRepository.save(caffFile);

        caff.setFile(caffFile);
        caffFile.setCaff(caff);

        caffRepository.save(caff);
        caffFileRepository.save(caffFile);

        return caff;
    }

    private Caff parseCaff(CaffFile caffFile) {
        try {
            String[] command = {"./caff_parser/src/output","./temp","generated"};
            Files.write(Paths.get("temp"), caffFile.getData());
            return execCmd(command);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public Caff execCmd(String[] command) {
        try {
            Process process = new ProcessBuilder(command).start();

            Scanner error = new Scanner(process.getErrorStream());
            while(error.hasNextLine()){
                System.out.println(error.nextLine());
            }

            process.waitFor();
            int exitvalue = process.exitValue();
            if(exitvalue == 0) {
                try (Scanner input = new Scanner(process.getInputStream())) {
                    String[] result = input.nextLine().split(";");
                    Caff caff = new Caff();
                    caff.setName(result[0]);
                    caff.setTags(result[1]);
                    caff.setWidth(Integer.parseInt(result[2]));
                    caff.setHeight(Integer.parseInt(result[3]));
                    caff.setPreviewFile(Files.readAllBytes(Paths.get("generated.bmp")));
                    return caff;
                }
            } else {
                System.out.println("Something went wrong");
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
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

    public byte[] getPreviewOfCaff(Long id) {
        return caffRepository.findById(id).get().getPreviewFile();
    }
}
