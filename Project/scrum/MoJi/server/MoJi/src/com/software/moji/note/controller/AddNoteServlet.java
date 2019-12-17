package com.software.moji.note.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.software.moji.entity.Note;
import com.software.moji.note.service.NoteService;

/**
 * Servlet implementation class AddNoteServlet
 */
@WebServlet("/AddNoteServlet")
public class AddNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddNoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		String userId = null;
		String title = null;
		String content = null;
		String latitude = null;
		String longitude = null;
		String self = null;
		String location = null;
		
		Double right = null;
		Double left = null;
		Double top = null;
		Double bottom = null;
	
		Date date = new Date();
		//将日期格式化
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMddHHmmss");
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//转换成字符串格式
		String id = null;
		String time = simpleDateFormat1.format(date);
			
		List<String > pathList = new ArrayList<String>();
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		PrintWriter writer = response.getWriter();
		NoteService noteService = new NoteService();
		
		try {
			List<FileItem> items = upload.parseRequest(request);
			for(FileItem fi : items) {
				if(fi.isFormField()) {
					System.out.println(fi.getString("UTF-8"));
					switch (fi.getFieldName()) {
						case "userId":
							userId = fi.getString("UTF-8");
							id = simpleDateFormat.format(date) + userId;
							System.out.println(id);
							break;
						case "title":
							title = fi.getString("UTF-8");
							break;
						case "content":
							content = fi.getString("UTF-8");
							break;
						case "latitude":
							latitude = fi.getString("UTF-8");
							right = Double.valueOf(latitude) - 0.0009;
							left = Double.valueOf(latitude) + 0.0009;
							break;
						case "longitude":
							longitude = fi.getString("UTF-8");
							top = Double.valueOf(longitude) - 0.0009;
							bottom = Double.valueOf(longitude) + 0.0009;
							break;
						case "self":
							self = fi.getString("UTF-8");
							break;
						case "location":
							location = fi.getString("UTF-8");
							break;
						default:
							break;
					}
				}else {
					String realPath = this.getServletContext().getRealPath("/");
					fi.write(new File(realPath+"image\\"+fi.getName()));
					pathList.add("image/"+fi.getName());
					System.out.println("image/"+fi.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int i;
		Note note = noteService.checkNote(userId, left, right, top, bottom);
		if(note != null) {
			i = noteService.updateNote(
					pathList,
					note,
					id, 
					userId, 
					title, content, 
					Double.valueOf(latitude), 
					Double.valueOf(longitude), 
					location,
					time, 
					Integer.valueOf(self)
					);
			if(i > 0) {
				writer.write("1");
				System.out.println("便签替换成功");
			}else {
				writer.write("0");
				System.out.println("便签替换失败");
			}
		}else {
			i = noteService.addNote(
					pathList,
					id, 
					userId, 
					title, content, 
					Double.valueOf(latitude), 
					Double.valueOf(longitude),
					location,
					time, 
					Integer.valueOf(self)
					);
			if(i > 0) {
				writer.write("1");
				System.out.println("便签新增成功");
			}else {
				writer.write("0");
				System.out.println("便签新增失败");
			}
		}
		
	}

}