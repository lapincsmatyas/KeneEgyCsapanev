package com.example.cb.model;

import com.example.cb.payload.CommentPayload;

import javax.persistence.*;

@Entity
@Table(name="comments")
public class Comment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="comment_id")
	private long commentid;
	private String username;
	private String comment;
	
	public Comment() {}
	public Comment(String username, String comment) {
		this.username = username;
		this.comment = comment;
	}
	public long getCommentid() {
		return commentid;
	}
	public void setCommentid(long commentid) {
		this.commentid = commentid;
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

	public CommentPayload commentToPayload(Comment comment){
		return new CommentPayload(comment.getUsername(), comment.getComment());
	}
}
