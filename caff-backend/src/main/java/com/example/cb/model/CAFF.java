package com.example.cb.model;

import java.util.List;

import javax.persistence.*;

import com.example.cb.payload.Comment;

@Entity
@Table(name="caffs")
public class CAFF {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	private String type;
	@Lob
	private byte[] data;
	//private List<Comment> comments;
	
	public CAFF() {}
	public CAFF(String name, String type, byte[] data) {
		this.name=name;
		this.type=type;
		this.data=data;
	}
	public CAFF(String name, String type, byte[] data, List<Comment> comments) {
		this.name=name;
		this.type=type;
		this.data=data;
		//this.comments=comments;
	}
	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	/*public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}*/
}
