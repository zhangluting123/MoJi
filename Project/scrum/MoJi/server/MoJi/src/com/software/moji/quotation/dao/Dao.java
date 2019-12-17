package com.software.moji.quotation.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.software.moji.util.DBUtil;

/** 
* @author:Ming 
* @version data:2019年10月14日 下午8:40:15 
* Class description:
* 数据库具体操作层
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
