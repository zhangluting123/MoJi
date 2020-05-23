package com.moji.userlike.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.moji.entity.UserLike;
import com.moji.userlike.dao.UserLikeMapper;

/**   
 * @ClassName: UserLikeService   
 * @Description:TODO
 * @author: 张璐婷
 * @date: 2020年5月22日 上午11:07:30       
 */
@Service
public class UserLikeService {
	
	@Resource
	private UserLikeMapper userLikeMapper;
	
	public int insertUserLike(String id,String userId,String noteId,String videoId) {
		return this.userLikeMapper.insertUserLike(id,userId, noteId, videoId);
	}

	public int deleteUserLike(String likeId) {
		return this.userLikeMapper.deleteUserLike(likeId);
	}
	
	public List<UserLike> queryUserLike(String userId) {
		return this.userLikeMapper.queryUserLikeByUserId(userId);
	}
}
