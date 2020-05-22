/**
 * @Title: UserService.java
 * @Package com.moji.user.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月8日
 * @version V1.0
 */
package com.moji.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moji.entity.Attention;
import com.moji.entity.User;
import com.moji.user.dao.UserMapper;

/**
 * @ClassName: UserService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 春波
 * @date 2020年4月8日
 *
 */
@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;
	
	public User queryUser(String userId) {
		return this.userMapper.queryUser(userId);
	}
	//@author ming
	public List<User> queryFollowed(String userId) {
		return this.userMapper.queryFollowed(userId);
	}
	//@author ming
	public Attention queryAttention(Attention attention) {
		return this.userMapper.queryAttention(attention);
	}
	//@author ming
	public int deleteAttention(Attention attention) {
		return this.userMapper.deleteAttention(attention);
	}
	public int updateUserMsg(User user) {
		return this.userMapper.updateMsgOfUser(user);
	}
	public int updateUserWithoutAvatar(User user) {
		return this.userMapper.updateMsgWithoutAvatar(user);
	}
	public User loginUser(String phone, String password) {
		return this.userMapper.loginUser(phone, password);
	}
	public int addUsers(User user) {
		return this.userMapper.addUsers(user);
	}
	//@author ming
	public int addAttention(Attention attention) {
		return this.userMapper.addAttention(attention);
	}
	public int changeUserPwd(String user,String newPwd) {
		return this.userMapper.changeUserPwd(user, newPwd);
	}
	public User queryPhone(String phone) {
		return this.userMapper.queryPhone(phone);
	}
}
