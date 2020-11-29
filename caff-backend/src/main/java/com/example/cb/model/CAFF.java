package com.example.cb.model;

import java.util.List;

import javax.persistence.*;

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
	private String imguri;
	@OneToMany(orphanRemoval=true)
	@JoinColumn(name="comment_id")
	private List<Comment> comments;
	
	public CAFF() {}
	public CAFF(String name, String type, byte[] data, String imguri) {
		this.name=name;
		this.type=type;
		this.data=data;
		this.imguri=imguri;
	}
	public CAFF(String name, String type, byte[] data, String imguri, List<Comment> comments) {
		this.name=name;
		this.type=type;
		this.data=data;
		this.imguri=imguri;
		this.comments=comments;
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
	public String getImguri() {
		return imguri;
	}
	public void setImguri(String imguri) {
		this.imguri = imguri;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
}
