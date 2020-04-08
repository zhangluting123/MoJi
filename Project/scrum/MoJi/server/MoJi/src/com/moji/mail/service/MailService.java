/**
 * @Title: MailService.java
 * @Package com.moji.mail.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月8日
 * @version V1.0
 */
package com.moji.mail.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moji.entity.Mail;
import com.moji.mail.dao.MailMapper;

/**
 * @ClassName: MailService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 春波
 * @date 2020年4月8日
 *
 */
@Service
public class MailService {

	@Autowired
	private MailMapper mailMapper;
	
	public List<Mail> quaryMail(String userId) throws SQLException{
		return this.mailMapper.queryMail(userId);
	}
	public int deleteMail(int mailId) throws SQLException{
		return this.mailMapper.deleteMail(mailId);
	}
	public int insertMail(Mail mail) throws SQLException {
		return this.mailMapper.insertMail(mail);
	}

}
