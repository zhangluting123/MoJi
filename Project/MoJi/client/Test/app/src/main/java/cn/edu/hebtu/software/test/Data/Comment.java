package cn.edu.hebtu.software.test.Data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 春波
 * @ProjectName MoJi
 * @PackageName：com.example.moji
 * @Description：
 * @time 2019/11/28 11:49
 */
public class Comment implements Parcelable {
    private User user;
    private String id;
    private String noteId;
    private String commentContent;
    private String commentTime;

    public Comment(){

    }

    public Comment(User user, String id, String noteId, String commentContent, String commentTime) {
        this.user = user;
        this.id = id;
        this.noteId = noteId;
        this.commentContent = commentContent;
        this.commentTime = commentTime;
    }

    protected Comment(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
        id = in.readString();
        noteId = in.readString();
        commentContent = in.readString();
        commentTime = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commnetContent) {
        this.commentContent = commnetContent;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                user.toString()+
                ", id='" + id + '\'' +
                ", noteId='" + noteId + '\'' +
                ", commentContent='" + commentContent + '\'' +
                ", commentTime='" + commentTime + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user, flags);
        dest.writeString(id);
        dest.writeString(noteId);
        dest.writeString(commentContent);
        dest.writeString(commentTime);
    }
}
