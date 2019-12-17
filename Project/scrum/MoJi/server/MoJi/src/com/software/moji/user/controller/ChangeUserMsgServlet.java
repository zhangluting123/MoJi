package com.software.moji.user.controller;

import java.io.File;
import java.io.IOException;
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

import com.software.moji.entity.User;
import com.software.moji.user.service.UserService;
/**
 * @Title:       MoJi
 * @Package      com.software.moji.user.controller
 * @Description: 更新用户信息
 * @author       张璐婷
 * @date         2019/12/8
 * @version V1.0
 */
@WebServlet("/ChangeUserMsgServlet")
public class ChangeUserMsgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */

    public ChangeUserMsgServlet() {
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
		
		User user = new User();
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		boolean is = false;//判断是否更改头像
		try {
			List<FileItem> items = upload.parseRequest(request);
			for(FileItem fi : items) {
				if(fi.isFormField()) {
					String string = fi.getString("utf-8");
					switch (fi.getFieldName()) {
						case "userId":
							user.setUserId(string);
							break;
						case "userName":
							user.setUserName(string);
							break;
						case "occupation":
							user.setOccupation(string);
							break;
						case "signature":
							user.setSignature(string);
							break;
						case "sex":
							user.setSex(string);
						default:
							break;
					}
				}else {
					String realPath = this.getServletContext().getRealPath("/");
					fi.write(new File(realPath+"avatar\\"+fi.getName()));
					user.setUserHeadImg("avatar/"+fi.getName());
					is = true;
				}
			}
			System.out.println(user.toString());
			boolean b = false;
			if(is) {
				b = new UserService().updateUserMsg(user);
			}else {
				b = new UserService().updateUserWithoutAvatar(user);
			}
			if(b) {
				response.getWriter().write("OK");
				System.out.println("修改个人信息成功");
			}else {
				response.getWriter().write("ERROR");
				System.out.println("修改个人信息失败");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
