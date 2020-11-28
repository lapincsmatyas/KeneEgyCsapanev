package com.example.cb.payload;

import java.io.File;

public class CAFFDownLoad {
	private String name;
	private String uri;
	private String type;
	private File file;
	
	public CAFFDownLoad(String name, String uri, String type) {
		this.name = name;
		this.uri = uri;
		this.type = type;
	}
	public CAFFDownLoad(String name, String uri, String type, File file) {
		this.name = name;
		this.uri = uri;
		this.type = type;
		this.file = file;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
}
