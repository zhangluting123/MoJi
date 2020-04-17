/**
 * @Title: VideoService.java
 * @Package com.moji.video.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月17日
 * @version V1.0
 */
package com.moji.video.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moji.entity.Video;
import com.moji.video.dao.VideoMapper;

/**
 * @ClassName: VideoService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 春波
 * @date 2020年4月17日
 *
 */
@Service
public class VideoService {

	@Autowired
	private VideoMapper videoMapper;
	
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
	public int addVideo(Video video, String userId) {
		return this.videoMapper.addVideo(video, userId);
	}

	/**
	 * 
	 * @Title: checkVideoId
	 * @Description: 查询是否存在此id
	 * @param @param videoId
	 * @param @return 参数
	 * @return int 返回类型
	 * @throws
	 */
	public int checkVideoId(String videoId) {
		return this.videoMapper.checkVideoId(videoId);
	}
}
