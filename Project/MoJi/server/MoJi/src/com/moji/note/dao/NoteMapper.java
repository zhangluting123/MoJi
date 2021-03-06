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
	 * @Title: changeSelf
	 * @Description: 更改便签的公开性
	 * @param @param noteId
	 * @param @return 参数
	 * @return int 返回类型
	 * @throws
	 */
	public int changeSelf(@Param("noteId")String noteId, @Param("self")int self);
	
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
	public List<Note> queryVisualNote(@Param("userId")String userId);
	
	/**
	 * 
	* @author:      ming
	* @Description: 查询关注note
	* @param        @throws SQLException  
	* @return       List<Note>    
	* @throws
	 */
	public List<Note> queryAttentionNote(@Param("userId")String userId);
	
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
	 * @Title: queryVisualNoteByUserId
	 * @Description: 查询某人公开note
	 * @author: 张璐婷 
	 * @date: 2020年5月16日 下午2:42:58
	 */
	public List<Note> queryVisualNoteByUserId(@Param("userId")String userId);
	
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
	public List<Note> checkNote(
			@Param("userId")String userId, 
			@Param("left")double left, 
			@Param("right")double right, 
			@Param("top")double top, 
			@Param("bottom")double bottom);
	
	
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
	
	/**
	 * @Title: queryNote
	 * @Description: 根据ID值查询note
	 * @author: 张璐婷 
	 * @date: 2020年4月25日 下午6:59:49
	 */
	public Note queryNoteById(String noteId);
	
	/**
	 * @Title: addLikeByNoteId
	 * @Description: 添加点赞数量
	 * @author: 张璐婷 
	 * @date: 2020年5月22日 上午10:48:50
	 */
	public int addLikeByNoteId(@Param("noteId")String noteId);
	
	/**
	 * @Title: deleteLikeByNoteId
	 * @Description: 取消点赞
	 * @author: 张璐婷 
	 * @date: 2020年5月22日 上午10:50:36
	 */
	public int deleteLikeByNoteId(@Param("noteId")String noteId);
	
}
