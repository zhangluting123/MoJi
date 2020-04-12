/**
 * @Title: UserController.java
 * @Package com.moji.user.controller
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月8日
 * @version V1.0
 */
package com.moji.user.controller;

import java.io.File;
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
import com.moji.entity.User;
import com.moji.user.service.UserService;

/**
 * @ClassName: UserController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 春波
 * @date 2020年4月8日
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/login", method=RequestMethod.GET, produces="application/json;charset=utf-8")
	public String login(HttpServletRequest request, 
			@RequestParam(value="phone",required=true)String phone,
			@RequestParam(value="password",required=true)String pwd) {
		String str = "";
		User user = this.userService.loginUser(phone, pwd);
		if(user == null) {
			str = "0";
			System.out.println("手机号或密码不正确");
		}else {
			Gson gson = new Gson();
			str = gson.toJson(user);
			System.out.println(str);
			System.out.println("登录成功");
		}
		return str;
	}
	
	@RequestMapping(value="/changePwd", method=RequestMethod.GET, produces="application/json;charset=utf-8")
	public String changePwd(HttpServletRequest request, 
			@RequestParam(value="userId",required=true)String userId,
			@RequestParam(value="newPwd",required=true)String newPwd) {
		String str = "";
		int b = this.userService.changeUserPwd(userId, newPwd);
		if(b > 0) {
			str = "OK";
			System.out.println("密码修改成功!");
		}
		return str;
	}

	@RequestMapping(value="/changeMsg", method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public String changeMsg(HttpServletRequest request) {
		String str = "";
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
					String realPath = request.getSession().getServletContext().getRealPath("/");
					fi.write(new File(realPath+"avatar\\"+fi.getName()));
					user.setUserHeadImg("avatar/"+fi.getName());
					is = true;
				}
			}
			System.out.println(user.toString());
			int b = 0;
			if(is) {
				b = this.userService.updateUserMsg(user);
			}else {
				b = this.userService.updateUserWithoutAvatar(user);
			}
			if(b > 0) {
				str = "OK";
				System.out.println("修改个人信息成功");
			}else {
				str = "ERROR";
				System.out.println("修改个人信息失败");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return str;
	}
	
	@RequestMapping(value="/regist", method=RequestMethod.GET, produces="application/json;charset=utf-8")
	public String rigest(HttpServletRequest request, 
			@RequestParam(value="phone",required=true)String phone,
			@RequestParam(value="password",required=true)String password) {
		
		String str = "";
		String string = String.valueOf((int)((Math.random()*9+1)*100))+"_"+phone.substring(7);
		
		User newUser = new User();
		newUser.setUserId(string);
		newUser.setPhone(phone);
		newUser.setPassword(password);
		newUser.setUserName(string);
		
		User ifRegist = this.userService.queryPhone(phone);
		if(!(null == ifRegist)) {
			str = "Regist";
		}else {
			int b = this.userService.addUsers(newUser);
			if(b > 0) {
				str = "OK";
				System.out.println("注册成功");
			}
		}
		return str;
	}
}