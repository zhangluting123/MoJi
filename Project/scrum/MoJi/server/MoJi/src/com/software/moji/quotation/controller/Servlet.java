package com.software.moji.quotation.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.software.moji.quotation.service.Service;

/**
 * 1.主要任务:接收客户端数据 调用服务
 * 2.--数据显示
 * 3.**数据监测
 * Servlet implementation class Servlet
 */
@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Servlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	
    	PrintWriter writer;
		//设置编码方式
    	request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		//接收数据
		String ts = request.getParameter("infot");
		String cs = request.getParameter("infoc");
		String infodata = request.getParameter("infod");
//		System.out.println("----------------------------------------");
//		System.out.println("--Data received from the client");
//		System.out.println("--Table:"+ts);
//		System.out.println("--Command:"+cs);
//		System.out.println("--Parameter:"+infodata);
//		System.out.println("----------------------------------------");
		//测试与客户端连接是否成功
		response.resetBuffer();
		writer = response.getWriter();
		writer.write("name=leon;pwd=886;success");
		//调用service
		Service service = new Service();
		String back = service.execute(ts, cs,infodata);
		//向客户端返回数据
		if(cs.equals("4")) {
			response.resetBuffer();
			writer = response.getWriter();
			writer.write(back);
		}
		
//		System.out.println("----------------------------------------");
//		System.out.println("--Returns data to the client");
//		System.out.println("--Return data:"+back);
//		System.out.println("----------------------------------------");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
