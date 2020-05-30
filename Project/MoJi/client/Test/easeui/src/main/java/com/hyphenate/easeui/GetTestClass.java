package com.hyphenate.easeui;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * @ProjectName:    MoJi
 * @Description:    ContentProvider共享数据库信息
 * @Author:         张璐婷
 * @CreateDate:     2020/5/29 19:53
 * @Version:        1.0
 */
public class GetTestClass {

    public static String avatarAndName(Context context,String userId){
        ContentResolver resolver = context.getContentResolver();
        Uri uri = Uri.parse("content://onest.zlt.user.provider/user");
        Cursor cursor = resolver.query(uri, null, userId, null,null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()){
            String avatar = cursor.getString(1);
            String name = cursor.getString(2);
            buffer.append(avatar+";"+name+"\n");
        }
        return buffer.toString();
    }

    public static String getIp(){
        return "123.56.175.200";//云服务器
//        return "192.168.2.167";//zlt
    }

}
