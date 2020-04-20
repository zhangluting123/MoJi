package com.moji.replycomment.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.moji.comment.service.CommentService;
import com.moji.entity.Comment;
import com.moji.entity.Mail;
import com.moji.entity.Note;
import com.moji.entity.ReplyComment;
import com.moji.entity.User;
import com.moji.mail.service.MailService;
import com.moji.note.service.NoteService;
import com.moji.replycomment.service.ReplyCommentService;
import com.moji.user.service.UserService;
import com.moji.util.JPushUtil;
import com.sun.jndi.url.corbaname.corbanameURLContextFactory;
import com.sun.swing.internal.plaf.metal.resources.metal;

/**   
 * @ClassName: ReplyCommentController   
 * @Description:TODO
 * @author: 张璐婷
 * @date: 2020年4月17日 上午9:32:42       
 */
@RestController
@RequestMapping("/replyComment")
public class ReplyCommentController {

	@Resource
	private ReplyCommentService replyCommentService;
	@Resource
	private CommentService commentService; 
	@Resource
	private UserService userService;
	@Resource
	private MailService mailService;
	
	@RequestMapping(value="/add",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	public String addReplyComment(@RequestParam(value="replyContent",required=true)String replyContent,
			@RequestParam(value="replyUserId",required=true)String replyUserId,
			@RequestParam(value="commentId",required=true)String commentId) {
		
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddmmss");
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
		String replyId = simpleDateFormat.format(date) + replyUserId;
		String replyTime = simpleDateFormat1.format(date);
		int n = replyCommentService.addReplyComment(replyId, commentId, replyUserId, replyContent, replyTime);		

		User user = this.userService.queryUser(replyUserId);//发送消息的人
		Comment comment = this.commentService.findComment(commentId);//comment.getUser()接受消息的人
		Map<String, String> extrasparams = new HashMap<>();
		extrasparams.put("acceptTime", replyTime);
		extrasparams.put("myName", comment.getUser().getUserName());
		extrasparams.put("otherName", user.getUserName());
		extrasparams.put("commentContent", replyContent);
		Mail mail = new Mail();
		mail.setAcceptTime(replyTime);
		mail.setCommentContent(replyContent);
		mail.setOtherName(user.getUserName());
		mail.setUserId(comment.getUser().getUserId());
		try {
			this.mailService.insertMail(mail);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		extrasparams.put("mailId", mail.getMailId()+"");
		JPushUtil.sendToBieMing(replyUserId,"叮咚~","新消息",user.getUserName()+"@你啦,快去看看吧！",extrasparams);
		
		if(n > 0) {
			return "OK";
		}
		return"ERROR";
	}

	
	@RequestMapping(value="/list",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	public String list(@RequestParam(value="commentId",required=true)String commentId) {
		List<ReplyComment> replys = replyCommentService.showReplyComment(commentId);
		Gson gson = new Gson();
		String str = gson.toJson(replys);
		return str;
	}
}
