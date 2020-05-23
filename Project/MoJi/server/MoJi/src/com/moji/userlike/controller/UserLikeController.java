package com.moji.userlike.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.moji.entity.Note;
import com.moji.entity.ReturnNote;
import com.moji.entity.UserLike;
import com.moji.note.service.NoteService;
import com.moji.userlike.service.UserLikeService;
import com.moji.video.service.VideoService;

/**   
 * @ClassName: UserLikeController   
 * @Description:TODO
 * @author: 张璐婷
 * @date: 2020年5月22日 下午12:22:24       
 */
@RestController
@RequestMapping("/userLike")
public class UserLikeController {
	@Resource
	private NoteService noteService;
	
	@Resource
	private VideoService videoService;
	
	@Resource
	private UserLikeService userLikeService;
	
	@RequestMapping(value="/addLike",method=RequestMethod.GET)
	public String addLike(@RequestParam(value="userId",required=true)String userId,
			@RequestParam(value="noteId",required=false)String noteId,
			@RequestParam(value="videoId",required=false)String videoId) {
		int i = 0,j = 0;
		String id = String.valueOf((int)((Math.random()*9+1)*100000))+"_"+userId.substring(3);
		if(null != noteId) {
			i = this.noteService.addLikeByNoteId(noteId);
			j = this.userLikeService.insertUserLike(id,userId, noteId, null);
		}else if(null != videoId) {
			i = this.videoService.addLikeByVideoId(videoId);
			j = this.userLikeService.insertUserLike(id,userId, null, videoId);
		}
		if(i > 0 && j > 0) {
			return id;
		}
		return "ERROR";
	}
	
	@RequestMapping(value="/deleteLike",method=RequestMethod.GET)
	public String deleteLike(@RequestParam(value="likeId",required=true)String likeId,
			@RequestParam(value="noteId",required=false)String noteId,
			@RequestParam(value="videoId",required=false)String videoId) {
		int i = 0,j = 0;
		if(null != noteId) {
			i = this.noteService.deleteLikeByNoteId(noteId);
		}else if(null != videoId) {
			i = this.videoService.deleteLikeByVideoId(videoId);
		}
		j = this.userLikeService.deleteUserLike(likeId);
		if(i > 0 && j > 0) {
			return "OK";
		}
		return "ERROR";
	}
	
	@RequestMapping(value="/list",method=RequestMethod.GET, produces="application/json;charset=utf-8")
	public String listLike(@RequestParam(value="userId",required=true)String userId) {
		List<UserLike> userLikes = this.userLikeService.queryUserLike(userId);
		//转化成需要返回的类型
		for (int i = 0; i < userLikes.size(); i++) {
			ReturnNote returnNote = userLikes.get(i).getNoteLike();
			Note note = userLikes.get(i).getDbNote();
			if(null != note) {
				returnNote.setNoteId(note.getNoteId());
				returnNote.setLatitude(note.getLatitude());
				returnNote.setLongitude(note.getLongitude());
				returnNote.setTitle(note.getTitle());
				returnNote.setContent(note.getContent());
				returnNote.setLocation(note.getLocation());
				returnNote.setTime(note.getTime());
				returnNote.setUserId(note.getUserId());
				returnNote.setUser(note.getUser());
				returnNote.setSelf(note.getSelf());
				returnNote.setLike(note.getLike());
				for(int j = 0; j < note.getImgList().size(); j++) {
					returnNote.getImgList().add(note.getImgList().get(j).getImgPath());
				}
				userLikes.get(i).setDbNote(null);
			}
			
		}
		String str = null;
		if(null != userLikes) {
			Gson gson = new Gson();
			str = gson.toJson(userLikes);
		}
		return str;
	}
}
