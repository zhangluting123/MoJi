package com.software.moji.mail.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.software.moji.entity.Mail;
import com.software.moji.entity.Note;
import com.software.moji.mail.service.MailService;
import com.software.moji.note.service.NoteService;

/**
 * Servlet implementation class MailServlet
 */
@WebServlet("/QueryMailServlet")
public class QueryMailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryMailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		String userId=request.getParameter("userId");
		System.out.println(userId);
		try {
			List<Mail> list =  new MailService().equaryMail(userId);
			if(list != null && list.size()>0) {
				Gson gson = new Gson();
				String string = gson.toJson(list);
				writer.write(string);
				System.out.println(string);
			}
		} catch (SQLException e) {
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
