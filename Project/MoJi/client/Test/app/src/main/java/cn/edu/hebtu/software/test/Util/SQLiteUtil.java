package cn.edu.hebtu.software.test.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import cn.edu.hebtu.software.test.Data.User;

public class SQLiteUtil {
    private Context context;
    private LocalSQLiteDataBase dbHelper;
    private SQLiteDatabase db;

    public SQLiteUtil(Context context) {
        this.context = context;
    }

    public void openDB(Context context){
        dbHelper = new LocalSQLiteDataBase(context,"localmoji.db",null,1);
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        db.close();
    }


    public  void insert(User user){
        openDB(context);
        ContentValues cv = new ContentValues();
        cv.put("user_id",user.getUserId());
        cv.put("avatar_path",user.getUserHeadImg());
        cv.put("user_name",user.getUserName());
        cv.put("sex",user.getSex());
        cv.put("signature",user.getSignature());
        cv.put("occupation",user.getOccupation());
        cv.put("phone",user.getPhone());
        db.insert("user_detail", null, cv);
        close();
    }

    public void update(User user){
        openDB(context);
        ContentValues cv = new ContentValues();
        cv.put("avatar_path",user.getUserHeadImg());
        cv.put("user_name",user.getUserName());
        cv.put("sex",user.getSex());
        cv.put("signature",user.getSignature());
        cv.put("occupation",user.getOccupation());
        cv.put("phone",user.getPhone());
        String whereU = "user_id ='" +user.getUserId()+"'";
        db.update("user_detail", cv, whereU, null);
        close();
    }

    public void delete(String userId){
        openDB(context);
        String where = "user_id ='" + userId+"'";
        db.delete("user_detail", where, null);
        close();
    }

    public User query(String userId){
        openDB(context);
        Cursor cursor = db.rawQuery("select * from user_detail where user_id = ?", new String[]{userId});
        //遍历Cursor
        User user = new User();
        if(cursor.moveToFirst()){//判断是否有数据，有则移动到第一条
            String avatar = cursor.getString(cursor.getColumnIndex("avatar_path"));
            String userName = cursor.getString(cursor.getColumnIndex("user_name"));
            String sex = cursor.getString(cursor.getColumnIndex("sex"));
            String signature = cursor.getString(cursor.getColumnIndex("signature"));
            String occupation = cursor.getString(cursor.getColumnIndex("occupation"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            user.setUserId(userId);
            user.setUserHeadImg(avatar);
            user.setUserName(userName);
            user.setSex(sex);
            user.setSignature(signature);
            user.setOccupation(occupation);
            user.setPhone(phone);
        }
        close();
        return user;
    }

    public boolean queryExistUser(String userId){
        try {
            openDB(context);
            Cursor cursor = db.rawQuery("select * from user_detail where user_id = ?", new String[]{userId});
            if(cursor.moveToFirst()) {
                return true;
            }
        }finally {
            close();
        }
        return false;
    }


}
