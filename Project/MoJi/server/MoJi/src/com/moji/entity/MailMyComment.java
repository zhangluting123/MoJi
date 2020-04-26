package com.moji.entity;

/**   
 * @ClassName: MailMyComment   
 * @Description:TODO
 * @author: 张璐婷
 * @date: 2020年4月24日 上午9:13:00       
 */
public class MailMyComment {
	private Integer id;
	private User user;
	private Comment comment;
	private ReplyComment replyComment;
	private Character crFlag;
	private Integer readFlag;
	
	public MailMyComment() {
		super();
	}

	public MailMyComment(Integer id, User user, Comment comment, ReplyComment replyComment, Character crFlag,
			Integer readFlag) {
		super();
		this.id = id;
		this.user = user;
		this.comment = comment;
		this.replyComment = replyComment;
		this.crFlag = crFlag;
		this.readFlag = readFlag;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public ReplyComment getReplyComment() {
		return replyComment;
	}

	public void setReplyComment(ReplyComment replyComment) {
		this.replyComment = replyComment;
	}

	public Character getCrFlag() {
		return crFlag;
	}

	public void setCrFlag(Character crFlag) {
		this.crFlag = crFlag;
	}

	public Integer getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(Integer readFlag) {
		this.readFlag = readFlag;
	}

	@Override
	public String toString() {
		return "MailMyComment [id=" + id + ", user=" + user + ", comment=" + comment + ", replyComment=" + replyComment
				+ ", crFlag=" + crFlag + ", readFlag=" + readFlag + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
}
