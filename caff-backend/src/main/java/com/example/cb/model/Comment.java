package com.example.cb.model;

import com.example.cb.payload.CommentPayload;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="comments")
public class Comment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private String userName;
	private String comment;

	@ManyToOne
	@JoinColumn(name = "caff_id")
	@JsonIgnore
	private Caff caff;
	
	public Comment() {}
	public Comment(String userName, String comment) {
		this.userName = userName;
		this.comment = comment;
	}
	public long getId() {
		return id;
	}
	public void setId(long commentid) {
		this.id = commentid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String username) {
		this.userName = username;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	public CommentPayload commentToPayload(Comment comment){
		return new CommentPayload(comment.getUserName(), comment.getComment());
	}

	public Caff getCaff() {
		return caff;
	}

	public void setCaff(Caff caff) {
		this.caff = caff;
	}
}
