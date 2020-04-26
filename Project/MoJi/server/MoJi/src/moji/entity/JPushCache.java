package com.moji.entity;

/**   
 * @ClassName: JPushCache   
 * @Description:TODO
 * @author: 张璐婷
 * @date: 2020年4月26日 下午2:10:31       
 */
public class JPushCache {
	private Integer jpushId;
	private String receiveId;
	private String sendUserName;
	private String jpushExtras;
	private Character jpushFlag;
	
	public JPushCache() {
		super();
	}

	public JPushCache(Integer jpushId, String receiveId, String sendUserName, String jpushExtras, Character jpushFlag) {
		super();
		this.jpushId = jpushId;
		this.receiveId = receiveId;
		this.sendUserName = sendUserName;
		this.jpushExtras = jpushExtras;
		this.jpushFlag = jpushFlag;
	}

	public Integer getJpushId() {
		return jpushId;
	}

	public void setJpushId(Integer jpushId) {
		this.jpushId = jpushId;
	}

	public String getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public String getJpushExtras() {
		return jpushExtras;
	}

	public void setJpushExtras(String jpushExtras) {
		this.jpushExtras = jpushExtras;
	}

	public Character getJpushFlag() {
		return jpushFlag;
	}

	public void setJpushFlag(Character jpushFlag) {
		this.jpushFlag = jpushFlag;
	}
	
	
	
	
}
