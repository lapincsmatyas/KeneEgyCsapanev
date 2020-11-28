package com.example.cb.payload;

import org.springframework.web.multipart.MultipartFile;

//request to download and response to upload CAFF
public class CAFFUpLoad {
	private String name;
	private MultipartFile file;
	
	public CAFFUpLoad(String name, MultipartFile file) {
		this.name = name;
		this.file = file;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
}
