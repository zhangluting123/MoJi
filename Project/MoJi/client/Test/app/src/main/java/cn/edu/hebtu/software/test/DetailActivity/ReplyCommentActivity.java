package cn.edu.hebtu.software.test.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.test.Adapter.ReplyCommentAdapter;
import cn.edu.hebtu.software.test.Data.Comment;
import cn.edu.hebtu.software.test.Data.ReplyComment;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.ActivityManager;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;
import cn.edu.hebtu.software.test.Util.SoftKeyBoardListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mob.tools.RxMob;

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

import static cn.edu.hebtu.software.test.R.id.btn_submitComment;

/**
 * @ProjectName:    MoJi
 * @Description:    回复评论
 * @Author:         张璐婷
 * @CreateDate:     2020/4/20 15:43
 * @Version:        1.0
 */
public class ReplyCommentActivity extends AppCompatActivity {
    private ImageView commentHead;
    private TextView commentContent;
    private TextView userName;
    private TextView commentDate;
    private TextView replyNum;
    private EditText edtInsertComment;
    private Button btnSubmitComment;
    private Comment comment;
    private String parentId;
    private int pos;
    private List<ReplyComment> replyComments;
    private ReplyCommentAdapter adapter;
    private MyApplication data;
    private String ip;
    private User user;
    private int noteType;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(getApplicationContext(), (CharSequence) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    replyComments = (List<ReplyComment>) msg.obj;
                    initReplyComment();
                    break;
                case 1003:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    refresh(false);
                    break;
                case 1004:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    refresh(true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_comment);

        ActivityManager.getInstance().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#22000000"));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        }

        Toolbar toolbar = findViewById(R.id.reply_comment_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                finish();
            }
        });

        getViews();
        data = (MyApplication)getApplication();
        ip = data.getIp();
        user = data.getUser();

        Intent intent = getIntent();
        comment = intent.getParcelableExtra("comment");
        noteType = intent.getIntExtra("noteType",0);
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(this).load("http://" + ip + ":8080/MoJi/" + comment.getUser().getUserHeadImg()).apply(options).into(commentHead);
        commentContent.setText(comment.getCommentContent());
        userName.setText(comment.getUser().getUserName());
        commentDate.setText(comment.getCommentTime());
        replyNum.setText(comment.getReplyCount()+"");
        //获取回复的评论
        getReplyComment();

        //点击头像
        commentHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OtherMsgActivity.class);
                intent.putExtra("user",comment.getUser());
                startActivity(intent);
            }
        });

        //插入评论
        btnSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != user.getUserId()) {
                    String content = edtInsertComment.getText().toString().trim();
                    if (content.length() == 0) {
                        Toast.makeText(ReplyCommentActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        if(edtInsertComment.getHint().equals("请输入评论")) {
                            //回复评论
                            insertComment();
                        }else{
                            //回复的回复
                            insertReplyComment();
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/4/20  16:07
     *  @Description: 初始化回复评论
     */
    private void initReplyComment(){
        ListView listView = findViewById(R.id.lv_replyList);
        adapter = new ReplyCommentAdapter(replyComments,getApplicationContext(),R.layout.item_reply_comment);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edtInsertComment.setHint("回复@"+replyComments.get(position).getReplyUser().getUserName());
                edtInsertComment.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
                parentId = replyComments.get(position).getReplyId();
                pos = position;
                edtInsertComment.setSelection(0);
                edtInsertComment.setFocusable(true);
                //键盘如果关闭弹出
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    private void getViews(){
        commentHead = findViewById(R.id.iv_commentHead);
        commentContent = findViewById(R.id.tv_commentContent);
        userName = findViewById(R.id.user_name);
        commentDate = findViewById(R.id.tv_commentDate);
        replyNum = findViewById(R.id.tool_replynum);
        edtInsertComment = findViewById(R.id.edt_insertComment);
        btnSubmitComment = findViewById(R.id.btn_submitComment);
    }

    private void refresh(boolean flag){
        int a = comment.getReplyCount();
        comment.setReplyCount(a++);
        replyNum.setText(a+"");
        ReplyComment replyComment = new ReplyComment();
        replyComment.setReplyUser(data.getUser());
        replyComment.setReplyContent(edtInsertComment.getText().toString().trim());
        replyComment.setReplyTime("今天");
        if(flag){
            replyComment.setReplyComment(replyComments.get(pos));
        }
        replyComments.add(replyComment);
        adapter.flush(replyComments);
        clearInput();
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
        SoftKeyBoardListener.setListener(ReplyCommentActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
            }

            @Override
            public void keyBoardHide(int height) {
                if(!edtInsertComment.getHint().equals("请输入评论")) {
                    edtInsertComment.setHint("请输入评论");
                }
            }
        });
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/4/20  15:43
     *  @Description: 获取回复的评论
     */
    private void getReplyComment(){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getApplicationContext())){
                    try {
                        List<ReplyComment> replyComments = new ArrayList<>();
                        URL url = new URL("http://"+ip+":8080/MoJi/replyComment/list?commentId="+comment.getId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String str = null;
                        if((str = reader.readLine()) != null){
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<ReplyComment>>(){}.getType();
                            replyComments = gson.fromJson(str,type);
                        }
                        Message message = Message.obtain();
                        message.what = 1002;
                        message.obj = replyComments;
                        handler.sendMessage(message);
                        in.close();
                        reader.close();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Message msg = Message.obtain();
                    msg.what = 1001;
                    msg.obj = "未连接到服务器";
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

   /**
    *  @author: 张璐婷
    *  @time: 2020/4/24  21:37
    *  @Description: 插入回复的评论
    */
    private void insertComment(){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getApplicationContext())){
                    try {
                        URL url = new URL("http://"+ip+":8080/MoJi/replyComment/add?replyContent="+edtInsertComment.getText().toString()+"&commentId="+comment.getId()+"&replyUserId="+data.getUser().getUserId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        if(reader.readLine().equals("OK")){
                            Message message = Message.obtain();
                            message.what = 1003;
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

    /**
     *  @author: 张璐婷
     *  @time: 2020/4/24  21:39
     *  @Description: 回复的回复
     */
    private void insertReplyComment(){

        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getApplicationContext())){
                    try {
                        URL url = new URL("http://"+ip+":8080/MoJi/replyComment/addReplyToReply?replyContent="+edtInsertComment.getText().toString()+"&commentId="+comment.getId()+"&replyUserId="+data.getUser().getUserId()+"&parentId="+parentId+"&ifVideo="+noteType);
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
}
