package com.moji.entity;
/** 
* @author:Ming 
* @version data:2020年5月21日 下午3:47:42 
* Class description:
*/
public class Attention {
	private int id;
    private String oneId;
    private String twoId;
    
    public Attention() {
		// TODO Auto-generated constructor stub
	}

	public Attention(int id, String oneId, String twoId) {
		super();
		this.id = id;
		this.oneId = oneId;
		this.twoId = twoId;
	}

	@Override
	public String toString() {
		return "Attention [id=" + id + ", oneId=" + oneId + ", twoId=" + twoId + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOneId() {
		return oneId;
	}

	public void setOneId(String oneId) {
		this.oneId = oneId;
	}

	public String getTwoId() {
		return twoId;
	}

	public void setTwoId(String twoId) {
		this.twoId = twoId;
	}
    
}
