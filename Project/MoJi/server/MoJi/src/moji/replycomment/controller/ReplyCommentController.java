package com.moji.replycomment.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.moji.comment.service.CommentService;
import com.moji.entity.Comment;
import com.moji.entity.JPushCache;
import com.moji.entity.ReplyComment;
import com.moji.entity.User;
import com.moji.jpushcache.service.JpushCacheService;
import com.moji.mailmycomment.service.MailMyCommentService;
import com.moji.replycomment.service.ReplyCommentService;
import com.moji.user.service.UserService;
import com.moji.util.JPushUtil;

import sun.tools.jar.resources.jar;

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
	private MailMyCommentService mailMyCommentService;
	@Resource
	private JpushCacheService jpushCacheService;
	
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
		String msgId = "";
		User user = this.userService.queryUser(replyUserId);//发送消息的人
		Comment comment = this.commentService.findComment(commentId);//comment.getUser()接收消息的人
		if(!user.getUserId().equals(comment.getUser().getUserId())) {
			this.mailMyCommentService.addMailMyCommentService(comment.getUserId(),null,replyId,'R');
			msgId = JPushUtil.sendToBieMing(comment.getUserId(),"叮咚~","新消息",user.getUserName()+"评论你啦,快去看看吧！",null);
			if(msgId.equals("")) {
				addJpushCache(comment,null,user);
			}else {
				test(msgId, comment, null,user);
			}
		}

		if(n > 0) {
			return "OK";
		}
		return"ERROR";
	
	}
	

	
	@RequestMapping(value="/addReplyToReply",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	public String addReplyToReply(@RequestParam(value="replyContent",required=true)String replyContent,
			@RequestParam(value="replyUserId",required=true)String replyUserId,
			@RequestParam(value="commentId",required=true)String commentId,
			@RequestParam(value="parentId",required=true)String parentId) {
		
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddmmss");
		SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
		String replyId = simpleDateFormat.format(date) + replyUserId;
		String replyTime = simpleDateFormat1.format(date);
		int n = replyCommentService.addReplyToReply(replyId, commentId, replyUserId, replyContent, replyTime, parentId);
		String msgId = "";
		User user = this.userService.queryUser(replyUserId);//发送消息的人
		ReplyComment replyComment = this.replyCommentService.findReplyComment(parentId);//replyComment.getReplyUser()接受消息的人
		
		if(!user.getUserId().equals(replyComment.getReplyUser().getUserId())) {
			this.mailMyCommentService.addMailMyCommentService(replyComment.getReplyUser().getUserId(),null,replyId,'R');
			msgId = JPushUtil.sendToBieMing(replyComment.getReplyUser().getUserId(),"叮咚~","新消息",user.getUserName()+"评论你啦,快去看看吧！",null);
			if(msgId.equals("")) {//发送失败暂存数据，登录时重新发送
				addJpushCache(null, replyComment, user);
			}else {
				test(msgId, null,replyComment, user);
			}
		}

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
	
	/**
	 * @Title: addJpushCache
	 * @Description: 添加缓存，登录重新发送
	 * @author: 张璐婷 
	 * @date: 2020年4月26日 下午5:17:19
	 */
	private void addJpushCache(Comment comment,ReplyComment replyComment,User user) {
		JPushCache jPushCache = new JPushCache();
		if(null == replyComment) {
			jPushCache.setReceiveId(comment.getUserId());
		}else {
			jPushCache.setReceiveId(replyComment.getReplyUser().getUserId());
		}
		jPushCache.setSendUserName(user.getUserName());
		jPushCache.setJpushFlag('C');	
		this.jpushCacheService.addJpushCacheMsg(jPushCache);
	}
	/**
	 * @Title: test
	 * @Description: 测试通知是否发送成功
	 * @author: 张璐婷 
	 * @date: 2020年4月26日 下午6:37:51
	 */
	private void test(String msgId,Comment comment,ReplyComment replyComment,User user) {
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000*20);
					int count = JPushUtil.testGetReport(msgId);
					if(count > 0) {
						System.out.println("通知发送成功");
					}else {
						if(null != comment) {
							addJpushCache(comment,null, user);
						}else {
							addJpushCache(null,replyComment, user);
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
