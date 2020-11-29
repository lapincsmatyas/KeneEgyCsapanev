package com.example.cb.service;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.cb.model.CAFF;
import com.example.cb.repository.CAFFRepository;

@Service
public class CAFFService {
	@Autowired
	private CAFFRepository repo;
	
	public CAFF store(MultipartFile file, byte[] imgdata, String imguri) throws IOException{
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		CAFF caff = new CAFF(fileName, file.getContentType(), file.getBytes(), imgdata, imguri);
		return repo.save(caff);
	}
	
	public CAFF getCAFF(Long id) {
		return repo.findById(id).get();
	}
	
	public Stream<CAFF> getAllCAFF(){
		return repo.findAll().stream();
	}
}
