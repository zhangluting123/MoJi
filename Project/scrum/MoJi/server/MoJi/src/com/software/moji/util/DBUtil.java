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
	 * @��ȡ���ݿ������
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
	 * @��ѯ����
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
	 * @��������
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
			System.out.println("�޸ĳɹ���");
		}else {
			System.out.println("�޸�ʧ�ܣ�");
		}
		return 0;
	}
	/**
	 * @ɾ������
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
			System.out.println("ɾ���ɹ���");
		}else {
			System.out.println("ɾ��ʧ�ܣ�");
		}
		return 0;
	}
	/**
	 * @�������
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
			System.out.println("��ӳɹ���");
		}else {
			System.out.println("���ʧ�ܣ�");
		}
		return 0;
	}
	/**
	 * @�ر�����
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

