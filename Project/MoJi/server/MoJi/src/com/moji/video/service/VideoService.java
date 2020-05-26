/**
 * @Title: VideoService.java
 * @Package com.moji.video.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月17日
 * @version V1.0
 */
package com.moji.video.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
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
	
	/**
	 * @Title: findVideoByUserId
	 * @Description: 根据用户id值查询视频
	 * @author: 张璐婷 
	 * @date: 2020年5月16日 上午11:17:07
	 */
	public List<Video> findVideoByUserId(String userId) {
		return this.videoMapper.findVideoByUserId(userId);
	}
	
	/**
	 * @Title: findVideoById
	 * @Description: 根据id查询视频
	 * @author: ming
	 * @date: 2020年5月16日 
	 */
	public Video findVideoById(String Id) {
		return this.videoMapper.findVideoById(Id);
	}
	
	/**
	 * @Title: deleteVideoById
	 * @Description: 删除video
	 * @author: 张璐婷 
	 * @date: 2020年5月16日 上午11:17:50
	 */
	public int deleteVideoById(String videoId) {
		return this.videoMapper.deleteVideoById(videoId);
	}
	
	/**
	 * @Title: addLikeByVideoId
	 * @Description: 点赞
	 * @author: 张璐婷 
	 * @date: 2020年5月22日 下午12:26:57
	 */
	public int addLikeByVideoId(String videoId) {
		return this.videoMapper.addLikeByVideoId(videoId);
	}
	
	/**
	 * @Title: deleteLikeByVideoId
	 * @Description: 取消点赞
	 * @author: 张璐婷 
	 * @date: 2020年5月22日 下午12:27:22
	 */
	public int deleteLikeByVideoId(String videoId) {
		return this.videoMapper.deleteLikeByVideoId(videoId);
	}
	
	/**
	 * @Description: 查询所有视频
	 * @author: ming
	 */
	public List<Video> queryAll() {
		return this.videoMapper.queryAll();
	}
}
