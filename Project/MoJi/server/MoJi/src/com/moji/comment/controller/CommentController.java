/**
 * @Title: CommentController.java
 * @Package com.moji.comment.controller
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月8日
 * @version V1.0
 */
package com.moji.comment.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.moji.comment.service.CommentService;
import com.moji.entity.Comment;
import com.moji.entity.Mail;
import com.moji.entity.Note;
import com.moji.entity.User;
import com.moji.mail.service.MailService;
import com.moji.note.service.NoteService;
import com.moji.replycomment.service.ReplyCommentService;
import com.moji.user.service.UserService;
import com.moji.util.JPushUtil;

/**
 * @ClassName: CommentController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 春波
 * @date 2020年4月8日
 *
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

	@Autowired
	private CommentService commentService;
	@Autowired
	private ReplyCommentService replyCommentService;
	@Autowired
	private NoteService noteService;
	@Autowired
	private UserService userService;
	@Autowired
	private MailService mailService;
	
	@RequestMapping(value="/list", method=RequestMethod.GET, produces="application/json;charset=utf-8")
	public String showCommentByNoteId(HttpServletRequest request, @RequestParam(value="noteId",required=true)String noteId) {
		List<Comment> list = this.commentService.showComment(noteId);
		//添加评论数量
		for(int i = 0; i < list.size();i++) {
			list.get(i).setReplyCount(this.replyCommentService.countOfReply(list.get(i).getId()));
		}
		String str = "";
		if(list.size() > 0) {
			Gson gson = new Gson();
			str = gson.toJson(list);
			System.out.println("共有" + list.size() + "条评论");
		}else {
			System.out.println("共有0条评论");
		}
		return str;
	}

	@RequestMapping(value="/add", method=RequestMethod.GET, produces="application/json;charset=utf-8")
	public String addComment(HttpServletRequest request, 
			@RequestParam(value="noteId",required=true)String noteId,
			@RequestParam(value="userId",required=true)String userId,
			@RequestParam(value="commentContent",required=true)String commentContent) {
		Date date = new Date();
		//将日期格式化
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddmmss");
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
		//转换成字符串格式
		String commentId = simpleDateFormat.format(date) + noteId;
		String commentTime = simpleDateFormat1.format(date);
		
		int i = commentService.addComment(commentId, noteId, userId, commentContent, commentTime);
		String re = "0";
		if(i > 0) {
			re = "1";
			System.out.println("评论增加成功");
			Note note = this.noteService.queryUserId(noteId);//收到评论的人
			User user = this.userService.queryUser(userId);//发布评论的人
			Map<String, String> extrasparams = new HashMap<>();
			extrasparams.put("acceptTime", commentTime);
			extrasparams.put("myName", note.getUser().getUserName());
			extrasparams.put("otherName", user.getUserName());
			extrasparams.put("commentContent", commentContent);
			System.out.println("userId="+userId+";notegetuserid="+note.getUser().getUserId());
			
			Mail mail = new Mail();
			mail.setAcceptTime(commentTime);
			mail.setCommentContent(commentContent);
			mail.setOtherName(user.getUserName());
			mail.setUserId(note.getUser().getUserId());
			try {
				int b = this.mailService.insertMail(mail);
				if(b > 0) {
					System.out.println("通知消息写入成功！");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			extrasparams.put("mailId", mail.getMailId()+"");
			int j = JPushUtil.sendToBieMing(note.getUser().getUserId(),"叮咚~","新消息",user.getUserName()+"评论你啦,快去看看吧！",extrasparams);
			if(j == 1) {
				re = "2";
				System.out.println("[评论已发送通知]");
			}
		}
		
		return re+","+commentId;
	}
	
	
	
}
