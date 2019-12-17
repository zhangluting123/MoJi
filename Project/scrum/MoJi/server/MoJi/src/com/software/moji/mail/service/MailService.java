package com.software.moji.mail.service;

import java.sql.SQLException;
import java.util.List;

import com.software.moji.entity.Mail;
import com.software.moji.mail.dao.MailDao;

public class MailService {
	public List<Mail> equaryMail(String userid) throws SQLException{
		return new MailDao().queryMail(userid);
	}
	public boolean deleteMail(int mailId) throws SQLException{
		return new MailDao().deleteMail(mailId);
	}
	public boolean insertMail(Mail mail) throws SQLException {
		return new MailDao().insertMail(mail);
	}
}
