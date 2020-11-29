package com.example.cb.model;

import javax.persistence.*;

@Entity
@Table(name="Cimages")
public class Cimage {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	@Lob
	private byte[] data;
	
	public Cimage() {}
	public Cimage(String name, byte[] data) {
		this.name = name;
		this.data = data;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
}
