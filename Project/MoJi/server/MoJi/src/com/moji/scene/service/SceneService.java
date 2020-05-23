package com.moji.scene.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.moji.entity.Scene;
import com.moji.scene.dao.SceneMapper;

/**   
 * @ClassName: SceneService   
 * @Description:TODO
 * @author: 张璐婷
 * @date: 2020年5月23日 上午11:31:04       
 */
@Service
public class SceneService {
	
	@Resource
	private SceneMapper sceneMapper;
	
	/**
	 * @Title: findAllScene
	 * @Description: 查询所有景点
	 * @author: 张璐婷 
	 * @date: 2020年5月23日 上午11:44:12
	 */
	public List<Scene> findAllScene(){
		return this.sceneMapper.findAllScene();
	}
	
	/**
	 * @Title: addLike
	 * @Description: 添加点赞
	 * @author: 张璐婷 
	 * @date: 2020年5月23日 上午11:37:45
	 */
	public int addLike(Integer id) {
		return this.sceneMapper.addLike(id);
	}
	
	/**
	 * @Title: deleteLike
	 * @Description: 取消点赞
	 * @author: 张璐婷 
	 * @date: 2020年5月23日 上午11:38:11
	 */
	public int deleteLike(Integer id) {
		return this.sceneMapper.deleteLike(id);
	}
	
}
