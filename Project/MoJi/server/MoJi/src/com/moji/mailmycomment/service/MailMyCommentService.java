package com.moji.mailmycomment.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.moji.entity.MailMyComment;
import com.moji.mailmycomment.dao.MailMyCommentMapper;

/**   
 * @ClassName: MailMyCommentService   
 * @Description:TODO
 * @author: 张璐婷
 * @date: 2020年4月24日 上午9:21:40       
 */
@Service
public class MailMyCommentService {

	@Resource
	private MailMyCommentMapper mailMyCommentMapper;
	
	/**
	 * @Title: addMailMyCommentService
	 * @Description: 添加评论消息
	 * @author: 张璐婷 
	 * @date: 2020年4月24日 上午9:39:34
	 */
	public int addMailMyCommentService(String userId,String commentId,String replyId,Character crflag) {
		return this.mailMyCommentMapper.insertMailMyComment(userId, commentId, replyId, crflag);
	}
	
	/**
	 * @Title: findMailMyComment
	 * @Description: 查询所有通知消息
	 * @author: 张璐婷 
	 * @date: 2020年4月24日 下午4:12:21
	 */
	public List<MailMyComment> findMailMyComment(String userId){
		return this.mailMyCommentMapper.queryMailMyComment(userId);
	}
	
	/**
	 * @Title: updateReadMsg
	 * @Description: 更新阅读信息
	 * @author: 张璐婷 
	 * @date: 2020年4月25日 下午12:49:34
	 */
	public int updateReadMsg(Integer myCommentId) {
		return this.mailMyCommentMapper.updateReadMsg(myCommentId);
	}
	
	/**
	 * @Title: removeMailMyComment
	 * @Description: 删除消息
	 * @author: 张璐婷 
	 * @date: 2020年4月25日 下午2:53:16
	 */
	public int removeMailMyComment(Integer myCommentId) {
		return this.mailMyCommentMapper.deleteMailMyComment(myCommentId);
	}
}
