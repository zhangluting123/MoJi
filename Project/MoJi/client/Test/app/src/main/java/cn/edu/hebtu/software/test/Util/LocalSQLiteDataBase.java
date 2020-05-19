package cn.edu.hebtu.software.test.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class LocalSQLiteDataBase extends SQLiteOpenHelper {
    private Context context;
    public LocalSQLiteDataBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE `user_detail` (\n" +
                "  `user_id` varchar(20) NOT NULL DEFAULT '',\n" +
                "  `avatar_path` varchar(50) DEFAULT 'avatar/default_head.png',\n" +
                "  `user_name` varchar(20) DEFAULT NULL,\n" +
                "  `sex` varchar(10) DEFAULT 'boy',\n" +
                "  `signature` varchar(300) DEFAULT NULL,\n" +
                "  `occupation` varchar(10) DEFAULT NULL,\n" +
                "  `password` varchar(12) DEFAULT NULL,\n" +
                "  `phone` varchar(11) DEFAULT NULL,\n" +
                "  PRIMARY KEY (`user_id`)\n" +
                ")";
        db.execSQL(sql);
//        Toast.makeText(context, "创建数据库表成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
