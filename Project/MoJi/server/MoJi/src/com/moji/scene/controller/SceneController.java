package com.moji.scene.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.moji.entity.Scene;
import com.moji.scene.service.SceneService;

/**   
 * @ClassName: SceneController   
 * @Description:TODO
 * @author: 张璐婷
 * @date: 2020年5月23日 上午11:29:47       
 */
@RestController
@RequestMapping("/scene")
public class SceneController {
	
	@Resource
	private SceneService sceneService;
	
	@RequestMapping(value="/list",method=RequestMethod.GET,produces="application/json;charset=utf-8")
	public String list() {
		List<Scene> list = this.sceneService.findAllScene();
		Gson gson = new Gson();
		String str = gson.toJson(list);
		return str;
	}
	
	@RequestMapping(value="/addLike",method=RequestMethod.GET)
	public String addLike(@RequestParam("sceneId")Integer sceneId) {
		int i = this.sceneService.addLike(sceneId);
		if(i > 0) {
			return "OK";
		}
		return "ERROR";
	}
	
	@RequestMapping(value="/deleteLike",method=RequestMethod.GET)
	public String deleteLike(@RequestParam("sceneId")Integer sceneId) {
		int i = this.sceneService.deleteLike(sceneId);
		if(i > 0) {
			return "OK";
		}
		return "ERROR";
	}

}
