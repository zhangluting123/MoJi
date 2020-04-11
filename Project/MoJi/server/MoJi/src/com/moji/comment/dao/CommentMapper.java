/**
 * @Title: CommentMapper.java
 * @Package com.moji.comment.dao
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月8日
 * @version V1.0
 */
package com.moji.comment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.moji.entity.Comment;

/**
 * @ClassName: CommentMapper
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 春波
 * @date 2020年4月8日
 *
 */
public interface CommentMapper {
	/**
	 * @ClassName: CommentDao
	 * @Description: 对数据库comment_detail表进行操作
	 * @author 春波
	 * @date 2019年12月3日
	 */
	public List<Comment> showComment(@Param("noteId")String noteId);
	
	/**
	 * 
	 * @Title: addComment
	 * @Description: 新增评论
	 * @author 春波
	 * @date 2019年12月5日
	 */
	public int addComment(@Param("commentId")String commentId, 
			@Param("noteId")String noteId, 
			@Param("userId")String userId, 
			@Param("content")String content, 
			@Param("time")String time);
}
