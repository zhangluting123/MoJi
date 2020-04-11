package cn.edu.hebtu.software.test.Util;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.core.app.NotificationManagerCompat;

/**
 * @ProjectName:    MoJi
 * @Description:    通知权限工具类
 * @Author:         张璐婷
 * @CreateDate:     2019/12/7 13:10
 * @Version:        1.0
 */
public class PermissionUtils {

    public static void openNotifiPermission(Context context) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        boolean NoTiIsOpened = manager.areNotificationsEnabled();

        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean GPSIsOpened = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
        if(!NoTiIsOpened || !GPSIsOpened) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
        }

    }
}
