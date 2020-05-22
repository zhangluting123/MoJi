package cn.edu.hebtu.software.test.Data;
//ming
public class Follow {
    private int id;
    private String oneId;
    private String twoId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOneId() {
        return oneId;
    }

    public void setOneId(String oneId) {
        this.oneId = oneId;
    }

    public String getTwoId() {
        return twoId;
    }

    public void setTwoId(String twoId) {
        this.twoId = twoId;
    }

    public Follow(int id, String oneId, String twoId) {
        this.id = id;
        this.oneId = oneId;
        this.twoId = twoId;
    }
}
