package cn.edu.hebtu.software.test.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ClassName: ReplyComment   
 * @Description: 回复评论
 * @author: 张璐婷
 * @date: 2020年4月17日 上午9:27:15       
 */
public class ReplyComment implements Parcelable {
    private String replyId;
    private Comment comment;
    private User replyUser;
    private String replyContent;
    private String replyTime;
    private ReplyComment replyComment;

    public ReplyComment() {
        super();
    }

    public ReplyComment(String replyId, Comment comment, User replyUser, String replyContent, String replyTime,
                        ReplyComment replyComment) {
        super();
        this.replyId = replyId;
        this.comment = comment;
        this.replyUser = replyUser;
        this.replyContent = replyContent;
        this.replyTime = replyTime;
        this.replyComment = replyComment;
    }


    protected ReplyComment(Parcel in) {
        replyId = in.readString();
        comment = in.readParcelable(Comment.class.getClassLoader());
        replyUser = in.readParcelable(User.class.getClassLoader());
        replyContent = in.readString();
        replyTime = in.readString();
        replyComment = in.readParcelable(ReplyComment.class.getClassLoader());
    }

    public static final Creator<ReplyComment> CREATOR = new Creator<ReplyComment>() {
        @Override
        public ReplyComment createFromParcel(Parcel in) {
            return new ReplyComment(in);
        }

        @Override
        public ReplyComment[] newArray(int size) {
            return new ReplyComment[size];
        }
    };

    public String getReplyId() {
        return replyId;
    }
    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }
    public Comment getComment() {
        return comment;
    }
    public void setComment(Comment comment) {
        this.comment = comment;
    }
    public User getReplyUser() {
        return replyUser;
    }
    public void setReplyUser(User replyUser) {
        this.replyUser = replyUser;
    }
    public String getReplyContent() {
        return replyContent;
    }
    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }
    public String getReplyTime() {
        return replyTime;
    }
    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public ReplyComment getReplyComment() {
        return replyComment;
    }

    public void setReplyComment(ReplyComment replyComment) {
        this.replyComment = replyComment;
    }

    @Override
    public String toString() {
        return "ReplyComment [replyId=" + replyId + ", comment=" + comment + ", replyUser=" + replyUser
                + ", replyContent=" + replyContent + ", replyTime=" + replyTime + ", replyComment=" + replyComment
                + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(replyId);
        dest.writeParcelable(comment, flags);
        dest.writeParcelable(replyUser, flags);
        dest.writeString(replyContent);
        dest.writeString(replyTime);
        dest.writeParcelable(replyComment, flags);
    }
}
