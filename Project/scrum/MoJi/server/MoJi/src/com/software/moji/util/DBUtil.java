package com.software.moji.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	private static Connection conn;
	private static ResultSet rs;
	private static Statement stmt;
	private final static String DRIVER = "com.mysql.jdbc.Driver";
	private final static String CONN_STR = "jdbc:mysql://127.0.0.1:3306/moji";
	private final static String USER = "root";
	private final static String Pwd = "";
	/**
	 * @获取数据库的连接
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static Connection getConnection() throws SQLException,ClassNotFoundException{
		if(null == conn || conn.isClosed()) {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(CONN_STR,USER,Pwd);
		}
		return conn;
	}
	/**
	 * @查询数据
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static ResultSet queryData(String sql) throws ClassNotFoundException, SQLException {
		getConnection();
		if(null == stmt || stmt.isClosed()) {
			stmt = conn.createStatement();
		}
		if(null == rs || rs.isClosed() ) {
			rs = stmt.executeQuery(sql);
		}
		return rs;
	}
	/**
	 * @更新数据
	 * @param sql
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static int updateDate(String sql) throws ClassNotFoundException, SQLException {
		getConnection();
		if(null == stmt || stmt.isClosed()) {
			stmt = conn.createStatement();
		}
		int n = stmt.executeUpdate(sql);
		if(n > 0) {
			System.out.println("修改成功！");
		}else {
			System.out.println("修改失败！");
		}
		return 0;
	}
	/**
	 * @删除数据
	 * @param sql
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static int deleteDate(String sql) throws ClassNotFoundException, SQLException {
		getConnection();
		if(null == stmt || stmt.isClosed()) {
			stmt = conn.createStatement();
		}
		int n = stmt.executeUpdate(sql);
		if(n > 0) {
			System.out.println("删除成功！");
		}else {
			System.out.println("删除失败！");
		}
		return 0;
	}
	/**
	 * @添加数据
	 * @param sql
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static int insertDate(String sql) throws ClassNotFoundException, SQLException {
		getConnection();
		if(null == stmt || stmt.isClosed()) {
			stmt = conn.createStatement();
		}
		int n = stmt.executeUpdate(sql);
		if(n > 0) {
			System.out.println("添加成功！");
		}else {
			System.out.println("添加失败！");
		}
		return 0;
	}
	/**
	 * @关闭连接
	 * @throws SQLException
	 */
	public static  void closeConnection() throws SQLException {
		if(null != conn && !conn.isClosed()) {
			conn.close();
		}
		if(null != stmt && !stmt.isClosed()) {
			stmt.close();
		}
		if(null != rs && !rs.isClosed()) {
			rs.close();
		}
	}
}

