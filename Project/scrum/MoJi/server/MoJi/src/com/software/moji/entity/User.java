package com.software.moji.entity;

public class User {
	private String userId;
    private String userHeadImg;
    private String userName;
    private String sex;
    private String signature;
    private String occupation;
    private String password;
    private String phone;

    public User() {
    }

    public User(String userId, String userHeadImg, String userName, String sex, String signature, String occupation, String password, String phone) {
        this.userId = userId;
        this.userHeadImg = userHeadImg;
        this.userName = userName;
        this.sex = sex;
        this.signature = signature;
        this.occupation = occupation;
        this.password = password;
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userHeadImg=" + userHeadImg + ", userName=" + userName + ", sex=" + sex
				+ ", signature=" + signature + ", occupation=" + occupation + ", password=" + password + ", phone="
				+ phone + "]";
	}

    
}
