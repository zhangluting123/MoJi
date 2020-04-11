/**
 * @Title: CommentService.java
 * @Package com.moji.comment.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月8日
 * @version V1.0
 */
package com.moji.comment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moji.comment.dao.CommentMapper;
import com.moji.entity.Comment;

/**
 * @ClassName: CommentService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 春波
 * @date 2020年4月8日
 *
 */
@Service
public class CommentService {

	@Autowired
	CommentMapper commentMapper;
	
	/**
	 * 
	 * @Title: showComment
	 * @Description: 展示评论
	 * @return
	 */
	public List<Comment> showComment(String noteId){
		return commentMapper.showComment(noteId);
	}
	
	/**
	 * 
	 * @Title: addComment
	 * @Description: 新增评论
	 * @return
	 */
	public int addComment(String commentId, String noteId, String userId, String content, String time) {
		return commentMapper.addComment(commentId, noteId, userId, content, time);
	}
}
