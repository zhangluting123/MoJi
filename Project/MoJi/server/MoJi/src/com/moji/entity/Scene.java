package com.moji.entity;

/**   
 * @ClassName: Scene   
 * @Description:TODO
 * @author: 张璐婷
 * @date: 2020年5月23日 上午11:33:27       
 */
public class Scene {
	private Integer id;
	private String place;
	private String content;
	private String time;
	private String path;
	private Integer like;
	public Scene() {
		super();
	}
	public Scene(Integer id, String place, String content, String time, String path, Integer like) {
		super();
		this.id = id;
		this.place = place;
		this.content = content;
		this.time = time;
		this.path = path;
		this.like = like;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
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
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getLike() {
		return like;
	}
	public void setLike(Integer like) {
		this.like = like;
	}
	@Override
	public String toString() {
		return "Scene [id=" + id + ", place=" + place + ", content=" + content + ", time=" + time + ", path=" + path
				+ ", like=" + like + "]";
	}
	
	

}
