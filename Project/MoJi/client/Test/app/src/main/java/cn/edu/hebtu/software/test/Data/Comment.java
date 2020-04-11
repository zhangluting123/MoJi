package cn.edu.hebtu.software.test.Data;



/**
 * @author 春波
 * @ProjectName MoJi
 * @PackageName：com.example.moji
 * @Description：
 * @time 2019/11/28 11:49
 */
public class Comment {
    private User user;
    private String id;
    private String noteId;
    private String userName;
    private String commentContent;
    private String commentTime;

    public Comment(){

    }

    public Comment(User user, String id, String noteId, String userName, String commentContent, String commentTime) {
        this.user = user;
        this.id = id;
        this.noteId = noteId;
        this.userName = userName;
        this.commentContent = commentContent;
        this.commentTime = commentTime;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
                ", userName='" + userName + '\'' +
                ", commentContent='" + commentContent + '\'' +
                ", commentTime='" + commentTime + '\'' +
                '}';
    }
}
