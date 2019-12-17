package com.software.moji.note.controller;

import java.io.IOException;
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
 * @Title:       MoJi
 * @Package      com.software.moji.note.controller
 * @Description: 查询可见便签
 * @author       张璐婷
 * @date         2019.12.10
 * @version V1.0
 */
@WebServlet("/QueryVisualNoteServlet")
public class QueryVisualNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryVisualNoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		try {
			List<Note> list = new NoteService().queryVisualNote();
			if(list.size() > 0) {
				Gson gson = new Gson();
				String str = gson.toJson(list);
				response.getWriter().write(str);
		
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
