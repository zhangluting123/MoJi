package com.moji.replycomment.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.moji.entity.ReplyComment;
import com.moji.replycomment.dao.ReplyCommentMapper;


/**   
 * @ClassName: ReplyCommentService   
 * @Description:TODO
 * @author: 张璐婷
 * @date: 2020年4月17日 上午9:34:01       
 */
@Service
public class ReplyCommentService {

	@Resource
	private ReplyCommentMapper replyCommentMapper;
	/**
	 * @Title: addReplyComment
	 * @Description: 添加回复的评论
	 * @author: 张璐婷 
	 * @date: 2020年4月17日 上午10:09:48
	 */
	public int addReplyComment(String replyId,String commentId,String replyUserId,String replyContent,String replyTime) {
		return replyCommentMapper.insertReplyComment(replyId, commentId, replyUserId, replyContent, replyTime);
	}
	
	/**
	 * @Title: countOfReply
	 * @Description: 查询数量
	 * @author: 张璐婷 
	 * @date: 2020年4月17日 上午10:10:06
	 */
	public int countOfReply(String commentId) {
		return replyCommentMapper.queryCountOfReplyByCommentId(commentId);
	}
	
	/**
	 * @Title: showReplyComment
	 * @Description: 查询回复的评论
	 * @author: 张璐婷 
	 * @date: 2020年4月17日 上午10:16:43
	 */
	public List<ReplyComment> showReplyComment(String commentId){
		return replyCommentMapper.queryReplyCommentByCommentId(commentId);
	}
	
	/**
	 * @Title: addReplyToReply
	 * @Description: 回复的回复
	 * @author: 张璐婷 
	 * @date: 2020年4月24日 下午8:46:41
	 */
	public int addReplyToReply(String replyId,String commentId,String replyUserId,String replyContent,String replyTime,String parentId) {
		return replyCommentMapper.insertReplyToReply(replyId, commentId, replyUserId, replyContent, replyTime, parentId);
	}
	
	public ReplyComment findReplyComment(String replyId) {
		return this.replyCommentMapper.queryReplyCommentById2(replyId);
	}
	
}
