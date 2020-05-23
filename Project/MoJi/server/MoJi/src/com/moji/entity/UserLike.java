package com.moji.entity;
/**   
 * @ClassName: UserLike   
 * @Description:TODO
 * @author: 张璐婷
 * @date: 2020年5月22日 上午10:59:26       
 */
public class UserLike {
	private String id;
	private String userId;
	private Note dbNote;
	private ReturnNote noteLike = new ReturnNote();
	private Video videoLike;
	
	public UserLike() {
		super();
	}
	
	

	public UserLike(String id, String userId, Note dbNote, ReturnNote noteLike, Video videoLike) {
		super();
		this.id = id;
		this.userId = userId;
		this.dbNote = dbNote;
		this.noteLike = noteLike;
		this.videoLike = videoLike;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Note getDbNote() {
		return dbNote;
	}

	public void setDbNote(Note dbNote) {
		this.dbNote = dbNote;
	}

	public ReturnNote getNoteLike() {
		return noteLike;
	}

	public void setNoteLike(ReturnNote noteLike) {
		this.noteLike = noteLike;
	}

	public Video getVideoLike() {
		return videoLike;
	}

	public void setVideoLike(Video videoLike) {
		this.videoLike = videoLike;
	}

	@Override
	public String toString() {
		return "UserLike [id=" + id + ", userId=" + userId + ", dbNote=" + dbNote + ", noteLike=" + noteLike
				+ ", videoLike=" + videoLike + "]";
	}

	

	

}
