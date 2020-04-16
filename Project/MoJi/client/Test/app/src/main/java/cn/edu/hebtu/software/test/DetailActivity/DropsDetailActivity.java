package cn.edu.hebtu.software.test.DetailActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import cn.edu.hebtu.software.test.Adapter.CommentAdapter;
import cn.edu.hebtu.software.test.Adapter.GuidePageAdapter;
import cn.edu.hebtu.software.test.Data.Comment;
import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Util.ActivityManager;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;
import cn.edu.hebtu.software.test.Util.SoftKeyBoardListener;
import cn.edu.hebtu.software.test.View.InnerScrollListView;

/**
 * @ProjectName: MoJi
 * @Description: 点滴详情页
 * @Author: 李晓萌
 * @CreateDate: 2019/12/2 16:23
 * @Version: 1.0
 */

public class DropsDetailActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    private RelativeLayout rlhead;
    private TextView userName;
    private TextView noteTime;
    private ImageView headImg;
    private TextView noteAddress;
    private TextView noteContent;
    private TextView commentCount;
    private EditText edtInsertComment;
    private Button btnSubmitComment;
    private LinearLayout noComment;
    private Note note;
    private List<Comment> commentList;
    private RequestOptions options = new RequestOptions().circleCrop();

    private ViewPager vp;
    private List<View> viewList;//图片资源的集合
    private ViewGroup vg;//放置圆点

    //实例化圆点View
    private ImageView point;
    private ImageView[] ivPointArray;

    private LinearLayout.LayoutParams layoutParams;
    private PopupWindow popupWindow;

    private MyApplication data;
    private String ip;
    private User user;

    private CommentAdapter commentAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    edtInsertComment.setText("");
                    break;
                case 1002:
                    commentList = (List<Comment>)msg.obj;
                    initComment();
                    break;
                case 1003:
                    Comment comment = new Comment();
                    user = data.getUser();
                    comment.setUser(user);
                    comment.setCommentContent(edtInsertComment.getText().toString().trim());
                    comment.setCommentTime("今天");
                    commentList.add(comment);
                    if(commentList.size() <= 0) {
                        noComment.setVisibility(View.GONE);
                    }
                    commentAdapter.flush(commentList);

            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropsdetail);

        ActivityManager.getInstance().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#22000000"));
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
        note = bundle.getParcelable("note");
        noteTime.setText(note.getTime());
        Glide.with(this).load("http://"+ip+":8080/MoJi/"+note.getUser().getUserHeadImg()).apply(options).into(headImg);
        noteAddress.setText(note.getLocation());
        noteContent.setText(note.getContent());
        userName.setText(note.getUser().getUserName());


        //加载ViewPager
        initViewPager();

        //加载底部圆点
        initPoint();

        //获得评论
        getComments();

        //插入评论
        btnSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != user.getUserId()) {
                    String content = edtInsertComment.getText().toString().trim();
                    if (content.length() == 0) {
                        Toast.makeText(DropsDetailActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        if(edtInsertComment.getHint().equals("请输入评论")) {
                            insertComment(content);
                        }else{
                            //TODO
                            Toast.makeText(getApplicationContext(), "回复评论---", Toast.LENGTH_SHORT).show();
                            edtInsertComment.setHint("请输入评论");
                            edtInsertComment.setText("");
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void getViews() {
        rlhead = findViewById(R.id.rl_head);//轮播图
        userName = findViewById(R.id.tv_userName);
        noteTime = findViewById(R.id.tv_noteTime);
        headImg = findViewById(R.id.iv_headImg);
        noteAddress = findViewById(R.id.tv_noteAddress);
        noteContent = findViewById(R.id.iv_noteContent);
        commentCount = findViewById(R.id.tv_commentCount);
        edtInsertComment = findViewById(R.id.edt_insertComment);
        btnSubmitComment = findViewById(R.id.btn_submitComment);
        noComment = findViewById(R.id.ll_noComment);
        vp = findViewById(R.id.vp_glide);
        vg = findViewById(R.id.guide_ll_point);
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
        commentAdapter = new CommentAdapter( commentList, R.layout.item_comment, getApplicationContext());
        listView.setAdapter(commentAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtInsertComment.setHint("回复@"+commentList.get(position).getUser().getUserName());
                edtInsertComment.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
                edtInsertComment.setSelection(0);
                edtInsertComment.setFocusable(true);
                //弹出键盘
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

                    Toast.makeText(getApplicationContext(), "分享评论", Toast.LENGTH_SHORT).show();
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
     *  @time: 2020/4/16  8:44
     *  @Description: 监听键盘是否弹起
     */
    private void onKeyBoardListener(){
        SoftKeyBoardListener.setListener(DropsDetailActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
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

    /**
     * 加载底部圆点
     */
    private void initPoint() {
        //根据ViewPager的item数量实例化数组
        ivPointArray = new ImageView[viewList.size()];
        //循环新建底部圆点ImageView，将生成的ImageView保存到数组中
        int size = note.getImgList().size();
        for (int i = 0;i<size;i++){
            point = new ImageView(this);
            layoutParams = new LinearLayout.LayoutParams(30,30);

            //第一个页面需要设置为选中状态，这里采用两张不同的图片
            if (i == 0){
                point.setBackgroundResource(R.mipmap.ic_page_indicator_focused);
            }else{
                layoutParams.leftMargin=20;
                point.setBackgroundResource(R.mipmap.ic_page_indicator);
            }
            point.setLayoutParams(layoutParams);
            point.setPadding(40,0,40,0);//left,top,right,bottom
            ivPointArray[i] = point;

            //将数组中的ImageView加入到ViewGroup
            vg.addView(ivPointArray[i]);
        }
    }

    /**
     * 加载图片ViewPager
     */
    private void initViewPager() {

        viewList = new ArrayList<>();
        if(note.getImgList().size() <= 0){
            ImageView imageView = new ImageView(this);
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(R.mipmap.default_bg)
                    .into(new SimpleTarget<Bitmap>() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Drawable drawable = new BitmapDrawable(resource);
                            imageView.setBackground(drawable);    //设置背景
                        }
                    });

            //将ImageView加入到集合中
            viewList.add(imageView);
        }else{
            for (int i = 0;i<note.getImgList().size();i++){
                Log.e("img=", note.getImgList().get(i));
                //new ImageView并设置图片资源
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load("http://"+ ip +":8080/MoJi/"+note.getImgList().get(i))
                        .into(imageView);

                //将ImageView加入到集合中
                viewList.add(imageView);
            }
        }


        //View集合初始化好后，设置Adapter
        vp.setAdapter(new GuidePageAdapter(viewList));
        //设置滑动监听
        vp.setOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 滑动后的监听
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        //循环设置当前页的标记图
        int length = note.getImgList().size();
        for (int i = 0;i<length;i++){
            ivPointArray[position].setBackgroundResource(R.mipmap.ic_page_indicator_focused);
            if (position != i){
                ivPointArray[i].setBackgroundResource(R.mipmap.ic_page_indicator);
            }
        }

    }
    @Override
    public void onPageScrollStateChanged(int state) {

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
                        URL url = new URL("http://"+ip+":8080/MoJi/comment/add?noteId="+note.getNoteId()+"&userId="+data.getUser().getUserId()+"&commentContent="+content);
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String str = reader.readLine();
                        Message msg = Message.obtain();
                        msg.what = 1001;
                        if(str != null ){
                            if("2".equals(str)){
                                msg.obj = "评论发布成功";
                                handler.sendMessage(Message.obtain(handler,1003));
                            }else if("1".equals(str)){
                                msg.obj = "评论发布成功[未发送通知]";
                                handler.sendMessage(Message.obtain(handler,1003));
                            }
                        }else{
                            msg.obj = "评论发布失败";
                            handler.sendMessage(Message.obtain(handler,1001));
                        }

                        in.close();
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Message message  = Message.obtain();
                    message.obj = "未连接到服务器";
                    handler.sendMessage(Message.obtain(handler,1001));
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
                        URL url = new URL("http://"+ip+":8080/MoJi/comment/list?noteId="+note.getNoteId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String str = null;
                        while((str = reader.readLine()) != null){
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
