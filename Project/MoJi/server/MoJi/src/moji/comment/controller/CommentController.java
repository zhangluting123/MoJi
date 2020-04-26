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
import com.moji.entity.JPushCache;
import com.moji.entity.Mail;
import com.moji.entity.MailMyComment;
import com.moji.entity.Note;
import com.moji.entity.User;
import com.moji.jpushcache.service.JpushCacheService;
import com.moji.mail.dao.MailMapper;
import com.moji.mail.service.MailService;
import com.moji.mailmycomment.service.MailMyCommentService;
import com.moji.note.service.NoteService;
import com.moji.replycomment.service.ReplyCommentService;
import com.moji.user.service.UserService;
import com.moji.util.JPushUtil;

import sun.tools.jar.resources.jar;

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
	private MailMyCommentService mailMyCommentService;
	@Autowired
	private JpushCacheService jpushCacheService;
	
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
			@RequestParam(value="commentContent",required=true)String commentContent) throws SQLException {
		Date date = new Date();
		//将日期格式化
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddmmss");
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
		//转换成字符串格式
		String commentId = simpleDateFormat.format(date) + noteId;
		String commentTime = simpleDateFormat1.format(date);
		
		int i = commentService.addComment(commentId, noteId, userId, commentContent, commentTime);
		String re = "0";
		String msgId = "";
		Note note = null;
		User user = null;
		if(i > 0) {
			re = "1";
			System.out.println("评论增加成功");
			note = this.noteService.queryUserId(noteId);//收到评论的人
			user = this.userService.queryUser(userId);//发布评论的人
			
			if(!note.getUserId().equals(user.getUserId())) {
				int b = this.mailMyCommentService.addMailMyCommentService(note.getUserId(), commentId, null, 'C');
				if(b > 0) {
					System.out.println("通知消息写入成功！");
				}
	
				msgId = JPushUtil.sendToBieMing(note.getUserId(),"叮咚~","新消息",user.getUserName()+"评论你发布的动态啦,快去看看吧！",null);
				if(msgId.equals("")) {
					addJpushCache(note, user);
				}else {
					test(msgId, note, user);
				}
					
			}
		}
		return re+","+commentId;
	
	}
	/**
	 * @Title: test
	 * @Description: 测试通知是否发送成功
	 * @author: 张璐婷 
	 * @date: 2020年4月26日 下午6:38:11
	 */
	private void test(String msgId,Note note,User user) {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(1000*60);
					int count = JPushUtil.testGetReport(msgId);
					if(count > 0) {
						System.out.println("通知发送成功");
					}else {
						addJpushCache(note, user);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	/**
	 * @Title: addJpushCache
	 * @Description: 添加缓存，登录重新发送
	 * @author: 张璐婷 
	 * @date: 2020年4月26日 下午5:17:19
	 */
	private void addJpushCache(Note note,User user) {
		JPushCache jPushCache = new JPushCache();
		jPushCache.setReceiveId(note.getUserId());
		jPushCache.setSendUserName(user.getUserName());
		jPushCache.setJpushFlag('D');
		this.jpushCacheService.addJpushCacheMsg(jPushCache);
	}
	

}
