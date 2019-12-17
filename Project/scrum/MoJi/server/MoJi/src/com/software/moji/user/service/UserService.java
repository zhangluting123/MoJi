package com.software.moji.user.service;

import java.sql.SQLException;

import com.software.moji.entity.User;
import com.software.moji.user.dao.UserDao;

public class UserService {
	public User queryUser(String userId) throws SQLException {
		return new UserDao().queryUser(userId);
	}
	public boolean updateUserMsg(User user) throws SQLException {
		return new UserDao().updateMsgOfUser(user);
	}
	public boolean updateUserWithoutAvatar(User user) throws SQLException {
		return new UserDao().updateMsgWithoutAvatar(user);
	}
	public User loginUser(String phone, String password) {
		return new UserDao().loginUser(phone, password);
	}
	public boolean addUsers(User user) throws SQLException {
		return new UserDao().addUsers(user);
	}
	public boolean changeUserPwd(String user,String newPwd) throws SQLException {
		return new UserDao().changeUserPwd(user, newPwd);
	}
	public boolean queryPhone(String phone) throws SQLException {
		return new UserDao().queryPhone(phone);
	}
}
