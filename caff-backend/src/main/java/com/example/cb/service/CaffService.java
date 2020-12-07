package com.example.cb.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import com.example.cb.model.CaffFile;
import com.example.cb.model.CaffWithoutData;
import com.example.cb.model.Comment;
import com.example.cb.repository.CaffFileRepository;
import com.example.cb.repository.CaffWithoutDataRepository;
import com.example.cb.repository.CommentRepository;
import com.example.cb.util.Md5Generator;
import org.aspectj.bridge.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.cb.model.Caff;
import com.example.cb.repository.CaffRepository;

@Service
public class CaffService {
    Logger logger = LoggerFactory.getLogger(CaffService.class);

    @Autowired
    private CaffRepository caffRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CaffFileRepository caffFileRepository;

    @Autowired
    private CaffWithoutDataRepository caffWithoutDataRepository;

    public List<Caff> getAllCAFF() {
        return caffRepository.findAll();
    }


    public void delete(Caff caff) {
        caffRepository.delete(caff);
    }

    public Caff uploadCaff(CaffFile caffFile) throws ParseException {
        try {
            Caff caff = parseCaff(caffFile);
            caff.setFileMd5(Md5Generator.getMd5OfByteArray(caffFile.getData()));
            caffRepository.save(caff);
            caffFileRepository.save(caffFile);

            caff.setFile(caffFile);
            caffFile.setCaff(caff);

            caffRepository.save(caff);
            caffFileRepository.save(caffFile);
            logger.info("CAFF file uploaded");

            return caff;
        } catch (ParseException e) {
            logger.error("There was  an error during parsing" + e.getMessage());
            throw e;
        }
    }

    private Caff parseCaff(CaffFile caffFile) throws ParseException {
        try {
            String parserURI = "./caff_parser/src/output.exe";
            String md5 = Md5Generator.getMd5OfFile(parserURI);
            if (!"ae8a417509c88f61d7c0c799c68a47fb".equals(md5)) {
                throw new SecurityException("The parser has been tampered");
            }
            String[] command = {parserURI, "./temp", "generated"};
            Files.write(Paths.get("temp"), caffFile.getData());
            return execCmd(command);
        } catch (IOException e) {
            logger.error("Could not save the preview image");
        } catch (InterruptedException e) {
            logger.error("There was an error during the execution of the parse process");
        } catch (SecurityException e) {
            logger.error("There was an error during MD5 check", e.getMessage());
        }
        return null;
    }

    public Caff execCmd(String[] command) throws ParseException, InterruptedException, IOException {

        Process process = new ProcessBuilder(command).start();

        Scanner error = new Scanner(process.getErrorStream());
        StringBuilder errorMessage = new StringBuilder();
        while (error.hasNextLine()) {
            errorMessage.append(error.nextLine());
        }

        process.waitFor();
        int exitvalue = process.exitValue();
        if (exitvalue == 0) {
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
            throw new ParseException(errorMessage.toString(), 0);
        }
    }

    public Caff getCAFFById(long id) {
        return caffRepository.findById(id);
    }

    public Caff persistCAFF(Caff caff) {
        return caffRepository.save(caff);
    }

    public Comment addCommentToCaff(Caff caff, Comment comment) {
        comment.setCaff(caff);
        caffRepository.save(caff);
        return commentRepository.save(comment);
    }

    public byte[] getPreviewOfCaff(long id) {
        return caffRepository.findById(id).getPreviewFile();
    }

    public List<CaffWithoutData> getAllCAFFWithoutData() {
        return caffWithoutDataRepository.findAll();
    }

    public byte[] getCaffFileById(String id) throws SecurityException {
        Caff caff = caffRepository.findById(Long.parseLong(id)).get();
        String fileMd5 = Md5Generator.getMd5OfByteArray(caff.getFile().getData());
        if(!fileMd5.equals(caff.getFileMd5()))
            throw new SecurityException("The file hash has changed");

        return caffRepository.findById(Long.parseLong(id)).get().getFile().getData();
    }
}
