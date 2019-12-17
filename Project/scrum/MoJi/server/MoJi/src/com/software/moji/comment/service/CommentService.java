package com.software.moji.comment.service;

/**
 * @Title: CommentService.java
 * @Package com.moji.comment.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2019年12月3日
 * @version V1.0
 */

import java.util.List;

import com.software.moji.comment.dao.CommentDao;
import com.software.moji.entity.Comment;

/**
 * @ClassName: CommentService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 春波
 * @date 2019年12月3日
 */
public class CommentService {

	/**
	 * 
	 * @Title: showComment
	 * @Description: 展示评论
	 * @return
	 */
	public List<Comment> showComment(String noteId){
		return new CommentDao().showComment(noteId);
	}
	
	/**
	 * 
	 * @Title: addComment
	 * @Description: 新增评论
	 * @return
	 */
	public int addComment(String commentId, String noteId, String userId, String content, String time) {
		return new CommentDao().addComment(commentId, noteId, userId, content, time);
	}
	
}

