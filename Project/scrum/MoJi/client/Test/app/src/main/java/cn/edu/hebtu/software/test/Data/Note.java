package cn.edu.hebtu.software.test.Data;



import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 春波
 * @ProjectName MoJi
 * @PackageName：com.example.moji
 * @Description：
 * @time 2019/11/12 15:08
 */
public class Note implements Parcelable{
    private User user;
    private List<String> imgList = new ArrayList<>();
    private String noteId;
    private Double latitude;
    private Double longitude;
    private String title;
    private String content;
    private String location;
    private String userId;
    private String time;
    private int self;

    public Note(){

    }

    public Note(User user, List<String> imgList, String noteId, Double latitude, Double longitude, String title, String content, String location, String userId, String time, int self) {
        this.user = user;
        this.imgList = imgList;
        this.noteId = noteId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.content = content;
        this.location = location;
        this.userId = userId;
        this.time = time;
        this.self = self;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static Creator<Note> getCREATOR() {
        return CREATOR;
    }

    protected Note(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
        imgList = in.createStringArrayList();
        noteId = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        title = in.readString();
        content = in.readString();
        location = in.readString();
        userId = in.readString();
        time = in.readString();
        self = in.readInt();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

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
        return "Note{" +
                user.toString()+
                ", imgList=" + imgList +
                ", noteId='" + noteId + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", location='" + location + '\'' +
                ", userName='" + userId + '\'' +
                ", time='" + time + '\'' +
                ", self=" + self +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user, flags);
        dest.writeStringList(imgList);
        dest.writeString(noteId);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(location);
        dest.writeString(userId);
        dest.writeString(time);
        dest.writeInt(self);
    }
}
