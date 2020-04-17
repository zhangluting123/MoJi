package cn.edu.hebtu.software.test.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 春波
 * @ProjectName sendVideo
 * @PackageName：com.example.sendvideo
 * @Description：
 * @time 2020/4/17 9:11
 */
public class Video implements Parcelable {
    private String videoId;//id
    private String path;//路径
    private String title;//标题
    private String duration;//时长
    private String size;//大小
    private String uploadTime;//上传时间
    private User user;//用户信息

    public Video() {
    }

    public Video(String videoId, String path, String title, String duration, String size, String uploadTime, User user) {
        this.videoId = videoId;
        this.path = path;
        this.title = title;
        this.duration = duration;
        this.size = size;
        this.uploadTime = uploadTime;
        this.user = user;
    }

    protected Video(Parcel in) {
        videoId = in.readString();
        path = in.readString();
        title = in.readString();
        duration = in.readString();
        size = in.readString();
        uploadTime = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoId);
        dest.writeString(path);
        dest.writeString(title);
        dest.writeString(duration);
        dest.writeString(size);
        dest.writeString(uploadTime);
        dest.writeParcelable(user, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

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
}
