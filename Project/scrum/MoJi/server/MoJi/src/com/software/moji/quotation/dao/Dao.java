package com.software.moji.quotation.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.software.moji.util.DBUtil;

/** 
* @author:Ming 
* @version data:2019��10��14�� ����8:40:15 
* Class description:
* ���ݿ���������
*/

public class Dao {
	
	DBUtil util=new DBUtil();
	
	//query
	public ResultSet queryData(String sql) throws ClassNotFoundException, SQLException {
		util.getConnection();
		
		Statement stmt =util.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		//util.closeConnection();
		return rs;	
	}

	//update
	public int updateData(String sql) throws ClassNotFoundException, SQLException{
		util.getConnection();
		Statement stmt = util.getConnection().createStatement();
		stmt.executeUpdate(sql);
		//util.closeConnection();
		return 0;
	}
		
	//Choose form
	public String selectTable(int t) {
		if(t==1) {
			return "cakeshop_cake";
		}else if(t==2) {
			return "cakeshop_order";
		}else if(t==6) {
			return "moji_ana";
		}else{
			return "cakeshop_user";
		}
	}
}
