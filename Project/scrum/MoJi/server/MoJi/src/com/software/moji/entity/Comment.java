package com.software.moji.entity;

/**
 * @Title: Comment.java
 * @Package com.moji.entity
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2019年12月3日
 * @version V1.0
 */

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Comment
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 春波
 * @date 2019年12月3日
 */
public class Comment {
	private User user;
	private String id;
	private String noteId;
	private String userId;
	private String commentContent;
	private String commentTime;
	
	public Comment() {
		// TODO Auto-generated constructor stub
	}

	public Comment(User user, String id, String noteId, String userId, String commentContent, String commentTime) {
		super();
		this.user = user;
		this.id = id;
		this.noteId = noteId;
		this.userId = userId;
		this.commentContent = commentContent;
		this.commentTime = commentTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNoteId() {
		return noteId;
	}

	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

}

