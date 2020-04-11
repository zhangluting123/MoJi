/**
 * @Title: NoteMapper.java
 * @Package com.moji.note.dao
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月8日
 * @version V1.0
 */
package com.moji.note.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.moji.entity.Img;
import com.moji.entity.Note;

/**
 * @ClassName: NoteMapper
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 春波
 * @date 2020年4月8日
 *
 */
public interface NoteMapper {

	/**
	 * 
	* @author:      张璐婷 
	* @Description: 查询指定便签的userId
	* @param        @throws SQLException  
	* @return       String    
	* @throws
	 */
	public Note queryUserId(@Param("noteId")String noteId);
	
	/**
	 * 
	* @author:      张璐婷 
	* @Description: 查询公开note
	* @param        @throws SQLException  
	* @return       List<Note>    
	* @throws
	 */
	public List<Note> queryVisualNote();
	
	/**
	 * 
	* @author:      张璐婷 
	* @Description: 查询便签
	* @param        @throws SQLException  
	* @return       List<Note>    
	* @throws
	 */
	public List<Note> queryNote(@Param("userId")String userId);
	
	/**
	 * 
	 * @Title: deleteNote
	 * @Description: 删除指定便签
	 * @author 春波
	 * @throws SQLException 
	 * @date 2019年12月14日
	 */
	public int deleteNote(@Param("deleteNoteId")String deleteNoteId);
	
	/**
	 * 
	 * @Title: findByRange
	 * @Description: 从数据库搜索满足范围条件的note
	 * @author 春波
	 * @throws SQLException 
	 * @date 2019年12月5日
	 */
	public List<Note> findByRange(
			@Param("userId")String userId,
			@Param("left")double left, 
			@Param("right")double right, 
			@Param("top")double top, 
			@Param("bottom")double bottom);
	
	/**
	 * 
	 * @Title: addNote
	 * @Description: 新增便签
	 * @author 春波
	 * @date 2019年12月5日
	 */
	public int addNote(
			@Param("noteId")String noteId, 
			@Param("userId")String userId, 
			@Param("title")String title, 
			@Param("content")String content, 
			@Param("latitude")double latitude, 
			@Param("longitude")double longitude, 
			@Param("location")String location, 
			@Param("time")String time, 
			@Param("self")int self);
	
	/**
	 * 
	 * @Title: addNoteImg
	 * @Description: 新增便签的图片
	 * @param @param pathlist
	 * @param @param noteId
	 * @param @return 参数
	 * @return int 返回类型
	 * @throws
	 */
	public int addNoteImg(@Param("imgs")List<Img> imgs);

	/**
	 * 
	 * @Title: checkNote
	 * @Description: 查询自己所在地是否已经存在标记
	 * @author 春波
	 * @date 2019年12月5日
	 */
	public Note checkNote(
			@Param("userId")String userId, 
			@Param("left")double left, 
			@Param("right")double right, 
			@Param("top")double top, 
			@Param("bottom")double bottom);
	
	/**
	 * 
	 * @Title: updateNote
	 * @Description: 更新已有的便签
	 * @author 春波
	 * @date 2019年12月5日
	 */
	public int updateNote(
			@Param("note")Note note, 
			@Param("noteId")String noteId, 
			@Param("userId")String userId, 
			@Param("title")String title, 
			@Param("content")String content, 
			@Param("latitude")double latitude, 
			@Param("longitude")double longitude, 
			@Param("location")String location, 
			@Param("time")String time, 
			@Param("self")int self);
	
	/**
	 * 
	 * @Title: deleteNoteImg
	 * @Description: 按照noteid删除图片
	 * @param @param noteId
	 * @param @return 参数
	 * @return int 返回类型
	 * @throws
	 */
	public int deleteNoteImg(@Param("deleteNoteId")String deleteNoteId);
	
}
