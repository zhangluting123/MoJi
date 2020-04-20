package com.moji.replycomment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.moji.entity.ReplyComment;

/**   
 * @ClassName: ReplyCommentMapper   
 * @Description:TODO
 * @author: 张璐婷
 * @date: 2020年4月17日 上午9:33:14       
 */
public interface ReplyCommentMapper {
	
	/**
	 * @Title: insertReplyComment
	 * @Description: 插入一条回复的评论
	 * @author: 张璐婷 
	 * @date: 2020年4月17日 上午9:42:44
	 */
	public int insertReplyComment(@Param("replyId") String replyId,
			@Param("commentId") String commentId,
			@Param("replyUserId") String replyUserId,
			@Param("replyContent") String replyContent,
			@Param("replyTime") String replyTime);
	
	/**
	 * @Title: queryCountOfReplyByCommentId
	 * @Description: 查询回复评论的数量
	 * @author: 张璐婷 
	 * @date: 2020年4月17日 上午9:43:54
	 */
	public int queryCountOfReplyByCommentId(String commentId);
	
	
	/**
	 * @Title: queryReplyCommentByCommentId
	 * @Description: 根据评论ID查询所有回复评论
	 * @author: 张璐婷 
	 * @date: 2020年4月17日 上午9:37:49
	 */
	public List<ReplyComment> queryReplyCommentByCommentId(String commentId);
}
