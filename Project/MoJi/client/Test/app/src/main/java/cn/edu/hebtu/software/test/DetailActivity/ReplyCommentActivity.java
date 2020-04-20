package cn.edu.hebtu.software.test.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.test.Adapter.ReplyCommentAdapter;
import cn.edu.hebtu.software.test.Data.Comment;
import cn.edu.hebtu.software.test.Data.ReplyComment;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.ActivityManager;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

/**
 * @ProjectName:    MoJi
 * @Description:    java类作用描述
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
    private Comment comment;
    private List<ReplyComment> replyComments;
    private MyApplication data;
    private String ip;
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

        Intent intent = getIntent();
        comment = intent.getParcelableExtra("comment");
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(this).load("http://" + ip + ":8080/MoJi/" + comment.getUser().getUserHeadImg()).apply(options).into(commentHead);
        commentContent.setText(comment.getCommentContent());
        userName.setText(comment.getUser().getUserName());
        commentDate.setText(comment.getCommentTime());
        replyNum.setText(comment.getReplyCount()+"");
        //获取回复的评论
        getReplyComment();
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/4/20  16:07
     *  @Description: 初始化回复评论
     */
    private void initReplyComment(){
        ListView listView = findViewById(R.id.lv_replyList);
        ReplyCommentAdapter adapter = new ReplyCommentAdapter(replyComments,getApplicationContext(),R.layout.item_reply_comment);
        listView.setAdapter(adapter);
    }

    private void getViews(){
        commentHead = findViewById(R.id.iv_commentHead);
        commentContent = findViewById(R.id.tv_commentContent);
        userName = findViewById(R.id.user_name);
        commentDate = findViewById(R.id.tv_commentDate);
        replyNum = findViewById(R.id.tool_replynum);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
