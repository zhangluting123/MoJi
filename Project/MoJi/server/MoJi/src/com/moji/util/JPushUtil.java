package com.moji.util;

import java.util.Map;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.*;
import cn.jpush.api.report.MessagesResult;
import cn.jpush.api.report.ReceivedsResult;
import cn.jpush.api.report.ReceivedsResult.Received;
import cn.jpush.api.report.model.CheckMessagePayload;
/**
 *
 * @ClassName: 
 * @Description: JPush工具类 
 * @author 
 * @date 
 *
 */
public class JPushUtil {
	//这是你的appkey
    private final static String APPKEY = "8a6de1e19ff318f09c8f77c7";
    //这是你的Master Secret
    private final static String MASTERSECRET = "f6620bbf0dd369c231825d3e";
    
 
    private static JPushClient jPushClient = new JPushClient(MASTERSECRET,APPKEY);
    
    /**
     * @Title: testGetReport
     * @Description: 测试msg_id是否可达等
     * @author: 张璐婷 
     * @date: 2020年4月26日 下午1:45:18
     */
    public static int testGetReport(String msgId) {
        JPushClient jpushClient = new JPushClient(MASTERSECRET, APPKEY);
        int count = 0;
		try {
			String mString = "{29273460422358885}";
            ReceivedsResult result = jpushClient.getReportReceiveds(msgId);
            Received received = result.received_list.get(0);
            System.out.println("--------------received---------------");
            System.out.println("android_received:" + received.android_received 
                    + "\nios:" + received.ios_apns_sent);  
			
            System.out.println("Got result - " + result);
            count = received.android_received;
        } catch (APIConnectionException e) {
            System.out.println("Connection error. Should retry later. "+ e);
            
        } catch (APIRequestException e) {
            System.out.println("Error response from JPush server. Should review and fix it. "+e);
            System.out.println("HTTP Status: " + e.getStatus());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Error Message: " + e.getErrorMessage());
        }
		return count;
	}
    
    /**
     * @发送给所有安卓用户
     * @param notification_title 通知内容标题
     * @param msg_title 消息内容标题
     * @param msg_content 消息内容
     * @param extrasparam 扩展字段
     * @return 0推送失败，1推送成功
     */
    public static int sendToAllAndroid( String notification_title, String msg_title, String msg_content, Map<String, String> extrasparams) {
        int result = 0;
        try {
            PushPayload pushPayload= JPushUtil.buildPushObject_android_all_alertWithTitle(notification_title,msg_title,msg_content,extrasparams);
            System.out.println(pushPayload);
            PushResult pushResult=jPushClient.sendPush(pushPayload);
            System.out.println(pushResult);
            if(pushResult.getResponseCode()==200){
                result=1;
            }
        } catch (Exception e) {
 
            e.printStackTrace();
        }
 
         return result;
    }
    
    /**
     * @推送给别名标识参数的用户[指定用户收到该消息]
     * @param registrationId 设备标识
     * @param notification_title 通知内容标题
     * @param msg_title 消息内容标题
     * @param msg_content 消息内容
     * @param extrasparam 扩展字段
     * @return 0推送失败，1推送成功
     */

	public static String sendToBieMing( String bieming,String notification_title, String msg_title, String msg_content, Map<String, String> extrasparams) {
        String result = "";
        try {
            PushPayload pushPayload= JPushUtil.buildPushObject_all_BieMing_alertWithTitle(bieming,notification_title,msg_title,msg_content,extrasparams);
            System.out.println(pushPayload);
            PushResult pushResult=jPushClient.sendPush(pushPayload);
            System.out.println(pushResult);
            
            if(pushResult.getResponseCode()==200){
                result = pushResult.msg_id + "";
            }
        } catch (APIConnectionException e) {
            e.printStackTrace();
 
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
        return result;
        
    }
    
 
    
    /**
     * @推送给设备标识参数的用户
     * @param registrationId 设备标识
     * @param notification_title 通知内容标题
     * @param msg_title 消息内容标题
     * @param msg_content 消息内容
     * @param extrasparam 扩展字段
     * @return 0推送失败，1推送成功
     * @备注还可设置推送消息的角标 通知铃声等数据
     */
    public static int sendToRegistrationId( String registrationId,String notification_title, String msg_title, String msg_content, Map<String, String> extrasparams) {
        int result = 0;
        try {
            PushPayload pushPayload= JPushUtil.buildPushObject_all_registrationId_alertWithTitle(registrationId,notification_title,msg_title,msg_content,extrasparams);
            System.out.println(pushPayload);
            PushResult pushResult=jPushClient.sendPush(pushPayload);
            System.out.println(pushResult);
            if(pushResult.getResponseCode()==200){
                result=1;
            }
        } catch (APIConnectionException e) {
            e.printStackTrace();
 
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
 
         return result;
    }
    /**
     * @发送给所有Android用户，广播形式
     * @param notification_title
     * @param msg_title
     * @param msg_content
     * @param extrasparams
     * @return
     */
    private static PushPayload buildPushObject_android_all_alertWithTitle(String notification_title, String msg_title, String msg_content, Map<String, String> extrasparams) {
        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.android())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.all())
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(notification_title)
                                .setTitle(notification_title)
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                               .addExtras(extrasparams)
                                .build())
                        .build()
                )
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                .setMessage(Message.newBuilder()
                        .setMsgContent(msg_content)
                        .setTitle(msg_title)
                        .addExtras(extrasparams)
                        .build())
 
                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(false)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天，单位为秒
                        .setTimeToLive(864000)
                        .build())
                .build();
    }
    
    /**
     * @别名
     * @param bieming
     * @param notification_title
     * @param msg_title
     * @param msg_content
     * @param extrasparam
     * @return
     */
    private static PushPayload buildPushObject_all_BieMing_alertWithTitle(String bieming,String notification_title, String msg_title, String msg_content, Map<String, String> extrasparams) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias(bieming))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                        .setAlert(msg_content)
                        .setTitle(notification_title)
                        .addExtras(extrasparams)
                        .build())
                   
                .build()
                )
           
                .setMessage(Message.newBuilder()
                        .setMsgContent(msg_content)
                        .setTitle(msg_title)
                        .build())
 
                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(false)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
                        .setTimeToLive(0)
                        .build())
 
                .build();
 
    }
    
    /**
     * @给指定设备id推送所有设备
     * @param registrationId        设备id
     * @param notification_title    通知标题
     * @param msg_title             消息标题
     * @param msg_content           消息内容
     * @param extrasparams          附加字段
     * @return
     */
    private static PushPayload buildPushObject_all_registrationId_alertWithTitle(String registrationId,String notification_title, String msg_title, String msg_content,  Map<String, String> extrasparams) {
        return PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.all())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.registrationId(registrationId))
                //jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(Notification.newBuilder()
                        //指定当前推送的android通知
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(notification_title)
                                .setTitle(notification_title)
                                .addExtras(extrasparams)
                                .build())
                        //指定当前推送的iOS通知
                        .addPlatformNotification(IosNotification.newBuilder()
                                //传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(notification_title)
                                .incrBadge(1)
                                //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                                .setSound("sound.caf")
                                //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                               
                                //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                //取消此注释，消息推送时ios将无法在锁屏情况接收
                                // .setContentAvailable(true)
                                .build())
 
 
                        .build())
                //Platform指定了哪些平台就会像指定平台中符合推送条件的设备进行推送。 jpush的自定义消息，
                // sdk默认不做任何处理，不会有通知提示。建议看文档http://docs.jpush.io/guideline/faq/的
                .setMessage(Message.newBuilder()
                        .setMsgContent(msg_content)
                        .setTitle(msg_title)
                        .addExtras(extrasparams)
                        .build())
 
                .setOptions(Options.newBuilder()
                        //此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                        .setApnsProduction(false)
                        //此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        //此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
                        .setTimeToLive(864000)
                        .build())
 
                .build();
 
    }
    
}