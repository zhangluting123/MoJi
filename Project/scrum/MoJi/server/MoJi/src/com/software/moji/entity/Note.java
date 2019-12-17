package com.software.moji.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Note
 * @Description: TODO(������һ�仰��������������)
 * @author ����
 * @date 2019��11��12��
 */
public class Note {
	private User user;
	private List<String> imgList = new ArrayList<>();
	private String noteId;
	private Double latitude;//ά�ȣ�X�������С
	private Double longitude;//���ȣ�y����С�´�
	private String title;
	private String content;
	private String location;
	private String time;
	private String userId;
	private int self;

	public Note() {
	}

	public Note(User user, List<String> imgList, String noteId, Double latitude, Double longitude, String title,
			String content, String location, String time, String userId, int self) {
		super();
		this.user = user;
		this.imgList = imgList;
		this.noteId = noteId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.title = title;
		this.content = content;
		this.location = location;
		this.time = time;
		this.userId = userId;
		this.self = self;
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

	public List<String> getImgList() {
		return imgList;
	}

	public void setImgList(List<String> imgList) {
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

	@Override
	public String toString() {
		return "Note [" + user.toString() + "latitude=" + latitude + ", longitude=" + longitude + ", content=" + content + ", time=" + time
				+ "]";
	}
	
	

}