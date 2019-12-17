package cn.edu.hebtu.software.test.Data;

public class Mail {
    private int mailId;
    private String acceptTime;
    private String commentContent;
    private String otherName;
    private String userId;

    public Mail() {

    }
    public Mail(int mailId, String acceptTime, String commentContent, String otherName,String userId) {
        super();
        this.mailId = mailId;
        this.acceptTime = acceptTime;
        this.commentContent = commentContent;
        this.otherName = otherName;
        this.userId = userId;
    }
    public int getMailId() {
        return mailId;
    }


    public String getAcceptTime() {
        return acceptTime;
    }
    public void setMailId(int mailId) {
        this.mailId = mailId;
    }
    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }
    public String getCommentContent() {
        return commentContent;
    }
    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
    public String getOtherName() {
        return otherName;
    }
    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

}
