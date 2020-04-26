package cn.edu.hebtu.software.test.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.test.Data.Comment;
import cn.edu.hebtu.software.test.Data.MailMyComment;
import cn.edu.hebtu.software.test.Data.ReplyComment;
import cn.edu.hebtu.software.test.LeftNavigation.MyMailActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
/**
 * @ProjectName:    MoJi
 * @Description:    回复评论页
 * @Author:         张璐婷
 * @CreateDate:     2020/4/25 14:59
 * @Version:        1.0
 */
public class MailReplyActivity extends AppCompatActivity {
    private MyApplication data;
    private String ip;
    private TextView userName;
    private Button btnSubmit;
    private EditText insertComment ;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_reply);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.MyTheam_color));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        }

        data = (MyApplication)getApplication();
        ip = data.getIp();

        Toolbar toolbar = findViewById(R.id.reply_comment_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                finish();
            }
        });

        Intent intent = getIntent();
        MailMyComment mailMyComment = intent.getParcelableExtra("mailMyComment");
        userName = findViewById(R.id.tv_reply_userName);
        btnSubmit = findViewById(R.id.btnSubmit);
        insertComment = findViewById(R.id.edt_insertComment);

        if(mailMyComment.getCrFlag() == 'C'){
            userName.setText(mailMyComment.getComment().getUser().getUserName());
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replyComment(mailMyComment.getComment());
                }
            });
        }else if(mailMyComment.getCrFlag() == 'R'){
            userName.setText(mailMyComment.getReplyComment().getReplyUser().getUserName());
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replyToReply(mailMyComment.getReplyComment());
                }
            });
        }
    }
    /**
     *  @author: 张璐婷
     *  @time: 2020/4/25  13:40
     *  @Description: 回复有关动态的评论
     *  同DropsDetailActivity 的 insertReplyComment()功能相同
     */
    private void replyComment(Comment comment){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getApplicationContext())){
                    try {
                        URL url = new URL("http://"+ip+":8080/MoJi/replyComment/add?replyContent="+insertComment.getText().toString()+"&commentId="+comment.getId()+"&replyUserId="+data.getUser().getUserId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        if(reader.readLine().equals("OK")){
                            Message message = Message.obtain();
                            message.what = 1001;
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
     *  @time: 2020/4/25  13:42
     *  @Description: 回复评论我评论的人
     *  同ReplyCommentActivity 的 insertReplyComment()作用相同
     */
    private void replyToReply(ReplyComment replyComment){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getApplicationContext())){
                    try {
                        URL url = new URL("http://"+ip+":8080/MoJi/replyComment/addReplyToReply?replyContent="+insertComment.getText().toString()+"&commentId="+replyComment.getComment().getId()+"&replyUserId="+data.getUser().getUserId()+"&parentId="+replyComment.getReplyId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        if(reader.readLine().equals("OK")){
                            Message message = Message.obtain();
                            message.what = 1001;
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
