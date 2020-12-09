package com.example.cb.payload;

import java.util.List;

//Response for getting a CAFF preview
public class CaffListItem {
	private long id;
	private String name;
	private String[] tags;

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

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}
}
