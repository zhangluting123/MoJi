package com.software.moji.note.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.software.moji.entity.Note;
import com.software.moji.entity.User;
import com.software.moji.util.DBUtil;


public class NoteDao {
	/**
	 * 
	* @author:      张璐婷 
	* @Description: 查询指定便签的userId
	* @param        @throws SQLException  
	* @return       String    
	* @throws
	 */
	public Note queryUserId(String noteId) throws SQLException {
		Connection connection = null;
		PreparedStatement pstm = null,pStatement2 = null;
		String userId = null;
		try {
			Note note = new Note();
			connection = DBUtil.getConnection();
			pstm = connection.prepareStatement("select * from note_detail where note_id = ?");
			pstm.setString(1, noteId);
			ResultSet rs = pstm.executeQuery();
			if(rs.next()) {
				note.setLocation(rs.getString(7));
				userId = rs.getString(2);
				pStatement2 = connection.prepareStatement("select * from user_detail where user_id = ?");
				pStatement2.setString(1, userId);
				ResultSet rsUser = pStatement2.executeQuery();
				User user = null;
				if(rsUser.next()) {
					user = new User();
					user.setUserId(userId);
					user.setUserName(rsUser.getString(3));
					note.setUser(user);
				}
			}
			return note;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			DBUtil.closeConnection();
		}
	
	}
	/**
	 * 
	* @author:      张璐婷 
	* @Description: 查询公开note
	* @param        @throws SQLException  
	* @return       List<Note>    
	* @throws
	 */
	public List<Note> queryVisualNote() throws SQLException{
		Connection conn = null;
		PreparedStatement pstm = null,pStatement = null,pStatement2 = null;
		List<Note> list = new ArrayList<>();
		try {
			conn = DBUtil.getConnection();
			pStatement= conn.prepareStatement("select * from note_detail where self = 0;");
			ResultSet rs = pStatement.executeQuery();
			while(rs.next()) {
				Note note = new Note();
				note.setNoteId(rs.getString(1));
				note.setUserId(rs.getString(2));
				note.setTitle(rs.getString(3));
				note.setContent(rs.getString(4));
				note.setLatitude(rs.getDouble(5));
				note.setLongitude(rs.getDouble(6));
				note.setLocation(rs.getString(7));
				note.setTime(rs.getString(8));
				
				pstm = conn.prepareStatement("select path from note_img where noteId = ?;");
				pstm.setString(1, note.getNoteId());
				ResultSet rsImg = pstm.executeQuery();
				List<String> imgList = new ArrayList<>();
				while(rsImg.next()) {
					imgList.add(rsImg.getString(1));
				}
				note.setImgList(imgList);
				
				pStatement2 = conn.prepareStatement("select * from user_detail where user_id = ?");
				pStatement2.setString(1, note.getUserId());
				ResultSet rsUser = pStatement2.executeQuery();
				User user = null;
				if(rsUser.next()) {
					user = new User();
					user.setUserId(rsUser.getString("user_id"));
					user.setUserName(rsUser.getString("user_name"));
					user.setUserHeadImg(rsUser.getString("avatar_path"));
					note.setUser(user);
				}
				
				note.setUser(user);
				list.add(note);
			}
			return list;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DBUtil.closeConnection();
		}
	}
	
	/**
	 * 
	* @author:      张璐婷 
	* @Description: 查询便签
	* @param        @throws SQLException  
	* @return       List<Note>    
	* @throws
	 */
	public List<Note> queryNote(String userId) throws SQLException{
		Connection conn = null;
		PreparedStatement pstm = null,pStatement = null;
		Statement stmt = null;
		List<Note> list = new ArrayList<>();
		try {
			conn = DBUtil.getConnection();
			pStatement= conn.prepareStatement("select * from note_detail where user_id = ?;");
			pStatement.setString(1, userId);
			ResultSet rs = pStatement.executeQuery();
			while(rs.next()) {
				Note note = new Note();
				note.setNoteId(rs.getString(1));
				note.setUserId(rs.getString(2));
				note.setTitle(rs.getString(3));
				note.setContent(rs.getString(4));
				note.setLocation(rs.getString(7));
				note.setTime(rs.getString(8));
				
				User user = new User();
				String sql1 = "select * from user_detail where user_id = '" + userId + "'";
				stmt = conn.createStatement();
				ResultSet rs1 = stmt.executeQuery(sql1);
				if(rs1.next()) {
					user.setUserId(rs1.getString("user_id"));
					user.setUserName(rs1.getString("user_name"));
					user.setUserHeadImg(rs1.getString("avatar_path"));
					user.setSex(rs1.getString("sex"));
					user.setSignature(rs1.getString("signature"));
					user.setOccupation(rs1.getString("occupation"));
					user.setPassword("");
					user.setPhone(rs1.getString("phone"));
					note.setUser(user);
				}
				
				pstm = conn.prepareStatement("select path from note_img where noteId = ?;");
				pstm.setString(1, note.getNoteId());
				ResultSet rsImg = pstm.executeQuery();
				List<String> imgList = new ArrayList<>();
				while(rsImg.next()) {
					imgList.add(rsImg.getString(1));
				}
				note.setImgList(imgList);
				list.add(note);
			}
			return list;
		
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			DBUtil.closeConnection();
		}
	}

	
	/**
	 * 
	 * @Title: deleteNote
	 * @Description: 删除指定便签
	 * @author 春波
	 * @throws SQLException 
	 * @date 2019年12月14日
	 */
	public boolean deleteNote(String noteId) throws SQLException {
		Connection connection = null;
		PreparedStatement pstm= null;
		try {
			connection = DBUtil.getConnection();
			DBUtil.deleteDate("delete from note_img where noteId = '"+noteId+"'");
			pstm = connection.prepareStatement("delete from note_detail where note_id = ?;");
			pstm.setString(1, noteId);
			int i = pstm.executeUpdate();
			if (i > 0) {
				return true;
			}else {
				return false;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			DBUtil.closeConnection();
		}
	}

	/**
	 * 
	 * @Title: findByRange
	 * @Description: 从数据库搜索满足范围条件的note
	 * @author 春波
	 * @throws SQLException 
	 * @date 2019年12月5日
	 */
	public List<Note> findByRange(String userId, double left, double right, double top, double bottom) throws SQLException{
		List<Note> list = null;
		try {
			Connection connection = DBUtil.getConnection();
			String sql = "select * from note_detail where (user_id = '" + userId + "') and (self = 0) and (latitude between " + right + " and " + left + ") and (longitude between " + top + " and " + bottom + ");";
			list = new ArrayList<Note>();
			Statement stmt = null;
			Statement stmt1 = null;
			Statement stmt2 = null;
			ResultSet rs = null;
			ResultSet rs1 = null;
			ResultSet rs2 = null;
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				Note note = new Note();
				note.setNoteId(rs.getString("note_id"));
				note.setLongitude(rs.getDouble("longitude"));
				note.setLatitude(rs.getDouble("latitude"));
				note.setTitle(rs.getString("title"));
				note.setContent(rs.getString("content"));
				note.setTime(rs.getString("time"));
				note.setLocation(rs.getString("location"));
				note.setUserId(userId);
				note.setSelf(rs.getInt("self"));
				
				User user = new User();
				String sql1 = "select * from user_detail where user_id = '" + userId + "'";
				stmt1 = connection.createStatement();
				rs1 = stmt1.executeQuery(sql1);
				if(rs1.next()) {
					user.setUserId(rs1.getString("user_id"));
					user.setUserName(rs1.getString("user_name"));
					user.setUserHeadImg(rs1.getString("avatar_path"));
					user.setSex(rs1.getString("sex"));
					user.setSignature(rs1.getString("signature"));
					user.setOccupation(rs1.getString("occupation"));
					user.setPassword("");
					user.setPhone(rs1.getString("phone"));
					note.setUser(user);
				}
				
				List<String> imgList = new ArrayList<>();
				String sql2 = "select * from note_img where noteId = '" + note.getNoteId() + "'";
				stmt2 = connection.createStatement();
				rs2 = stmt2.executeQuery(sql2);
				while(rs2.next()) {
					imgList.add(rs2.getString("path"));
				}
				note.setImgList(imgList);
//				System.out.println(note.toString());
				list.add(note);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.closeConnection();
		}
		return list;
	}
	
	/**
	 * 
	 * @Title: addNote
	 * @Description: 新增便签
	 * @author 春波
	 * @date 2019年12月5日
	 */
	public int addNote(List<String> pathlist, String id, String userId, String title, String content, double latitude, double longitude, String location, String time, int self) {
		Connection connection = null;
		PreparedStatement pstm= null;
		try {
			
			connection = DBUtil.getConnection();
			String sql = "insert into note_detail(note_id,user_id,title,content,latitude,longitude,location,time,self) values (?,?,?,?,?,?,?,?,?);";
			pstm = connection.prepareStatement(sql);
			pstm.setString(1, id);
			pstm.setString(2, userId);
			pstm.setString(3, title);
			pstm.setString(4, content);
			pstm.setDouble(5, latitude);
			pstm.setDouble(6, longitude);
			pstm.setString(7, location);
			pstm.setString(8, time);
			pstm.setInt(9, self);
			int i = pstm.executeUpdate();
			int j = 0;
			for(int m = 0; m < pathlist.size(); m++) {
				pstm = connection.prepareStatement("insert into note_img (img_id,path,noteId) values (?,?,?);");
				pstm.setString(1, id + m);
				pstm.setString(2, pathlist.get(m));
				pstm.setString(3, id);
				j = pstm.executeUpdate();
			}
			if(pathlist.size() == 0){
				if(i > 0) {
					return 1;
				}else {
					return 0;
				}
			}else {
				if(i > 0 && j > 0) {
					return 1;
				}else {
					return 0;
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 
	 * @Title: checkNote
	 * @Description: 查询自己所在地是否已经存在标记
	 * @author 春波
	 * @date 2019年12月5日
	 */
	public Note checkNote(String userId, double left, double right, double top, double bottom) {
		Note note = null;
		try {
			String sql = "select * from note_detail where user_id = '" + userId + "' and (latitude between " + right + " and " + left + ") and (longitude between " + top + " and " + bottom + ");";
			ResultSet rs = DBUtil.queryData(sql);
			if(rs.next()) {
				note = new Note();
				note.setNoteId(rs.getString("note_id"));
				note.setLongitude(rs.getDouble("longitude"));
				note.setLatitude(rs.getDouble("latitude"));
				note.setTitle(rs.getString("title"));
				note.setContent(rs.getString("content"));
				note.setTime(rs.getString("time"));
				note.setLocation(rs.getString("location"));
				note.setUserId(userId);
				note.setSelf(rs.getInt("self"));
			}
			DBUtil.closeConnection();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return note;
	}
	
	/**
	 * 
	 * @Title: updateNote
	 * @Description: 更新已有的便签
	 * @author 春波
	 * @date 2019年12月5日
	 */
	public int updateNote(List<String> pathlist, Note note, String id, String userId, String title, String content, double latitude, double longitude, String location, String time, int self) {
		Connection connection = null;
		PreparedStatement pstm= null;
		try {
			connection = DBUtil.getConnection();
			pstm = connection.prepareStatement("delete from note_img where noteId = ?;");
			pstm.setString(1, note.getNoteId());
			pstm.executeUpdate();
			
			String sql = "update note_detail set note_id=?,user_id=?,title=?,content=?,latitude=?,longitude=?,location=?,time=?,self=? where latitude=? and longitude=?";
			pstm = connection.prepareStatement(sql);
			pstm.setString(1, id);
			pstm.setString(2, userId);
			pstm.setString(3, title);
			pstm.setString(4, content);
			pstm.setDouble(5, latitude);
			pstm.setDouble(6, longitude);
			pstm.setString(7, location);
			pstm.setString(8, time);
			pstm.setInt(9, self);
			pstm.setDouble(10, note.getLatitude());
			pstm.setDouble(11, note.getLongitude());
			int i = pstm.executeUpdate();
			int j = 0;
			for(int m = 0; m < pathlist.size(); m++) {
				pstm = connection.prepareStatement("insert into note_img(img_id,path,noteId) values (?,?,?);");
				pstm.setString(1, id + m);
				pstm.setString(2, pathlist.get(m));
				pstm.setString(3, id);
				j = pstm.executeUpdate();
			}
			if(pathlist.size() == 0){
				if(i > 0) {
					return 1;
				}else {
					return 0;
				}
			}else {
				if(i > 0 && j > 0) {
					return 1;
				}else {
					return 0;
				}
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
