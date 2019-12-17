package com.software.moji.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.software.moji.entity.Note;
import com.software.moji.entity.User;
import com.software.moji.util.DBUtil;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

public class UserDao {
	/**
	 * @author:      张璐婷 
	 * @Description: 根据ID值查询用户信息
	 * @return       User    
	 * @throws
	 */
	public User queryUser(String userId) throws SQLException {
		Connection connection = null;
		PreparedStatement pstm = null;
		try {
			User user = null;
			connection = DBUtil.getConnection();
			pstm = connection.prepareStatement("select * from user_detail  where user_id = ?");
			pstm.setString(1, userId);
			ResultSet rs = pstm.executeQuery();
			if(rs.next()) {
				user = new User();
				user.setUserName(rs.getString(3));
			}
			return user;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			DBUtil.closeConnection();
		}
	}
	/**
	* @author:      张璐婷 
	* @Description: 更新用户信息
	* @return       boolean    
	* @throws
	 */
	public boolean updateMsgOfUser(User user) throws SQLException {
		try {
			PreparedStatement pstm = DBUtil.getConnection().prepareStatement("update user_detail set avatar_path=?,user_name=?,sex=?,signature=?,occupation=? where user_id=? ;");
			pstm.setString(1, user.getUserHeadImg());
			pstm.setString(2, user.getUserName());
			pstm.setString(3, user.getSex());
			pstm.setString(4, user.getSignature());
			pstm.setString(5, user.getOccupation());
			pstm.setString(6, user.getUserId());
			pstm.executeUpdate();
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			DBUtil.closeConnection();
		}
	}
	
	/**
	 * 
	 * @Title: updateMsgOfUser
	 * @Description: 更改个人信息不改头像
	 * @author 春波
	 * @date 2019年12月11日
	 */
	public boolean updateMsgWithoutAvatar(User user) throws SQLException {
		try {
			PreparedStatement pstm = DBUtil.getConnection().prepareStatement("update user_detail set user_name=?,sex=?,signature=?,occupation=? where user_id=? ;");
			pstm.setString(1, user.getUserName());
			pstm.setString(2, user.getSex());
			pstm.setString(3, user.getSignature());
			pstm.setString(4, user.getOccupation());
			pstm.setString(5, user.getUserId());
			pstm.executeUpdate();
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			DBUtil.closeConnection();
		}
	}
	
	/**
	 * 
	 * @Title: loginUser
	 * @Description: 用户登录
	 * @author 春波
	 * @date 2019年12月10日
	 */
	public User loginUser(String phone, String password) {
		Connection connection = null;
		PreparedStatement pstm = null;
		User user = null;
		try {
			connection = DBUtil.getConnection();
			pstm = connection.prepareStatement("select * from user_detail where phone = ? and password = ?");
			pstm.setString(1, phone);
			pstm.setString(2, password);
			ResultSet rs = pstm.executeQuery();
			if(rs.next()) {
				user = new User();
				user.setUserId(rs.getString(1));
				user.setUserName(rs.getString(3));
				user.setUserHeadImg(rs.getString(2));
				user.setSex(rs.getString(4));
				user.setSignature(rs.getString(5));
				user.setOccupation(rs.getString(6));
				user.setPassword(rs.getString(7));
				user.setPhone(rs.getString(8));
			}
			return user;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	* @author:      张璐婷 
	* @Description: 查询该手机号是否已注册
	* @param        @param phone
	* @param        @return
	* @param        @throws SQLException  
	* @return       boolean    
	* @throws
	 */
	public boolean queryPhone(String phone) throws SQLException {
		Connection conn = null;
		PreparedStatement pstm  = null;
		try {
			conn = DBUtil.getConnection();
			pstm = conn.prepareStatement("select * from user_detail where phone = ?");
			pstm.setString(1, phone);
			ResultSet rs = pstm.executeQuery();
			if(rs.next()) {
				return true;
			}else {
				return false;
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return true;
		} finally {
			DBUtil.closeConnection();
		}
		
	}
	
	/**
	* @author:      王佳成 
	* @Description: 添加用户 
	* @return       void    
	* @throws
	 */
		public boolean addUsers(User user) throws SQLException {
			try {
				//连接对象
				Connection conn = DBUtil.getConnection();;
				// 传递sql语句
			    PreparedStatement pst;
				
				
				//获取用户信息
				String id = user.getUserId();
				String name = user.getUserName();
				String phone = user.getPhone();
				String pwd = user.getPassword();
				
				//定义语句
				String add = "insert into user_detail(user_id,user_name,password,phone) values (?,?,?,?);";
				//添加数据
				pst = conn.prepareStatement(add);
				
				pst.setString(1, id);
				pst.setString(2, name);
				pst.setString(3, pwd);
				pst.setString(4, phone);
				
				pst.executeUpdate();
				return true;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} finally{
				DBUtil.closeConnection();
			}
		}
		
		/**
		* @author:      王佳成 
		* @Description: 修改密码
		* @return       void    
		* @throws
		 */
		public boolean changeUserPwd(String user,String newPwd) throws SQLException {
			
			try {
				//连接对象
				Connection conn = DBUtil.getConnection();
				// 传递sql语句
			    PreparedStatement pst;
			    //update语句
			    String sql = "update user_detail set password=? where user_id=?";
			    
			    pst = conn.prepareStatement(sql);
			    pst.setString(1, newPwd);
			    pst.setString(2, user);
			    
			    pst.executeUpdate();
			    return true;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}finally {
				DBUtil.closeConnection();
			}
			
		}
}
