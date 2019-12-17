package com.software.moji.user.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.software.moji.entity.User;
import com.software.moji.user.service.UserService;

/**
 * Servlet implementation class loginUserServlet
 */
@WebServlet("/loginUserServlet")
public class loginUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public loginUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		String phone = request.getParameter("phone");
		String pwd = request.getParameter("password");
		
		PrintWriter writer = response.getWriter();
		UserService userService = new UserService();
		User user = userService.loginUser(phone, pwd);
		if(user == null) {
			writer.write("0");
			System.out.println("手机号或密码不正确");
		}else {
			Gson gson = new Gson();
			String jsonStr = gson.toJson(user);
			writer.write(jsonStr);
			System.out.println(jsonStr);
			System.out.println("登录成功");
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
