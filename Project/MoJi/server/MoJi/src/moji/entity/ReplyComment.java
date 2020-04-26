package com.moji.entity;

/**   
 * @ClassName: ReplyComment   
 * @Description:回复评论
 * @author: 张璐婷
 * @date: 2020年4月17日 上午9:27:15       
 */
public class ReplyComment {
	private String replyId;
	private Comment comment;
	private User replyUser;
	private String replyContent;
	private String replyTime;
	private ReplyComment replyComment;
	
	public ReplyComment() {
		super();
	}
	
	public ReplyComment(String replyId, Comment comment, User replyUser, String replyContent, String replyTime,
			ReplyComment replyComment) {
		super();
		this.replyId = replyId;
		this.comment = comment;
		this.replyUser = replyUser;
		this.replyContent = replyContent;
		this.replyTime = replyTime;
		this.replyComment = replyComment;
	}

	public String getReplyId() {
		return replyId;
	}
	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}
	public Comment getComment() {
		return comment;
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}
	public User getReplyUser() {
		return replyUser;
	}
	public void setReplyUser(User replyUser) {
		this.replyUser = replyUser;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	
	public ReplyComment getReplyComment() {
		return replyComment;
	}

	public void setReplyComment(ReplyComment replyComment) {
		this.replyComment = replyComment;
	}

	@Override
	public String toString() {
		return "ReplyComment [replyId=" + replyId + ", comment=" + comment + ", replyUser=" + replyUser
				+ ", replyContent=" + replyContent + ", replyTime=" + replyTime + ", replyComment=" + replyComment
				+ "]";
	}
	
	
}
