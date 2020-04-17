/**
 * @Title: VideoMapper.java
 * @Package com.moji.video.dao
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月17日
 * @version V1.0
 */
package com.moji.video.dao;

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
}
