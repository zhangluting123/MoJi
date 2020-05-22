/**
 * @Title: UserMapper.java
 * @Package com.moji.user.dao
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月8日
 * @version V1.0
 */
package com.moji.user.dao;

import org.apache.ibatis.annotations.Param;

import com.moji.entity.Attention;
import com.moji.entity.User;

/**
 * @ClassName: UserMapper
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 春波
 * @date 2020年4月8日
 *
 */
public interface UserMapper {
	/**
	 * @author:      张璐婷 
	 * @Description: 根据ID值查询用户信息
	 * @return       User    
	 * @throws
	 */
	public User queryUser(@Param("userId")String userId);
	
	/**
	 * @author:      ming
	 * @Description: 根据ID值查询关注信息
	 * @return       Attention    
	 * @throws
	 */
	public Attention queryAttention(@Param("attention")Attention attention);
	
	/**
	 * @author:      ming
	 * @Description: 删除关注信息
	 * @return       Attention    
	 * @throws
	 */
	public int deleteAttention(@Param("attention")Attention attention);
	
	/**
	* @author:      张璐婷 
	* @Description: 更新用户信息
	* @return       boolean    
	* @throws
	 */
	public int updateMsgOfUser(@Param("user")User user);
	
	/**
	 * 
	 * @Title: updateMsgOfUser
	 * @Description: 更改个人信息不改头像
	 * @author 春波
	 * @date 2019年12月11日
	 */
	public int updateMsgWithoutAvatar(@Param("user")User user);
	
	/**
	 * 
	 * @Title: loginUser
	 * @Description: 用户登录
	 * @author 春波
	 * @date 2019年12月10日
	 */
	public User loginUser(@Param("phone")String phone, @Param("password")String password);
	
	/**
	* @author:      张璐婷 
	* @Description: 查询该手机号是否已注册
	* @param        @param phone
	* @param        @return
	* @param        @throws SQLException  
	* @return       boolean    
	* @throws
	 */
	public User queryPhone(@Param("phone")String phone);
	
	/**
	* @author:      王佳成 
	* @Description: 添加用户 
	* @return       void    
	* @throws
	 */
	public int addUsers(@Param("user")User user);
	
	/**
	* @author:      ming
	* @Description: 添加关注条目
	* @return       void    
	* @throws
	 */
	public int addAttention(@Param("attention")Attention attention);
	
	/**
	* @author:      王佳成 
	* @Description: 修改密码
	* @return       void    
	* @throws
	 */
	public int changeUserPwd(@Param("userId")String userId,@Param("newPwd")String newPwd);
	
	
}
