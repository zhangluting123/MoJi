package com.software.moji.quotation.service;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.software.moji.quotation.dao.Dao;

/** 
* @author:Ming 
* @version data:2019年10月14日 下午8:35:27 
* Class description:
* 1.编写具体业务逻辑
* 2.本层即为控制层
*/
public class Service {

	PrintWriter writer;
	ResultSet rs = null;
	
	Dao dao = new Dao();
	
	public String execute(String ts,String cs,String infodata) {
		String back="";
		int t=Integer.parseInt(ts);
		int c=Integer.parseInt(cs);
		//operate
		if(c==1) {//add
			add(t,infodata);
		}else if(c==2) {//delete
			delete(t,infodata);
		}else if(c==3) {//update
			update(t,infodata);
		}else if(c==4) {//query
			back = query(t);
		}
		return back;
	}
	
	public void add(int t,String infodata) {
		String[] splitdata = infodata.split(",");
		try {
			if(t==6) {
				dao.updateData("insert into moji_ana(id,title,content,time,place,image) values(0,null,null,null,null,'"+infodata+"')");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	public void delete(int t,String data) {
		try {
			if(t==6) {
				dao.updateData("delete from moji_ana where id = "+data+"");		
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 

	public void update(int t,String infodata) {
		try {		
			if(t==6) {
				String splitAll[]=infodata.split(",");
//				System.out.println(splitAll[1]);
//				System.out.println(splitAll[2]);
//				System.out.println(splitAll[0]);
				dao.updateData("update moji_ana set title = '"+splitAll[1]+"',content = '"+splitAll[2]+"' where id = "+splitAll[0]+"");
			}		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	public String query(int t) {
		String back="";
		try {
			String table=dao.selectTable(t);
			rs = dao.queryData("select * from "+table+"");
			if(t==6) {
				while(rs.next()) {
					back+=rs.getInt(1)+","+rs.getString(2)+","+rs.getString(3)+","+rs.getString(4)+","+rs.getString(5)+","+rs.getString(6)+";";
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return back;
	} 
}
