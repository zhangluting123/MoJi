package com.software.moji.user.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.software.moji.entity.User;
import com.software.moji.user.dao.UserDao;
import com.software.moji.user.service.UserService;

/**
 * Servlet implementation class RegistUserServlet
 */
@WebServlet("/RegistUserServlet")
public class RegistUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String phone = request.getParameter("phone");
		String password = request.getParameter("password");
		
		
		
		String string = String.valueOf((int)((Math.random()*9+1)*100))+"_"+phone.substring(7);
		System.out.println(string);
		User newUser = new User();
		newUser.setUserId(string);
		newUser.setPhone(phone);
		newUser.setPassword(password);
		newUser.setUserName(string);
		
		UserService service = new UserService();
		try {
			boolean ifRegist = service.queryPhone(phone);
			if(ifRegist) {
				response.getWriter().write("Regist");
			}else {
				boolean b = service.addUsers(newUser);
				if(b) {
					response.getWriter().write("OK");
					System.out.println("×¢²á³É¹¦");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
