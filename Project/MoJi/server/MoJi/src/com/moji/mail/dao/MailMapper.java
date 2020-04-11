/**
 * @Title: MailMapper.java
 * @Package com.moji.mail.dao
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月8日
 * @version V1.0
 */
package com.moji.mail.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.moji.entity.Mail;

/**
 * @ClassName: MailMapper
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 春波
 * @date 2020年4月8日
 *
 */
public interface MailMapper {
	
	/**
	 * 
	* @author:      邸凯扬
	* @Description: 查询消息通知
	* @param        @param userid
	* @param        @return
	* @param        @throws SQLException  
	* @return       List<Mail>    
	* @throws
	 */
	public List<Mail> queryMail(@Param("userId")String userId);
	
	/**
	* @author:      邸凯扬 
	* @Description: 删除某一条消息通知
	* @param        @param mailId
	* @param        @return
	* @param        @throws SQLException  
	* @return       boolean    
	* @throws
	 */
	public int deleteMail(@Param("mialId")int mailId);
	
	/**
	* @author:      张璐婷 
	* @Description: 插入消息通知
	* @param        @param mail
	* @param        @return
	* @param        @throws SQLException  
	* @return       boolean    
	* @throws
	 */
	public int insertMail(@Param("mail")Mail mail);
}
