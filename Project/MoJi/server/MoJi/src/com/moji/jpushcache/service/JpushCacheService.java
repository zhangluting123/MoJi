package com.moji.jpushcache.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.moji.entity.JPushCache;
import com.moji.jpushcache.dao.JpushCacheMapper;

/**   
 * @ClassName: JpushCacheService   
 * @Description:TODO
 * @author: 张璐婷
 * @date: 2020年4月26日 下午2:16:20       
 */
@Service
public class JpushCacheService {
	
	@Resource
	private JpushCacheMapper jpushCacheMapper;

	/**
	 * @Title: addJpushCacheMsg
	 * @Description: 增加通知消息
	 * @author: 张璐婷 
	 * @date: 2020年4月26日 下午2:18:26
	 */
	public int addJpushCacheMsg(JPushCache jPushCache) {
		return this.jpushCacheMapper.insertJpushCacheMsg(jPushCache);
	}
	
	/**
	 * @Title: findJPushCache
	 * @Description: 发送通知消息
	 * @author: 张璐婷 
	 * @date: 2020年4月26日 下午2:18:42
	 */
	public List<JPushCache> findJPushCache(String receiveId) {
		return this.jpushCacheMapper.queryJPushCache(receiveId);
	}
	
	/**
	 * @Title: removeJPushCache
	 * @Description: 删除通知消息
	 * @author: 张璐婷 
	 * @date: 2020年4月26日 下午3:29:11
	 */
	public int removeJPushCache(Integer jpushId) {
		return this.jpushCacheMapper.deleteJPushCache(jpushId);
	}
}
