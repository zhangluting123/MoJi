package com.software.moji.comment.dao;
/**
 * @Title: CommentDao.java
 * @Package com.moji.comment.dao
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2019年12月3日
 * @version V1.0
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.software.moji.entity.Comment;
import com.software.moji.entity.User;
import com.software.moji.util.DBUtil;

/**
 * @ClassName: CommentDao
 * @Description: 对数据库comment_detail表进行操作
 * @author 春波
 * @date 2019年12月3日
 */
public class CommentDao {
	
	/**
	 * 
	 * @Title: showComment
	 * @Description: 展示评论
	 * @author 春波
	 * @date 2019年12月5日
	 */
	public List<Comment> showComment(String noteId){
		Connection connection = null;
		Statement stmt1 = null;
		Statement stmt2 = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		List<Comment> list = null;
		String sql1 = "select * from comment_detail where note_id = '" + noteId + "';";
		try {
			connection = DBUtil.getConnection();
			list = new ArrayList<Comment>();
			stmt1 = connection.createStatement();
			rs1 = stmt1.executeQuery(sql1);
			while(rs1.next()) {
				Comment comment = new Comment();
				comment.setId(rs1.getString("comment_id"));
				comment.setNoteId(rs1.getString("note_id"));
				comment.setCommentContent(rs1.getString("comment_content"));
				comment.setCommentTime(rs1.getString("comment_time"));
				comment.setUserId(rs1.getString("comment_user_id"));
				
				String userId = rs1.getString("comment_user_id");
				String sql2 = "select * from user_detail where user_id = '" + userId + "'";
				stmt2 = connection.createStatement();
				rs2 = stmt2.executeQuery(sql2);
				User user = new User();
				if(rs2.next()) {
					user.setUserId(rs2.getString("user_id"));
					user.setUserName(rs2.getString("user_name"));
					user.setUserHeadImg(rs2.getString("avatar_path"));
					user.setSex(rs2.getString("sex"));
					user.setSignature(rs2.getString("signature"));
					user.setOccupation(rs2.getString("occupation"));
					user.setPassword("");
					user.setPhone(rs2.getString("phone"));
					comment.setUser(user);
				}else {
					comment.setUser(null);
				}
				
				list.add(comment);
			}
			DBUtil.closeConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 
	 * @Title: addComment
	 * @Description: 新增评论
	 * @author 春波
	 * @date 2019年12月5日
	 */
	public int addComment(String commentId, String noteId, String userId, String content, String time) {
		Connection connection = null;
		PreparedStatement pstm= null;
		try {
			connection = DBUtil.getConnection();
			String sql = "insert into comment_detail(comment_id,note_id,comment_user_id,comment_content,comment_time) values (?,?,?,?,?);";
			pstm = connection.prepareStatement(sql);
			pstm.setString(1, commentId);
			pstm.setString(2, noteId);
			pstm.setString(3, userId);
			pstm.setString(4, content);
			pstm.setString(5, time);
			int i = pstm.executeUpdate();
			if(i > 0) {
				return 1;
			}else {
				return 0;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
}

