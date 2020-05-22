package cn.edu.hebtu.software.test.Data;
/**   
 * @ClassName: UserLike   
 * @Description: 点赞
 * @author: 张璐婷
 * @date: 2020年5月22日 上午10:59:26       
 */
public class UserLike {
	private String id;
	private String userId;
	private Note noteLike ;
	private Video videoLike;

	public UserLike() {
		super();
	}

	public UserLike(String id, String userId, Note dbNote, Note noteLike, Video videoLike) {
		super();
		this.id = id;
		this.userId = userId;
		this.noteLike = noteLike;
		this.videoLike = videoLike;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

    public Note getNoteLike() {
        return noteLike;
    }

    public void setNoteLike(Note noteLike) {
        this.noteLike = noteLike;
    }

    public Video getVideoLike() {
		return videoLike;
	}

	public void setVideoLike(Video videoLike) {
		this.videoLike = videoLike;
	}

    @Override
    public String toString() {
        return "UserLike{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", noteLike=" + noteLike +
                ", videoLike=" + videoLike +
                '}';
    }
}
