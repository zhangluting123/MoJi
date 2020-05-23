/**
 * @Title: Note.java
 * @Package com.moji.entity
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月8日
 * @version V1.0
 */
package com.moji.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: Note
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 春波
 * @date 2019年11月12日
 */
public class Note {
	private String noteId;
	private Double latitude;//维度，X，左大右小
	private Double longitude;//经度，y，上小下大
	private String title;
	private String content;
	private String location;
	private String time;
	private String userId;
	private int self;
	private int like;
	private User user;
	private List<Img> imgList = new ArrayList<>();

	public Note() {
	}

	

	public Note(String noteId, Double latitude, Double longitude, String title, String content, String location,
			String time, String userId, int self, int like, User user, List<Img> imgList) {
		super();
		this.noteId = noteId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.title = title;
		this.content = content;
		this.location = location;
		this.time = time;
		this.userId = userId;
		this.self = self;
		this.like = like;
		this.user = user;
		this.imgList = imgList;
	}



	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getSelf() {
		return self;
	}

	public void setSelf(int self) {
		this.self = self;
	}

	public List<Img> getImgList() {
		return imgList;
	}

	public void setImgList(List<Img> imgList) {
		this.imgList = imgList;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNoteId() {
		return noteId;
	}

	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getLike() {
		return like;
	}

	public void setLike(int like) {
		this.like = like;
	}

	@Override
	public String toString() {
		return "Note [" + user.toString() + "latitude=" + latitude + ", longitude=" + longitude + ", content=" + content + ", time=" + time
				+ "]";
	}
	
	
	

}
