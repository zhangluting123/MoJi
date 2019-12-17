package com.software.moji.mail.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.software.moji.entity.Mail;
import com.software.moji.util.DBUtil;

public class MailDao {
	/**
	 * 
	* @author:      ۡ����
	* @Description: ��ѯ��Ϣ֪ͨ
	* @param        @param userid
	* @param        @return
	* @param        @throws SQLException  
	* @return       List<Mail>    
	* @throws
	 */
	public List<Mail> queryMail(String userid)throws SQLException{
		Connection connection=null;
		List<Mail>list=new ArrayList<>();
		PreparedStatement preparedStatement=null;
		 try {
			connection =DBUtil.getConnection();
			preparedStatement=connection.prepareStatement("select * from mail_msg where userid=?");
			preparedStatement.setString(1, userid);
			ResultSet rs=preparedStatement.executeQuery();
			while (rs.next()) {
				Mail mail=new Mail();
				mail.setMailId(rs.getInt(1));
				mail.setAcceptTime(rs.getString(2));
				mail.setCommentContent(rs.getString(3));
				mail.setOtherName(rs.getString(4));
				list.add(mail);
				
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}finally {
			DBUtil.closeConnection();
		}
	}
	/**
	* @author:      ۡ���� 
	* @Description: ɾ��ĳһ����Ϣ֪ͨ
	* @param        @param mailId
	* @param        @return
	* @param        @throws SQLException  
	* @return       boolean    
	* @throws
	 */
	public boolean deleteMail(int mailId)throws SQLException{
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		 try {
			connection =DBUtil.getConnection();
			preparedStatement=connection.prepareStatement("delete from mail_msg where mail_id=?");
			preparedStatement.setInt(1, mailId);
			preparedStatement.executeUpdate();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}finally {
			DBUtil.closeConnection();
		}
	}
	/**
	* @author:      ����� 
	* @Description: ������Ϣ֪ͨ
	* @param        @param mail
	* @param        @return
	* @param        @throws SQLException  
	* @return       boolean    
	* @throws
	 */
	public boolean insertMail(Mail mail) throws SQLException {
		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = DBUtil.getConnection();
			pstm = conn.prepareStatement("insert into mail_msg(acceptTime,comment_content,otherName,userId) values (?,?,?,?)");
			pstm.setString(1, mail.getAcceptTime());
			pstm.setString(2, mail.getCommentContent());
			pstm.setString(3, mail.getOtherName());
			pstm.setString(4, mail.getUserId());
			pstm.executeUpdate();
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			DBUtil.closeConnection();
		}
	}
	

}
