package cn.edu.hebtu.software.test.DetailActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import cn.edu.hebtu.software.test.Data.Video;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.UploadAndDownload.UploadVideoTask;
import cn.edu.hebtu.software.test.Util.FileUtil;

public class UploadVideoActivity extends AppCompatActivity {

    //控件
    private Toolbar addNoteToolbar;
    private Button btnVideoUpload;
    private Button btnChooseVideo;
    private TextView tvVideoId;
    private TextView tvVideoTitle;
    private TextView tvVideoPath;
    private TextView tvVideoDuration;
    private TextView tvVideoSize;
    private EditText tvVideoContent;
    private Spinner videoTag;
    private ImageView videoImg;
    //视频数据
    private Video video = new Video();
    //tag数据
    private List<String> tags = new ArrayList<>();
    //视频路径
    private String vPath = "";
    //视频缩略图
    private Bitmap bitmap;
    //常量
    public static final int RC_CHOOSE_VIDEO = 1;
    //ip
    private String ip;
    //自定义点击事件监听器
    private MyCustomListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        //给video添加user
        final MyApplication data = (MyApplication) getApplication();
        ip = data.getIp();
        video.setUser(data.getUser());
        //设置下拉框
        tags.add("请选择");
        tags.add("美食");
        tags.add("吃喝");
        tags.add("玩乐");
        tags.add("交友");
        //新建适配器
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,tags);
        //adapter设置一个下拉列表样式，参数为系统子布局
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.MyTheam_color));
        }
        getViews();
        setSupportActionBar(addNoteToolbar);
        registListener();
        //Spinner加载适配器
        videoTag.setAdapter(adapter);
    }

    /**
     *  @author 春波
     *  @time 2020/4/17  11:29
     *  @Description：获取控件
     */
    private void getViews() {
        this.addNoteToolbar = findViewById(R.id.addNoteToolbar);
        this.btnVideoUpload = findViewById(R.id.btn_video_upload);
        this.btnChooseVideo = findViewById(R.id.btn_choose_video);
        this.tvVideoId = findViewById(R.id.tv_video_id);
        this.tvVideoTitle = findViewById(R.id.tv_video_title);
        this.tvVideoPath = findViewById(R.id.tv_video_path);
        this.tvVideoDuration = findViewById(R.id.tv_video_duration);
        this.tvVideoSize = findViewById(R.id.tv_video_size);
        this.tvVideoContent = findViewById(R.id.tx_video_content);
        this.videoTag =findViewById(R.id.spinnerVideoTagStatic);
        this.videoImg = findViewById(R.id.img_video_bitmap);
    }

    /**
     *  @author 春波
     *  @time 2020/4/17  11:29
     *  @Description：设置自定义点击事件监听器
     */
    private void registListener(){
        this.listener = new MyCustomListener();
        this.btnVideoUpload.setOnClickListener(listener);
        this.btnChooseVideo.setOnClickListener(listener);
        this.videoTag.setOnItemSelectedListener(new OnItemSelectedListenerImpl());
    }

    /**
     *  @author 春波
     *  @time 2020/4/17  11:29
     *  @Description：自定义点击事件监听器类
     */
    class MyCustomListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_video_upload:
                    uploadVideo();
                    break;
                case R.id.btn_choose_video:
                    readVideo();
                    break;
            }
        }
    }

    /**
     *  @author 春波
     *  @time 2020/4/24  12:16
     *  @Description：下拉框选择事件
     */
    private class OnItemSelectedListenerImpl implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String tag = parent.getItemAtPosition(position).toString();
            video.setTag(tag);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            video.setTag("null");
        }

    }

    /**
     *  @author 春波
     *  @time 2020/4/17  11:29
     *  @Description：选取视频
     */
    private void readVideo() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //未授权，申请授权(从相册选择视频需要读取存储卡的权限)
            ActivityCompat.requestPermissions(UploadVideoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, RC_CHOOSE_VIDEO);
        } else {
            //已授权，获取照片
            chooseVideo();
        }
    }

    /**
     *  @author 春波
     *  @time 2020/4/17  11:31
     *  @Description：权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_CHOOSE_VIDEO:   //相册选择照片权限申请返回
                chooseVideo();
                break;
        }
    }

    /**
     *  @author 春波
     *  @time 2020/4/17  11:29
     *  @Description：打开相册
     */
    private void chooseVideo() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
        startActivityForResult(intentToPickPic, RC_CHOOSE_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RC_CHOOSE_VIDEO:
                    Uri uri = data.getData();
                    String vPath = FileUtil.getFilePathByUri(this, uri);
                    if (!TextUtils.isEmpty(vPath)) {
                        getVideoData(uri);
                        videoImg.setImageBitmap(bitmap);
                        tvVideoId.setText("ID：" + video.getVideoId());
                        tvVideoTitle.setText("标题：" + video.getTitle());
                        tvVideoPath.setText("路径：" + video.getPath());
                        tvVideoDuration.setText("时长：" + video.getDuration());
                        tvVideoSize.setText("大小：" + video.getSize());
                    }
                    break;
            }
        }
    }

    /**
     *  @author 春波
     *  @time 2020/4/17  13:42
     *  @Description：得到选中视频的信息
     */
    public void getVideoData(Uri uri) {
        ContentResolver cr = this.getContentResolver();
        //数据库查询操作
        //第一个参数 uri：为要查询的数据库+表的名称
        //第二个参数 projection ： 要查询的列
        //第三个参数 selection ： 查询的条件，相当于SQL where
        //第三个参数 selectionArgs ： 查询条件的参数，相当于?
        //第四个参数 sortOrder ： 结果排序
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // 视频ID:MediaStore.Audio.Media._ID
                int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                video.setVideoId(videoId + "");
                // 视频名称：MediaStore.Audio.Media.TITLE
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                video.setTitle(title);
                // 视频路径：MediaStore.Audio.Media.DATA
                String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                vPath = videoPath;
                video.setPath(videoPath);
                // 视频时长（默认ms）：MediaStore.Audio.Media.DURATION
                int duration_ms = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                int duration_s = (int)Math.floor(duration_ms / 1000);
                String ds = duration_s + "";
                if (duration_s < 10){
                    ds = "0" + duration_s;
                }
                int duration_m = (int)Math.floor((duration_s % 3600) / 60);
                String dm = duration_m + "";
                if (duration_m < 10){
                    dm = "0" + duration_m;
                }
                int duration_h = duration_s / 3600;
                String dh = duration_h + "";
                if (duration_h < 10){
                    dh = "0" + duration_h;
                }
                video.setDuration(dh + ":" + dm + ":" + ds);
                // 视频大小（默认Byte）：MediaStore.Audio.Media.SIZE
                long size_byte = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                double size_MB = (size_byte * 1.0) / (1024 * 1024);
                String sm = new java.text.DecimalFormat("#.00").format(size_MB);
                video.setSize(sm + "MB");

                // 生成视频缩略图
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(this,Uri.parse(videoPath));
                bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            }
        }
    }

    /**
     *  @author 春波
     *  @time 2020/4/17  13:42
     *  @Description：上传视频及其信息到服务器
     */
    public void uploadVideo(){
        if(this.tvVideoContent.getText().toString().trim().equals("")){
            Toast toast = Toast.makeText(this,"请填写内容",Toast.LENGTH_SHORT);
            toast.show();
        }else {
            video.setContent(this.tvVideoContent.getText().toString().trim());
        }
        if("null".equals(video.getTag())){
            Toast toast = Toast.makeText(this,"请选择标签",Toast.LENGTH_SHORT);
            toast.show();
        }
        if (vPath.length() == 0){
            Toast toast = Toast.makeText(this,"请选择视频",Toast.LENGTH_SHORT);
            toast.show();
        }else {
            UploadVideoTask task = new UploadVideoTask(this, video);
            task.execute("http://" + ip + ":8080/MoJi/video/upload");
        }
    }
}
