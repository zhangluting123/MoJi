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
 * @Title:       MoJi
 * @Package      com.software.moji.note.controller
 * @Description: œ¬‘ÿ±„«©
 * @author       ’≈Ë¥Ê√
 * @date         2019.12.2
 * @version V1.0
 */
@WebServlet("/DownloadNoteServlet")
public class DownloadNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadNoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		String userId = request.getParameter("userId");
		try {
			List<Note> list = new NoteService().DownloadNote(userId);
			if(list.size() > 0) {
				Gson gson = new Gson();
				String str = gson.toJson(list);
				System.out.println(str);
				writer.write(str);
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
