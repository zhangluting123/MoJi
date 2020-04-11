package cn.edu.hebtu.software.test.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
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

    protected User(Parcel in) {
        userId = in.readString();
        userHeadImg = in.readString();
        userName = in.readString();
        sex = in.readString();
        signature = in.readString();
        occupation = in.readString();
        password = in.readString();
        phone = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userHeadImg);
        dest.writeString(userName);
        dest.writeString(sex);
        dest.writeString(signature);
        dest.writeString(occupation);
        dest.writeString(password);
        dest.writeString(phone);
    }
}
