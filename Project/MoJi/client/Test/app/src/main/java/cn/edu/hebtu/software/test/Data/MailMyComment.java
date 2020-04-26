package cn.edu.hebtu.software.test.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ClassName: MailMyComment   
 * @Description: 评论消息
 * @author: 张璐婷
 * @date: 2020年4月24日 上午9:13:00       
 */
public class MailMyComment implements Parcelable{
	private Integer id;
	private User user;
	private Comment comment;
	private ReplyComment replyComment;
	private Character crFlag;
	private Integer readFlag;
	
	public MailMyComment() {
		super();
	}

	public MailMyComment(Integer id, User user, Comment comment, ReplyComment replyComment, Character crFlag,
			Integer readFlag) {
		super();
		this.id = id;
		this.user = user;
		this.comment = comment;
		this.replyComment = replyComment;
		this.crFlag = crFlag;
		this.readFlag = readFlag;
	}


    protected MailMyComment(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        user = in.readParcelable(User.class.getClassLoader());
        comment = in.readParcelable(Comment.class.getClassLoader());
        replyComment = in.readParcelable(ReplyComment.class.getClassLoader());
        int tmpCrFlag = in.readInt();
        crFlag = tmpCrFlag != Integer.MAX_VALUE ? (char) tmpCrFlag : null;
        if (in.readByte() == 0) {
            readFlag = null;
        } else {
            readFlag = in.readInt();
        }
    }

    public static final Creator<MailMyComment> CREATOR = new Creator<MailMyComment>() {
        @Override
        public MailMyComment createFromParcel(Parcel in) {
            return new MailMyComment(in);
        }

        @Override
        public MailMyComment[] newArray(int size) {
            return new MailMyComment[size];
        }
    };

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public ReplyComment getReplyComment() {
		return replyComment;
	}

	public void setReplyComment(ReplyComment replyComment) {
		this.replyComment = replyComment;
	}

	public Character getCrFlag() {
		return crFlag;
	}

	public void setCrFlag(Character crFlag) {
		this.crFlag = crFlag;
	}

	public Integer getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(Integer readFlag) {
		this.readFlag = readFlag;
	}

	@Override
	public String toString() {
		return "MailMyComment [id=" + id + ", user=" + user + ", comment=" + comment + ", replyComment=" + replyComment
				+ ", crFlag=" + crFlag + ", readFlag=" + readFlag + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeParcelable(user, flags);
        dest.writeParcelable(comment, flags);
        dest.writeParcelable(replyComment, flags);
        dest.writeInt(crFlag != null ? (int) crFlag : Integer.MAX_VALUE);
        if (readFlag == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(readFlag);
        }
    }
}
