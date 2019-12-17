package com.software.moji.note.controller;

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
import com.software.moji.entity.Note;
import com.software.moji.note.service.NoteService;

/**
 * Servlet implementation class FindNoteServlet
 */
@WebServlet("/ShowNoteServlet")
public class ShowNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowNoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		String userId = request.getParameter("userId");
		String left = request.getParameter("left");
		String right = request.getParameter("right");
		String top = request.getParameter("top");
		String bottom = request.getParameter("bottom");
		PrintWriter writer = response.getWriter();
		NoteService noteService = new NoteService();
		List<Note> list;
		try {
			list = noteService.findNote(
					userId,
					Double.valueOf(left),
					Double.valueOf(right), 
					Double.valueOf(top), 
					Double.valueOf(bottom)
					);
			if(list.size() > 0) {
				Gson gson = new Gson();
				String str = gson.toJson(list);
				writer.write(str);
				System.out.println("成功查询到数据");
			}else {
				System.out.println("没有查询到对应数据");
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
