/**
 * @Title: MailController.java
 * @Package com.moji.mail.controller
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月8日
 * @version V1.0
 */
package com.moji.mail.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.moji.entity.Mail;
import com.moji.mail.service.MailService;

/**
 * @ClassName: MailController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 春波
 * @date 2020年4月8日
 *
 */
@RestController
@RequestMapping("/mail")
public class MailController {

	@Autowired
	private MailService mailService;
	
	@RequestMapping(value="/delete", method=RequestMethod.GET, produces="application/json;charset=utf-8")
	public String deleteMailById(HttpServletRequest request, @RequestParam(value="mailId",required=true)int mailId) {
		String str = "ERROR";
		try {
			if(this.mailService.deleteMail(mailId) > 0) {
				str = "OK";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return str;
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET, produces="application/json;charset=utf-8")
	public String queryMailByUserId(HttpServletRequest request, @RequestParam(value="userId",required=true)String userId) {
		String str="";
		try {
			List<Mail> list =  this.mailService.quaryMail(userId);
			if(list!=null && list.size()>0) {
				Gson gson = new Gson();
				str = gson.toJson(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return str;
	}
}
