/**
 * @Title: NoteService.java
 * @Package com.moji.note.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月8日
 * @version V1.0
 */
package com.moji.note.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moji.entity.Img;
import com.moji.entity.Note;
import com.moji.note.dao.NoteMapper;

/**
 * @ClassName: NoteService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 春波
 * @date 2020年4月8日
 *
 */
@Service
public class NoteService {

	@Autowired
	private NoteMapper noteMapper;
	
	/**
	 * 
	 * @Title: changeSelf
	 * @Description: 改变便签的公开性
	 * @param @param noteId
	 * @param @return 参数
	 * @return int 返回类型
	 * @throws
	 */
	public int changeSelf(String noteId, int self) {
		return this.noteMapper.changeSelf(noteId,self);
	}
	
	/**
	 * 
	* @author:      张璐婷 
	* @Description: 查询指定便签的userId
	* @param        @throws SQLException  
	* @return       String    
	* @throws
	 */
	public Note queryUserId(String noteId) {
		return this.noteMapper.queryUserId(noteId);
	}
	
	/**
	 * 
	* @author:      张璐婷 
	* @Description: 查询公开note
	* @param        @throws SQLException  
	* @return       List<Note>    
	* @throws
	 */
	public List<Note> queryVisualNote(String userId){
		return this.noteMapper.queryVisualNote(userId);
	}
	
	/**
	 * 
	* @author:      ming
	* @Description: 查询关注note
	* @param        @throws SQLException  
	* @return       List<Note>    
	* @throws
	 */
	public List<Note> queryAttentionNote(String userId){
		return this.noteMapper.queryAttentionNote(userId);
	}
	
	/**
	 * 
	 * @Title: DownloadNote
	 * @Description: 查询便签
	 * @param @param userId
	 * @param @return
	 * @param @throws SQLException 参数
	 * @return List<Note> 返回类型
	 * @throws
	 */
	public List<Note> downloadNote(String userId){
		return this.noteMapper.queryNote(userId);
	} 
	
	/**
	 * @Title: queryVisualNoteByUserId
	 * @Description: 查询某人公开note
	 * @author: 张璐婷 
	 * @date: 2020年5月16日 下午2:45:25
	 */
	public List<Note> queryVisualNoteByUserId(String userId){
		return this.noteMapper.queryVisualNoteByUserId(userId);
	}
	/**
	 * 
	* @author:      张璐婷 
	* @Description: 查询便签
	* @param        @throws SQLException  
	* @return       List<Note>    
	* @throws
	 */
	public List<Note> queryNote(String userId){
		return this.noteMapper.queryNote(userId);
	}
	
	/**
	 * 
	 * @Title: deleteNote
	 * @Description: 删除指定便签
	 * @author 春波
	 * @throws SQLException 
	 * @date 2019年12月14日
	 */
	public int deleteNote(String noteId) {
		int i1 = this.noteMapper.deleteNote(noteId);
		int i2 = this.noteMapper.deleteNoteImg(noteId);
		int i3 = i1 + i2;
		return i3;
	}
	
	/**
	 * 
	 * @Title: findByRange
	 * @Description: 从数据库搜索满足范围条件的note
	 * @author 春波
	 * @throws SQLException 
	 * @date 2019年12月5日
	 */
	public List<Note> findByRange(String userId, double left, double right, double top, double bottom){
		return this.noteMapper.findByRange(userId, left, right, top, bottom);
	}
	
	/**
	 * 
	 * @Title: addNote
	 * @Description: 新增便签
	 * @author 春波
	 * @date 2019年12月5日
	 */
	public int addNote(List<Img> imgs, String id, String userId, String title, String content, double latitude, double longitude, String location, String time, int self) {
		int i1 = this.noteMapper.addNote(id, userId, title, content, latitude, longitude, location, time, self);
		int i2 = this.noteMapper.addNoteImg(imgs);
		int i3 = i1 + i2;
		return i3;
	}

	/**
	 * 
	 * @Title: checkNote
	 * @Description: 查询自己所在地是否已经存在标记
	 * @author 春波
	 * @date 2019年12月5日
	 */
	public List<Note> checkNote(String userId, double left, double right, double top, double bottom) {
		return this.noteMapper.checkNote(userId, left, right, top, bottom);
	}
	
	/**
	 * @Title: addLikeByNoteId
	 * @Description: 添加点赞数量
	 * @author: 张璐婷 
	 * @date: 2020年5月22日 上午10:48:50
	 */
	public int addLikeByNoteId(String noteId) {
		return this.noteMapper.addLikeByNoteId(noteId);
	}
	
	/**
	 * @Title: deleteLikeByNoteId
	 * @Description: 取消点赞
	 * @author: 张璐婷 
	 * @date: 2020年5月22日 上午10:50:36
	 */
	public int deleteLikeByNoteId(@Param("noteId")String noteId) {
		return this.noteMapper.deleteLikeByNoteId(noteId);
	}
	

}
