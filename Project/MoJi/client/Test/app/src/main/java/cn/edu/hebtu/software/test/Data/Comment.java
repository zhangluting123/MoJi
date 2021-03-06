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
    private Integer replyCount;
    private Note note;
    private Video video;

    public Comment(){

    }

    public Comment(User user, String id, String noteId, String commentContent, String commentTime, Integer replyCount, Note note ,Video video) {
        this.user = user;
        this.id = id;
        this.noteId = noteId;
        this.commentContent = commentContent;
        this.commentTime = commentTime;
        this.replyCount = replyCount;
        this.note = note;
        this.video=video;
    }


    protected Comment(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
        id = in.readString();
        noteId = in.readString();
        commentContent = in.readString();
        commentTime = in.readString();
        if (in.readByte() == 0) {
            replyCount = null;
        } else {
            replyCount = in.readInt();
        }
        note = in.readParcelable(Note.class.getClassLoader());
        video = in.readParcelable(Video.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(user, flags);
        dest.writeString(id);
        dest.writeString(noteId);
        dest.writeString(commentContent);
        dest.writeString(commentTime);
        if (replyCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(replyCount);
        }
        dest.writeParcelable(note, flags);
        dest.writeParcelable(video, flags);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "user=" + user +
                ", id='" + id + '\'' +
                ", noteId='" + noteId + '\'' +
                ", commentContent='" + commentContent + '\'' +
                ", commentTime='" + commentTime + '\'' +
                ", replyCount=" + replyCount +
                ", note=" + note +
                ", video=" + video +
                '}';
    }




}
