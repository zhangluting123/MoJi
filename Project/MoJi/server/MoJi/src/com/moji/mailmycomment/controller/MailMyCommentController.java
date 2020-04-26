package com.moji.mailmycomment.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.moji.entity.Comment;
import com.moji.entity.MailMyComment;
import com.moji.entity.Note;
import com.moji.entity.ReturnNote;
import com.moji.mailmycomment.service.MailMyCommentService;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;

/**   
 * @ClassName: MailMyCommentController   
 * @Description:TODO
 * @author: 张璐婷
 * @date: 2020年4月24日 上午9:20:29       
 */
@RestController
@RequestMapping("/mailmycomment")
public class MailMyCommentController {

	@Resource
	private MailMyCommentService mailMyCommentService;
	
	
	@RequestMapping(value="/list",method=RequestMethod.GET, produces="application/json;charset=utf-8")
	public String list(@RequestParam("userId")String userId) {
		List<MailMyComment> list = this.mailMyCommentService.findMailMyComment(userId);
		//转换Note
		Note note = null;
		ReturnNote returnNote = null;
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getCrFlag() == 'C') {
				note = list.get(i).getComment().getDbNote();
				returnNote = list.get(i).getComment().getNote();
				
			}else if(list.get(i).getCrFlag() == 'R'){
				note = list.get(i).getReplyComment().getComment().getDbNote();
				returnNote = list.get(i).getReplyComment().getComment().getNote();
			}
			returnNote.setNoteId(note.getNoteId());
			returnNote.setLatitude(note.getLatitude());
			returnNote.setLongitude(note.getLongitude());
			returnNote.setTitle(note.getTitle());
			returnNote.setContent(note.getContent());
			returnNote.setLocation(note.getLocation());
			returnNote.setTime(note.getTime());
			returnNote.setUserId(note.getUserId());
			returnNote.setUser(note.getUser());
			returnNote.getImgList().clear();
			for(int j = 0; j < note.getImgList().size(); j++) {
				returnNote.getImgList().add(note.getImgList().get(j).getImgPath());
			}
		}
		
		Gson gson = new Gson();
		String str = gson.toJson(list);
		System.out.println(str);
		return str;
	}
	
	@RequestMapping(value="/update",method=RequestMethod.GET, produces="application/json;charset=utf-8")
	public String update(@RequestParam("myCommentId")Integer myCommentId) {
		int n = this.mailMyCommentService.updateReadMsg(myCommentId);
		if(n > 0) {
			return "OK";
		}
		return "ERROR";
	}
	
	@RequestMapping(value="/delete",method=RequestMethod.GET, produces="application/json;charset=utf-8")
	public String delete(@RequestParam("myCommentId")Integer myCommentId) {
		int n = this.mailMyCommentService.removeMailMyComment(myCommentId);
		if(n > 0) {
			return "OK";
		}
		return "ERROR";
	}
}
