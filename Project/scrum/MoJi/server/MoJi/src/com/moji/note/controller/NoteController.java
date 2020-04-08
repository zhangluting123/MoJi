/**
 * @Title: NoteController.java
 * @Package com.moji.note.controller
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月8日
 * @version V1.0
 */
package com.moji.note.controller;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.moji.entity.Img;
import com.moji.entity.Note;
import com.moji.note.service.NoteService;

/**
 * @ClassName: NoteController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 春波
 * @date 2020年4月8日
 *
 */
@RestController
@RequestMapping("/note")
public class NoteController {

	@Autowired
	private NoteService noteService;
	
	@RequestMapping(value="/add", method=RequestMethod.GET, produces="application/json;charset=utf-8")
	public String addNote(HttpServletRequest request) {
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
			
		String str = "";
		
		List<Img> imgs = new ArrayList<Img>();
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		try {
			List<FileItem> items = upload.parseRequest(request);
			for(FileItem fi : items) {
				int i = 0;
				if(fi.isFormField()) {
					switch (fi.getFieldName()) {
						case "userId":
							userId = fi.getString("UTF-8");//用户id
							id = simpleDateFormat.format(date) + userId;//便签id
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
					String realPath = request.getSession().getServletContext().getRealPath("/");
					fi.write(new File(realPath+"image\\"+fi.getName()));
					Img img = new Img();
					img.setImgId(id+i);
					img.setImgPath("image/"+fi.getName());
					img.setNoteId(id);
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int i;
		Note note = this.noteService.checkNote(userId, left, right, top, bottom);
		if(note != null) {
			i = noteService.updateNote(
					imgs,
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
				str = "1";
				System.out.println("便签替换成功");
			}else {
				str = "0";
				System.out.println("便签替换失败");
			}
		}else {
			i = this.noteService.addNote(
					imgs,
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
				str = "1";
				System.out.println("便签新增成功");
			}else {
				str = "0";
				System.out.println("便签新增失败");
			}
		}
		return str;
	}

	@RequestMapping(value="/delete", method=RequestMethod.GET, produces="application/json;charset=utf-8")
	public String deleteNote(HttpServletRequest request, @RequestParam(value="noteId",required=true)String noteId) {
		String str = "0";
		if (this.noteService.deleteNote(noteId) > 0) {
			str = "1";
			System.out.println("删除便签成功");
		}else {
			System.out.println("删除便签失败");
		}
		return str;
	}
	
	@RequestMapping(value="/download", method=RequestMethod.GET, produces="application/json;charset=utf-8")
	public String downloadNote(HttpServletRequest request, @RequestParam(value="userId",required=true)String userId) {
		String str = "";
		List<Note> list = this.noteService.downloadNote(userId);
		if(list.size() > 0) {
			Gson gson = new Gson();
			str = gson.toJson(list);
		}
		return str;
	}
	
	@RequestMapping(value="/query", method=RequestMethod.GET, produces="application/json;charset=utf-8")
	public String queryVisualNote(HttpServletRequest request) {
		String str = "";
		List<Note> list = this.noteService.queryVisualNote();
		if(list.size() > 0) {
			Gson gson = new Gson();
			str = gson.toJson(list);
		}
		return str;
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET, produces="application/json;charset=utf-8")
	public String showNote(HttpServletRequest request, 
			@RequestParam(value="userId",required=true)String userId,
			@RequestParam(value="left",required=true)Double left,
			@RequestParam(value="top",required=true)Double top,
			@RequestParam(value="right",required=true)Double right,
			@RequestParam(value="bottom",required=true)Double bottom) {
		
		String str = "";
		List<Note> list = new ArrayList<>();
		try {
			list = this.noteService.findByRange(userId, left, right, top, bottom);
			if(list.size() > 0) {
				Gson gson = new Gson();
				str = gson.toJson(list);
				System.out.println("成功查询到数据");
			}else {
				System.out.println("没有查询到对应数据");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return str;
	}
	
}
