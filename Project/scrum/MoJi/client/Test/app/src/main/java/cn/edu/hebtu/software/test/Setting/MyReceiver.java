package cn.edu.hebtu.software.test.Setting;


import android.content.Context;
import android.content.Intent;
import android.util.Log;


import cn.edu.hebtu.software.test.DetailActivity.MailDetailActivity;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class MyReceiver extends JPushMessageReceiver{
//    private Context context;
//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch(msg.what){
//                case 1001:
//                    //设置别名
//                    JPushInterface.setAlias(context,2, (String)msg.obj);
//                    break;
//            }
//        }
//    };

    /**
     * 当打开通知消息时回调
     * @param context
     * @param notificationMessage
     */
    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
            //获取通知消息的内容
            String content = notificationMessage.notificationContent;
            Log.e("notif", notificationMessage.toString());
            Log.i("zlt","通知消息内容：" +content);
            String extra = notificationMessage.notificationExtras;
            //在Activity中显示通知消息的内容
            Intent intent = new Intent(context, MailDetailActivity.class);
            intent.putExtra("extra", extra);
            context.startActivity(intent);
    }



//    @Override
//    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
//        super.onAliasOperatorResult(context, jPushMessage);
//        switch (jPushMessage.getErrorCode()) {
//            case 0:
//                // 这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
//                SharedUtil.putString("app_JPushAlias",context, "alias", "success");
//                break;
//            case 6002:
//                // 延迟 60 秒来调用 Handler 设置别名(网络不佳时)
//
//                this.context = context;
//                mHandler.sendMessageDelayed(mHandler.obtainMessage(1001, jPushMessage.getAlias()), 1000 * 60);
//
//                break;
//            default:
//                break;
//        }
//        Log.e("zlt", jPushMessage.toString());
//    }



}
