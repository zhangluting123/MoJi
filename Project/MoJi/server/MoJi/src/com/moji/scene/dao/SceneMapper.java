package com.moji.scene.dao;

import java.util.List;

import com.moji.entity.Scene;

/**   
 * @ClassName: SceneMapper   
 * @Description:TODO
 * @author: 张璐婷
 * @date: 2020年5月23日 上午11:30:33       
 */
public interface SceneMapper {
	
	/**
	 * @Title: findAllScene
	 * @Description: 查询所有景点
	 * @author: 张璐婷 
	 * @date: 2020年5月23日 上午11:36:21
	 */
	public List<Scene> findAllScene();
	
	/**
	 * @Title: addLike
	 * @Description: 添加点赞
	 * @author: 张璐婷 
	 * @date: 2020年5月23日 上午11:37:45
	 */
	public int addLike(Integer id);
	
	/**
	 * @Title: deleteLike
	 * @Description: 取消点赞
	 * @author: 张璐婷 
	 * @date: 2020年5月23日 上午11:38:11
	 */
	public int deleteLike(Integer id);

}
