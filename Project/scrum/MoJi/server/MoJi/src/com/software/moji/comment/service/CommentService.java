package com.software.moji.comment.service;

/**
 * @Title: CommentService.java
 * @Package com.moji.comment.service
 * @Description: TODO(��һ�仰�������ļ���ʲô)
 * @author ����
 * @date 2019��12��3��
 * @version V1.0
 */

import java.util.List;

import com.software.moji.comment.dao.CommentDao;
import com.software.moji.entity.Comment;

/**
 * @ClassName: CommentService
 * @Description: TODO(������һ�仰��������������)
 * @author ����
 * @date 2019��12��3��
 */
public class CommentService {

	/**
	 * 
	 * @Title: showComment
	 * @Description: չʾ����
	 * @return
	 */
	public List<Comment> showComment(String noteId){
		return new CommentDao().showComment(noteId);
	}
	
	/**
	 * 
	 * @Title: addComment
	 * @Description: ��������
	 * @return
	 */
	public int addComment(String commentId, String noteId, String userId, String content, String time) {
		return new CommentDao().addComment(commentId, noteId, userId, content, time);
	}
	
}

