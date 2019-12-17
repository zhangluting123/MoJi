package com.software.moji.util;

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
/**
 *
 * @ClassName: 
 * @Description: JPush������ 
 * @author 
 * @date 
 *
 */
public class JPushUtil {
	//�������appkey
    private final static String APPKEY = "8a6de1e19ff318f09c8f77c7";
    //�������Master Secret
    private final static String MASTERSECRET = "f6620bbf0dd369c231825d3e";
 
    private static JPushClient jPushClient = new JPushClient(MASTERSECRET,APPKEY);
    
    /**
     * @���͸����а�׿�û�
     * @param notification_title ֪ͨ���ݱ���
     * @param msg_title ��Ϣ���ݱ���
     * @param msg_content ��Ϣ����
     * @param extrasparam ��չ�ֶ�
     * @return 0����ʧ�ܣ�1���ͳɹ�
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
     * @���͸�������ʶ�������û�[ָ���û��յ�����Ϣ]
     * @param registrationId �豸��ʶ
     * @param notification_title ֪ͨ���ݱ���
     * @param msg_title ��Ϣ���ݱ���
     * @param msg_content ��Ϣ����
     * @param extrasparam ��չ�ֶ�
     * @return 0����ʧ�ܣ�1���ͳɹ�
     */
    public static int sendToBieMing( String bieming,String notification_title, String msg_title, String msg_content, Map<String, String> extrasparams) {
        int result = 0;
        try {
            PushPayload pushPayload= JPushUtil.buildPushObject_all_BieMing_alertWithTitle(bieming,notification_title,msg_title,msg_content,extrasparams);
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
     * @���͸��豸��ʶ�������û�
     * @param registrationId �豸��ʶ
     * @param notification_title ֪ͨ���ݱ���
     * @param msg_title ��Ϣ���ݱ���
     * @param msg_content ��Ϣ����
     * @param extrasparam ��չ�ֶ�
     * @return 0����ʧ�ܣ�1���ͳɹ�
     * @��ע��������������Ϣ�ĽǱ� ֪ͨ����������
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
     * @���͸�����Android�û����㲥��ʽ
     * @param notification_title
     * @param msg_title
     * @param msg_content
     * @param extrasparams
     * @return
     */
    private static PushPayload buildPushObject_android_all_alertWithTitle(String notification_title, String msg_title, String msg_content, Map<String, String> extrasparams) {
        return PushPayload.newBuilder()
                //ָ��Ҫ���͵�ƽ̨��all����ǰӦ�������˵�����ƽ̨��Ҳ���Դ�android�Ⱦ���ƽ̨
                .setPlatform(Platform.android())
                //ָ�����͵Ľ��ն���all���������ˣ�Ҳ����ָ���Ѿ����óɹ���tag��alias���ӦӦ�ÿͻ��˵��ýӿڻ�ȡ����registration id
                .setAudience(Audience.all())
                //jpush��֪ͨ��android����jpushֱ���·���iOS����apns�������·���Winphone����mpns�·�
                .setNotification(Notification.newBuilder()
                        //ָ����ǰ���͵�android֪ͨ
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(notification_title)
                                .setTitle(notification_title)
                                //���ֶ�Ϊ͸���ֶΣ�������ʾ��֪ͨ�����û�����ͨ�����ֶ�����һЩ�����������ض���key��Ҫָ����ת��ҳ�棨value��
                               .addExtras(extrasparams)
                                .build())
                        .build()
                )
                //Platformָ������Щƽ̨�ͻ���ָ��ƽ̨�з��������������豸�������͡� jpush���Զ�����Ϣ��
                .setMessage(Message.newBuilder()
                        .setMsgContent(msg_content)
                        .setTitle(msg_title)
                        .addExtras(extrasparams)
                        .build())
 
                .setOptions(Options.newBuilder()
                        //���ֶε�ֵ������ָ��������Ҫ���͵�apns������false��ʾ������true��ʾ��������android���Զ�����Ϣ������
                        .setApnsProduction(false)
                        //���ֶ��Ǹ��������Լ������ͱ�ţ����������߷ֱ����ͼ�¼
                        .setSendno(1)
                        //���ֶε�ֵ������ָ�������͵����߱���ʱ��������������ֶ���Ĭ�ϱ���һ�죬���ָ������ʮ�죬��λΪ��
                        .setTimeToLive(864000)
                        .build())
                .build();
    }
    
    /**
     * @����
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
                        //���ֶε�ֵ������ָ��������Ҫ���͵�apns������false��ʾ������true��ʾ��������android���Զ�����Ϣ������
                        .setApnsProduction(false)
                        //���ֶ��Ǹ��������Լ������ͱ�ţ����������߷ֱ����ͼ�¼
                        .setSendno(1)
                        //���ֶε�ֵ������ָ�������͵����߱���ʱ��������������ֶ���Ĭ�ϱ���һ�죬���ָ������ʮ�죻
                        .setTimeToLive(864000)
                        .build())
 
                .build();
 
    }
    
    /**
     * @��ָ���豸id���������豸
     * @param registrationId        �豸id
     * @param notification_title    ֪ͨ����
     * @param msg_title             ��Ϣ����
     * @param msg_content           ��Ϣ����
     * @param extrasparams          �����ֶ�
     * @return
     */
    private static PushPayload buildPushObject_all_registrationId_alertWithTitle(String registrationId,String notification_title, String msg_title, String msg_content,  Map<String, String> extrasparams) {
        return PushPayload.newBuilder()
                //ָ��Ҫ���͵�ƽ̨��all����ǰӦ�������˵�����ƽ̨��Ҳ���Դ�android�Ⱦ���ƽ̨
                .setPlatform(Platform.all())
                //ָ�����͵Ľ��ն���all���������ˣ�Ҳ����ָ���Ѿ����óɹ���tag��alias���ӦӦ�ÿͻ��˵��ýӿڻ�ȡ����registration id
                .setAudience(Audience.registrationId(registrationId))
                //jpush��֪ͨ��android����jpushֱ���·���iOS����apns�������·���Winphone����mpns�·�
                .setNotification(Notification.newBuilder()
                        //ָ����ǰ���͵�android֪ͨ
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(notification_title)
                                .setTitle(notification_title)
                                .addExtras(extrasparams)
                                .build())
                        //ָ����ǰ���͵�iOS֪ͨ
                        .addPlatformNotification(IosNotification.newBuilder()
                                //��һ��IosAlert����ָ��apns title��title��subtitle��
                                .setAlert(notification_title)
                                .incrBadge(1)
                                //���ֶε�ֵdefault��ʾϵͳĬ����������sound.caf��ʾ����������Ŀ��������sound.caf���������ѣ�
                                // ���ϵͳû�д���Ƶ����ϵͳĬ���������ѣ����ֶ���������ַ�����iOS9�����ϵ�ϵͳ�����������ѣ����µ�ϵͳ��Ĭ������
                                .setSound("sound.caf")
                                //���ֶ�Ϊ͸���ֶΣ�������ʾ��֪ͨ�����û�����ͨ�����ֶ�����һЩ�����������ض���key��Ҫָ����ת��ҳ�棨value��
                               
                                //����˵����������һ��background���ͣ����˽�background����http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                                //ȡ����ע�ͣ���Ϣ����ʱios���޷��������������
                                // .setContentAvailable(true)
                                .build())
 
 
                        .build())
                //Platformָ������Щƽ̨�ͻ���ָ��ƽ̨�з��������������豸�������͡� jpush���Զ�����Ϣ��
                // sdkĬ�ϲ����κδ���������֪ͨ��ʾ�����鿴�ĵ�http://docs.jpush.io/guideline/faq/��
                .setMessage(Message.newBuilder()
                        .setMsgContent(msg_content)
                        .setTitle(msg_title)
                        .addExtras(extrasparams)
                        .build())
 
                .setOptions(Options.newBuilder()
                        //���ֶε�ֵ������ָ��������Ҫ���͵�apns������false��ʾ������true��ʾ��������android���Զ�����Ϣ������
                        .setApnsProduction(false)
                        //���ֶ��Ǹ��������Լ������ͱ�ţ����������߷ֱ����ͼ�¼
                        .setSendno(1)
                        //���ֶε�ֵ������ָ�������͵����߱���ʱ��������������ֶ���Ĭ�ϱ���һ�죬���ָ������ʮ�죻
                        .setTimeToLive(864000)
                        .build())
 
                .build();
 
    }
    
}

