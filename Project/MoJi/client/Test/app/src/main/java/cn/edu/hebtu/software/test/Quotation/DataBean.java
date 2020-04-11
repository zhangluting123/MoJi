package cn.edu.hebtu.software.test.Quotation;

public class DataBean {

    private int index;
    private String title;
    private String content;
    private String time;
    private String place;
    private String image;

    public DataBean(int index,String title,String content,String image){
        this.index=index;
        this.title=title;
        this.content=content;
        this.image=image;
    }

    public DataBean(int index,String title,String content,String time,String place,String image){
        this.index=index;
        this.title=title;
        this.content=content;
        this.time=time;
        this.place=place;
        this.image=image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
