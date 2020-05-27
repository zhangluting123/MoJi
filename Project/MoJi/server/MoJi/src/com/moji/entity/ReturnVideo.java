package com.moji.entity;
/** 
* @author:Ming 
* @version data:2020年5月27日 上午11:40:08 
* Class description:
*/
public class ReturnVideo {

	 private String videoId;//id
	 private String path;//路径
	 private String title;//标题
	 private String content;//内容
	 private String duration;//时长
	 private String size;//大小
	 private String uploadTime;//上传时间
	 private String tag;//分类
	 private int like;//点赞数量
	 private User user;//用户信息
	 
	 public ReturnVideo() {
		// TODO Auto-generated constructor stub
	}
	 
	public ReturnVideo(String videoId, String path, String title, String content, String duration, String size,
			String uploadTime, String tag, int like, User user) {
		super();
		this.videoId = videoId;
		this.path = path;
		this.title = title;
		this.content = content;
		this.duration = duration;
		this.size = size;
		this.uploadTime = uploadTime;
		this.tag = tag;
		this.like = like;
		this.user = user;
	}
	 
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getLike() {
		return like;
	}
	public void setLike(int like) {
		this.like = like;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
