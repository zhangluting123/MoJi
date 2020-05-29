package cn.edu.hebtu.software.test.DetailActivity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.base.fileprovider.FileProvider7;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.test.Adapter.CommentAdapter;
import cn.edu.hebtu.software.test.Adapter.GuidePageAdapter;
import cn.edu.hebtu.software.test.Data.Comment;
import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.Data.Video;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.ActivityManager;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;
import cn.edu.hebtu.software.test.Util.ShareImage;
import cn.edu.hebtu.software.test.Util.SoftKeyBoardListener;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @ProjectName: MoJi
 * @Description: 视频详情页
 * @Author: 邸祯策
 * @CreateDate: 2020/5/23
 * @Version: 1.0
 */

public class VideoDetailActivity extends AppCompatActivity{
    private RelativeLayout rlhead;
    private TextView userName;
    private TextView noteTime;
    private ImageView headImg;
    private TextView noteContent;
    private TextView commentCount;
    private EditText edtInsertComment;
    private Button btnSubmitComment;
    private LinearLayout noComment;
    private Video video;
    private List<Comment> commentList = new ArrayList<>();
    private RequestOptions options = new RequestOptions().circleCrop();

    private JCVideoPlayerStandard jcVideoPlayerStandard;
    private PopupWindow popupWindow;

    private MyApplication data;
    private String ip;
    private User user;

    private CommentAdapter commentAdapter;
    private String commentId;//当前选中评论ID

    private JCVideoPlayer.JCAutoFullscreenListener mSensorEventListener;
    private SensorManager mSensorManager;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    commentList = (List<Comment>)msg.obj;
                    initComment();
                    break;
                case 1003:
                    if(commentList.size() <= 0) { //之前没有评论，初始化adapter
                        noComment.setVisibility(View.GONE);
                        showCommentList();
                    }
                    Comment comment = new Comment();
                    user = data.getUser();
                    comment.setId((String) msg.obj);
                    comment.setUser(user);
                    comment.setCommentContent(edtInsertComment.getText().toString().trim());
                    comment.setCommentTime("今天");
                    comment.setReplyCount(0);
                    commentList.add(comment);
                    commentAdapter.flush(commentList);
                    int b = Integer.parseInt(commentCount.getText().toString());
                    b++;
                    commentCount.setText(b+"");
                    clearInput();
                    break;
                case 1004:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    for(int i = 0;i < commentList.size();++i){
                        if(commentList.get(i).getId().equals(commentId)){
                            Integer a = commentList.get(i).getReplyCount();
                            commentList.get(i).setReplyCount(++a);
                            break;
                        }
                    }
                    commentAdapter.flush(commentList);
                    clearInput();
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videodetail);

        ActivityManager.getInstance().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        }

        data = (MyApplication)getApplication();
        user = data.getUser();
        ip = data.getIp();

        getViews();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                finish();
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        video = bundle.getParcelable("video");
        noteTime.setText(video.getUploadTime());
        Glide.with(this).load("http://"+ip+":8080/MoJi/"+video.getUser().getUserHeadImg()).apply(options).into(headImg);
        String path = "http://"+ ip +":8080/MoJi/"+video.getPath();
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
        noteContent.setText(video.getContent());
        userName.setText(video.getUser().getUserName());
        //最终版本应由video.getPath()---》path
        jcVideoPlayerStandard.setUp(path, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        //同上
        Glide.with(this)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(4000000)
                                .centerCrop()
                                .error(R.drawable.fail)
                                .placeholder(R.drawable.fail)
                )
                .load(video.getPath())
                .into(jcVideoPlayerStandard.thumbImageView);

        //获得评论
        getComments();

        //注册监听器
        CustomMainOnClickListener listener = new CustomMainOnClickListener();
        btnSubmitComment.setOnClickListener(listener);
        headImg.setOnClickListener(listener);

    }


    class CustomMainOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_headImg:
                    Intent intent = new Intent(getApplicationContext(), OtherMsgActivity.class);
                    intent.putExtra("user",video.getUser());
                    startActivity(intent);
                    break;
                case R.id.btn_submitComment:
                    if(null != user.getUserId()) {
                        String content = edtInsertComment.getText().toString().trim();
                        if (content.length() == 0) {
                            Toast.makeText(VideoDetailActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            if(edtInsertComment.getHint().equals("请输入评论")) {
                                insertComment(content);
                            }else{
                                //回复评论
                                insertReplyComment();
                            }
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }

    private void getViews() {
        rlhead = findViewById(R.id.rl_head);//轮播图
        userName = findViewById(R.id.tv_userName);
        noteTime = findViewById(R.id.tv_noteTime);
        headImg = findViewById(R.id.iv_headImg);
        noteContent = findViewById(R.id.iv_noteContent);
        commentCount = findViewById(R.id.tv_commentCount);
        edtInsertComment = findViewById(R.id.edt_insertComment);
        btnSubmitComment = findViewById(R.id.btn_submitComment);
        noComment = findViewById(R.id.ll_noComment);
        jcVideoPlayerStandard = findViewById(R.id.videoplayer);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2019/12/11  18:40
     *  @Description: 在页面添加获取的评论
     */
    private void initComment(){
        if(commentList.size() <= 0){
            commentCount.setText("0");
            noComment();
        }else{
           commentCount.setText(commentList.size()+"");
            showCommentList();
        }
    }
    /**
     *  @author: 张璐婷
     *  @time: 2019/12/11  19:04
     *  @Description: 没有评论
     */
    private void noComment(){
        noComment.setVisibility(View.VISIBLE);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/4/14  8:48
     *  @Description: 展示评论列表
     */
    private void showCommentList(){
        ListView listView = findViewById(R.id.comment_list);
        commentAdapter = new CommentAdapter(1,commentList, R.layout.item_comment, getApplicationContext());
        listView.setAdapter(commentAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtInsertComment.setHint("回复@"+commentList.get(position).getUser().getUserName());
                edtInsertComment.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
                commentId = commentList.get(position).getId();
                edtInsertComment.setSelection(0);
                edtInsertComment.setFocusable(true);
                //键盘如果关闭弹出
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showPopupWindow(parent,commentList.get(position));
                return true;
            }
        });
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/4/13  16:53
     *  @Description: 点击评论弹出菜单
     */

    private void showPopupWindow(AdapterView<?> parent, Comment comment){
        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popup_menu,null);
        popupWindow = new PopupWindow(this);
        popupWindow.setContentView(v);
        popupWindow.setBackgroundDrawable(null);
        registerListener(comment,v);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true); //设置点击menu以外其他地方以及返回键退出
        popupWindow.setOutsideTouchable(true);  //设置触摸外面时消失
        //设置弹出位置
        popupWindow.showAtLocation(parent, Gravity.CENTER,0,0);
        popupWindow.setAnimationStyle(R.style.popup_menu);

        //设置弹出时背景色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        //关闭时背景色恢复
        popupWindow.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp1 = getWindow().getAttributes();
            lp1.alpha = 1f;
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getWindow().setAttributes(lp1);
        });

    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/4/14  17:06
     *  @Description: 监听器类
     */
    class CustomerOnClickListener implements View.OnClickListener {
        private Comment comment;
        public CustomerOnClickListener(Comment comment){
            this.comment = comment;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.share:
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(VideoDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    } else {
                        shareComment(comment);
                    }
                    break;
                case R.id.copy:
                    ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("tag1", comment.getCommentContent());
                    manager.setPrimaryClip(clipData);
                    Toast.makeText(getApplicationContext(), "已加入剪切板", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.report:
                    Toast.makeText(getApplicationContext(), "举报评论", Toast.LENGTH_SHORT).show();
                    break;
            }
            popupWindow.dismiss();
        }

    }
    /**
     *  @author: 张璐婷
     *  @time: 2020/4/21  21:02
     *  @Description: 分享评论
     */
    private void shareComment(Comment comment){
        new Thread(){
            @Override
            public void run() {
                Bitmap head = null;
                try {
                    URL url = new URL("http://"+ip+":8080/MoJi/"+comment.getUser().getUserHeadImg());
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    head = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bitmap newImg = ShareImage.drawTextAtBitmap(getApplicationContext(),head,comment.getUser().getUserName(),comment.getCommentContent());
                //分享图片
                File filePath = ShareImage.saveImageToGallery(getApplicationContext(), newImg);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                Uri uri = FileProvider7.getUriForFile(getApplicationContext(),filePath);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, uri);

                // 目标应用选择对话框的标题
                startActivity(Intent.createChooser(intent, "请选择"));
            }
        }.start();

    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/4/14  17:05
     *  @Description: 注册PopupWindow中元素的监听器
     *  必须是同一个视图点击才有效
     */
    private void registerListener(Comment comment,View v){
        CustomerOnClickListener listener = new CustomerOnClickListener(comment);
        Button report = v.findViewById(R.id.report);
        Button share = v.findViewById(R.id.share);
        Button copy = v.findViewById(R.id.copy);
        report.setOnClickListener(listener);
        share.setOnClickListener(listener);
        copy.setOnClickListener(listener);
    }


    /**
     *  @author: 张璐婷
     *  @time: 2020/4/14  17:06
     *  @Description: 触屏操作
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        onKeyBoardListener();
        return super.dispatchTouchEvent(ev);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/4/20  17:53
     *  @Description:  清空输入内容,键盘隐藏
     */
    private void clearInput(){
        edtInsertComment.setText("");
        edtInsertComment.setHint("请输入评论");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/4/16  8:44
     *  @Description: 监听键盘是否弹起
     */
    private void onKeyBoardListener(){
        SoftKeyBoardListener.setListener(VideoDetailActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
//                Log.e("软键盘显示高度", height+"");
            }

            @Override
            public void keyBoardHide(int height) {
//                Log.e("软键盘隐藏高度", height+"");
                if(!edtInsertComment.getHint().equals("请输入评论")) {
                    edtInsertComment.setHint("请输入评论");
                }
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     *  @author: 张璐婷
     *  @time: 2019/12/11  18:39
     *  @Description: 插入评论
     */
    private void insertComment(String content){
            new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getApplicationContext())){
                    try {
                        URL url = new URL("http://"+ip+":8080/MoJi/comment/add?noteId="+video.getVideoId()+"&userId="+data.getUser().getUserId()+"&commentContent="+content);
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String str = reader.readLine();
                        Message msg = Message.obtain();
                        String[] strings = str.split(",");
                        if("2".equals(strings[0]) || "1".equals(strings[0])){//2-评论发布成功,1-评论发布成功[未发送通知]
                            msg.obj = strings[1];Log.e("1notifcommentId=", strings[0]+"---"+strings[1]);
                            msg.what = 1003;
                            handler.sendMessage(msg);
                        }else{
                            msg.obj = "评论发布失败";
                            msg.what = 1001;
                            handler.sendMessage(msg);
                        }

                        in.close();
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Message message  = Message.obtain();
                    message.obj = "未连接到服务器";
                    message.what = 1001;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    /**
     *  @author: 张璐婷
     *  @time: 2019/12/11  18:39
     *  @Description: 获得评论
     */
    private void getComments(){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getApplicationContext())){
                    try {
                        List<Comment> list = new ArrayList<>();
                        URL url = new URL("http://"+ip+":8080/MoJi/comment/list?noteId="+video.getVideoId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String str = null;
                        if((str = reader.readLine()) != null){
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Comment>>(){}.getType();
                            list = gson.fromJson(str,type);
                        }
                        Message msg = Message.obtain();
                        msg.what = 1002;
                        msg.obj = list;
                        handler.sendMessage(msg);

                        in.close();
                        reader.close();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else{
                    Message message  = Message.obtain();
                    message.what = 1001;
                    message.obj = "未连接到服务器";
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/4/20  16:37
     *  @Description: 回复评论
     */
    public void insertReplyComment(){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getApplicationContext())){
                    try {
                        URL url = new URL("http://"+ip+":8080/MoJi/replyComment/add?replyContent="+edtInsertComment.getText().toString()+"&commentId="+commentId+"&replyUserId="+data.getUser().getUserId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        if(reader.readLine().equals("OK")){
                            Message message = Message.obtain();
                            message.what = 1004;
                            message.obj = "回复成功";
                            handler.sendMessage(message);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Message message  = Message.obtain();
                    message.what = 1001;
                    message.obj = "未连接到服务器";
                    handler.sendMessage(message);
                }
            }
        }.start();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayerStandard.backPress()){
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onResume() {
        super.onResume();
        Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
