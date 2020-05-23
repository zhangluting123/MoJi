/**
 * @Title: VideoMapper.java
 * @Package com.moji.video.dao
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月17日
 * @version V1.0
 */
package com.moji.video.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.moji.entity.Video;

/**
 * @ClassName: VideoMapper
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 春波
 * @date 2020年4月17日
 *
 */
public interface VideoMapper {
	/**
	 * 
	 * @Title: addVideo
	 * @Description: 插入视频信息
	 * @param @param video
	 * @param @param userId
	 * @param @return 参数
	 * @return int 返回类型
	 * @throws
	 */
	public int addVideo(@Param("video")Video video, @Param("userId")String userId);
	
	/**
	 * 
	 * @Title: checkVideoId
	 * @Description: 查询是否存在此id
	 * @param @param video
	 * @param @return 参数
	 * @return int 返回类型
	 * @throws
	 */
	public int checkVideoId(@Param("videoId")String videoId);
	
	/**
	 * @Title: findVideo
	 * @Description: 查询某用户所有视频
	 * @author: 张璐婷 
	 * @date: 2020年5月15日 下午5:35:33
	 */
	public List<Video> findVideoByUserId(@Param("userId")String userId);
	
	/**
	 * @Title: deleteVideoById
	 * @Description: 删除指定视频信息
	 * @author: 张璐婷 
	 * @date: 2020年5月16日 上午11:15:57
	 */
	public int deleteVideoById(@Param("videoId")String videoId);
	
	/**
	 * @Title: addLikeByVideoId
	 * @Description: 点赞
	 * @author: 张璐婷 
	 * @date: 2020年5月22日 下午12:26:57
	 */
	public int addLikeByVideoId(@Param("videoId")String videoId);
	
	/**
	 * @Title: deleteLikeByVideoId
	 * @Description: 取消点赞
	 * @author: 张璐婷 
	 * @date: 2020年5月22日 下午12:27:22
	 */
	public int deleteLikeByVideoId(@Param("videoId")String videoId);
	
	/**
	 * @Description: 查询所有视频
	 * @author: 邸祯策
	 */
	public List<Video> queryAll();
	
}
