package cn.edu.hebtu.software.test.Setting;

import android.app.Activity;
import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.mob.MobSDK;

import java.util.LinkedList;
import java.util.List;

import cn.edu.hebtu.software.test.Data.User;
import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application {
    //ip地址
    private String ip;
    private User user = new User();
    //当前打开的便签id
    private String currentNoteId;

    private Double latitude;
    private Double longitude;
    private String location;
    private String distance;

    private String msgPermission;

    public void onCreate() {

        super.onCreate();
        //MobTech SMSSDK 短信验证
        MobSDK.init(this,"2d667ace6f010","49f6aef48d288b453960dab2fb3d24e9");
        //百度地图初始化
        SDKInitializer.initialize(getApplicationContext());
        //Jpush初始化
        JPushInterface.setDebugMode(true);//允许控制台显示提示信息
        JPushInterface.init(this);
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public String getCurrentNoteId() {
        return currentNoteId;
    }

    public void setCurrentNoteId(String currentNoteId) {
        this.currentNoteId = currentNoteId;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getMsgPermission() {
        return msgPermission;
    }

    public void setMsgPermission(String msgPermission) {
        this.msgPermission = msgPermission;
    }

}
