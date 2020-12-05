package com.example.cb.payload;

public class CommentPayload {
	private String userName;
	private String comment;
	public CommentPayload(String username, String comment) {
		this.userName = username;
		this.comment = comment;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
