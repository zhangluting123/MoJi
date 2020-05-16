/**
 * @Title: VideoController.java
 * @Package com.moji.video.controller
 * @Description: TODO(用一句话描述该文件做什么)
 * @author 春波
 * @date 2020年4月17日
 * @version V1.0
 */
package com.moji.video.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.management.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.ibatis.annotations.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.moji.entity.Video;
import com.moji.video.service.VideoService;

/**
 * @ClassName: VideoController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 春波
 * @date 2020年4月17日
 *
 */
@RestController
@RequestMapping("/video")
public class VideoController {

	@Autowired
	private VideoService videoService;
	
	@RequestMapping(value="/myList",method=RequestMethod.GET, produces="application/json;charset=utf-8")
	public String listByUserId(@RequestParam("userId")String userId) {
		List<Video> list = this.videoService.findVideoByUserId(userId);
		Gson gson = new Gson();
		String str = gson.toJson(list);
		return str;
	}
	
	@RequestMapping(value="/delete",method=RequestMethod.GET, produces="application/json;charset=utf-8")
	public String delete(@RequestParam("videoId")String videoId) {
		int n = this.videoService.deleteVideoById(videoId);
		if(n > 0) {
			return "1";
		}else {
			return "0";
		}
	}
	
	@RequestMapping(value="/upload",method=RequestMethod.POST, produces="application/json;charset=utf-8")
	public String upload(HttpServletRequest request) {
		Video video = new Video();
		video.setLike(0);
		String userId = "";
		String re = "FAIL";
		
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem> items = upload.parseRequest(request);
			int m = 0;
			for(FileItem fi : items) {
				if(fi.isFormField()) {
					switch (fi.getFieldName()) {
						case "videoId":
							video.setVideoId(fi.getString("UTF-8"));//用户id
							break;
						case "videoTitle":
							video.setTitle(fi.getString("UTF-8"));
							break;
						case "videoContent":
							video.setContent(fi.getString("UTF-8"));
						case "videoPath":
							video.setPath(fi.getString("UTF-8"));
							break;
						case "videoDuration":
							video.setDuration(fi.getString("UTF-8"));
							break;
						case "videoSize":
							video.setSize(fi.getString("UTF-8"));
							break;
						case "userId":
							userId = fi.getString("UTF-8");
							break;
						case "videoTag":
							video.setTag(fi.getString("UTF-8"));
						default:
							break;
					}
				}else {
					String realPath = request.getSession().getServletContext().getRealPath("/");
					fi.write(new File(realPath+"video\\"+fi.getName()));
					video.setPath("video/"+fi.getName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//重新设置id
		String newVideoId = video.getVideoId() + userId;
		int j = this.videoService.checkVideoId(newVideoId);
		if(j > 0) {
			newVideoId = newVideoId + j;
		}
		video.setVideoId(newVideoId);
		//设置上传时间
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		//转换成字符串格式
		String time = simpleDateFormat.format(date);
		video.setUploadTime(time);
		//往数据库写入数据
		int i = this.videoService.addVideo(video, userId);
		if(i > 0) {
			re = "SUCCESS";
		}
		return re;
	}

}
