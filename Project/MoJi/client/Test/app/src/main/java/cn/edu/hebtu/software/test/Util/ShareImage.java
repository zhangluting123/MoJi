package cn.edu.hebtu.software.test.Util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import cn.edu.hebtu.software.test.R;

/**
 * @ProjectName:    MoJi
 * @Description:    分享图片
 * @Author:         张璐婷
 * @CreateDate:     2020/4/21 20:49
 * @Version:        1.0
 */
public class ShareImage {
    /**
     *  @author: 张璐婷
     *  @time: 2020/4/21  16:56
     *  @Description: 图片上写入文字
     */
    public static Bitmap drawTextAtBitmap(Context context, Bitmap head, String userName, String text) {
        Bitmap background = BitmapFactory.decodeResource(context.getResources(), R.mipmap.card);
        int x = background.getWidth();
        int y = background.getHeight();

        // 创建一个和原图同样大小的位图
        Bitmap newbit = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newbit);

        TextPaint textPaint = new TextPaint();
        canvas.drawBitmap(background, 0, 0, textPaint); // 在原始位置0，0插入背景图

        //用户信息
        int a = DensityUtil.dip2px(context,50);
        Rect mDestRect = new Rect(a,a,2*a,2*a);//绘制图片大小
        canvas.drawBitmap(head,null,mDestRect,new Paint());
        Paint paint = new Paint();
        paint.setTextSize(DensityUtil.sp2px(context, 16));
        canvas.drawText(userName, 5*a/2, 3*a/2, paint);


        textPaint.setColor(context.getResources().getColor(R.color.colorPrimaryDark));
        textPaint.setTextSize(DensityUtil.sp2px(context, 18));
        // 文本，画笔，每一行文本宽度，文本排列方式，行高
        StaticLayout layout = new StaticLayout(text,textPaint,x-a*2, Layout.Alignment.ALIGN_NORMAL,1.5F,0.0F,true);
        //文本居中显示
        canvas.translate(a,(y-layout.getHeight())/2);
        layout.draw(canvas);


        return newbit;
    }
    /**
     *  @author: 张璐婷
     *  @time: 2020/4/21  16:57
     *  @Description: 保存图片至系统文件夹
     */
    public static File saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片 创建文件夹
        File appDir = new File(Environment.getExternalStorageDirectory(), "moji-picture");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        //图片文件名称
        String fileName = "moji_"+System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        String path = file.getAbsolutePath();
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), path, fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        return file;
    }
}
