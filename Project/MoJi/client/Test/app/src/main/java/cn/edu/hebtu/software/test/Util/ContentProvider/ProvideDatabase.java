package cn.edu.hebtu.software.test.Util.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import cn.edu.hebtu.software.test.Util.LocalSQLiteDataBase;

/**
 * @ProjectName:    MoJi
 * @Description:    共享类
 * @Author:         张璐婷
 * @CreateDate:     2020/5/30 7:56
 * @Version:        1.0
 */
public class ProvideDatabase extends ContentProvider {

    /**
     * 自定义代码
    */
    private static final int USER_DIR = 0;
    /**
     * 权限:一般程序包名.provider
     */
    public static final String AUTHORITY = "onest.zlt.user.provider";
    /**
     * Uri匹配器
     */
    private static UriMatcher uriMatcher;
    /**
     * 自定义数据帮助类
     */
    private LocalSQLiteDataBase dbHelper;
    /**
     * 创建Uri匹配器，调用添加Uri方法添加权限，path，自定义代码
     */
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        /**
         * databaseTest程序book表全部和某项
         */
        uriMatcher.addURI(AUTHORITY, "user",USER_DIR);

    }

    @Override
    public boolean onCreate() {
        dbHelper = new LocalSQLiteDataBase(getContext(),"localmoji.db",null,1);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {//匹配uri成功执行下面
            case USER_DIR://查询user所有
                String where = null;
                if(selection != null && !"".equals(selection)){
                    where = "user_id='" +selection+"'";
                }
                return db.query("user_detail", projection, where, null, null, null, sortOrder);
            default:
                break;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String str = null;
        switch(uriMatcher.match(uri)){
            case USER_DIR:
                str = "vnd.zlt.cursor.dir/onest.zlt.user";
                break;
        }
        return str;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
