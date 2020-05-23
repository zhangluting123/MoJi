package com.moji.userlike.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.moji.entity.UserLike;

/**   
 * @ClassName: UserLikeMapper   
 * @Description:TODO
 * @author: 张璐婷
 * @date: 2020年5月22日 上午11:06:53       
 */
public interface UserLikeMapper {
	
	/**
	 * @Title: insertUserLike
	 * @Description: 添加用户点赞
	 * @author: 张璐婷 
	 * @date: 2020年5月22日 上午11:11:02
	 */
	public int insertUserLike(@Param("id")String id,
			@Param("userId")String userId,
			@Param("noteId")String noteId,
			@Param("videoId")String videoId);

	/**
	 * @Title: deleteUserLike
	 * @Description: 删除点赞信息
	 * @author: 张璐婷 
	 * @date: 2020年5月22日 上午11:25:56
	 */
	public int deleteUserLike(@Param("likeId")String likeId);
	
	/**
	 * @Title: queryUserLikeByUserId
	 * @Description: 查询用户点赞信息
	 * @author: 张璐婷 
	 * @date: 2020年5月22日 上午11:27:40
	 */
	public List<UserLike> queryUserLikeByUserId(@Param("userId")String userId);
}
