package com.example.cb.payload;

public class CommentPayload {
	private String username;
	private String comment;
	public CommentPayload(String username, String comment) {
		this.username = username;
		this.comment = comment;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
