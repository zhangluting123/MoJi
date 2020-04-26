package com.moji.jpushcache.dao;


import java.util.List;

import com.moji.entity.JPushCache;

/**   
 * @ClassName: JpushCacheMapper   
 * @Description:TODO
 * @author: 张璐婷
 * @date: 2020年4月26日 下午2:15:47       
 */
public interface JpushCacheMapper {

	/**
	 * @Title: insertJpushCacheMsg
	 * @Description: 增加通知消息
	 * @author: 张璐婷 
	 * @date: 2020年4月26日 下午2:18:26
	 */
	public int insertJpushCacheMsg(JPushCache jPushCache);
	
	/**
	 * @Title: queryJPushCache
	 * @Description: 发送通知消息
	 * @author: 张璐婷 
	 * @date: 2020年4月26日 下午2:18:42
	 */
	public List<JPushCache> queryJPushCache(String receiveId);
	
	/**
	 * @Title: deleteJPushCache
	 * @Description: 删除缓存通知消息
	 * @author: 张璐婷 
	 * @date: 2020年4月26日 下午3:28:14
	 */
	public int deleteJPushCache(Integer jpushId);
}
