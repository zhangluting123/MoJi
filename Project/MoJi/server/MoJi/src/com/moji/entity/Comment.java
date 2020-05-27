/**
 * @Title: Comment.java
 * @Package com.moji.entity
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月8日
 * @version V1.0
 */
package com.moji.entity;

/**
 * @Title: Comment.java
 * @Package com.moji.entity
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2019年12月3日
 * @version V1.0
 */

public class Comment {
	private String id;
	private String noteId;
	private String userId;
	private String commentContent;
	private String commentTime;
	private User user;
	private Integer replyCount;
	private Note dbNote;
	private Video dbVideo;
	private ReturnNote note = new ReturnNote();
	private ReturnVideo video  = new ReturnVideo();
	
	public Comment() {
		// TODO Auto-generated constructor stub
	}

	
	public Comment(String id, String noteId, String userId, String commentContent, String commentTime, User user,
			Integer replyCount, Note dbNote, Video dbVideo,ReturnNote note,ReturnVideo video) {
		super();
		this.id = id;
		this.noteId = noteId;
		this.userId = userId;
		this.commentContent = commentContent;
		this.commentTime = commentTime;
		this.user = user;
		this.replyCount = replyCount;
		this.dbNote = dbNote;
		this.dbVideo = dbVideo;
		this.note = note;
		this.video=video;
	}

	
	
	public Video getDbVideo() {
		return dbVideo;
	}


	public void setDbVideo(Video dbVideo) {
		this.dbVideo = dbVideo;
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

	public Integer getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(Integer replyCount) {
		this.replyCount = replyCount;
	}


	public Note getDbNote() {
		return dbNote;
	}


	public void setDbNote(Note dbNote) {
		this.dbNote = dbNote;
	}


	public ReturnNote getNote() {
		return note;
	}


	public void setNote(ReturnNote note) {
		this.note = note;
	}


	public ReturnVideo getVideo() {
		return video;
	}


	public void setVideo(ReturnVideo video) {
		this.video = video;
	}

	
	
}
