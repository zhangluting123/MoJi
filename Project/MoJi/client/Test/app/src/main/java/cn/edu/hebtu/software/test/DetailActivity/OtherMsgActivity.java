package cn.edu.hebtu.software.test.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import cn.edu.hebtu.software.test.Activity.LoginInActivity;
import cn.edu.hebtu.software.test.Activity.MainActivity;
import cn.edu.hebtu.software.test.Activity.RegistActivity;
import cn.edu.hebtu.software.test.ChatIM.ChatActivity;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.LeftNavigation.MineActivity;
import cn.edu.hebtu.software.test.LeftNavigation.MyFootPrintActivity;
import cn.edu.hebtu.software.test.LeftNavigation.UserInfoActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;
import cn.edu.hebtu.software.test.Util.SQLiteUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.hyphenate.easeui.EaseConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @ProjectName:    MoJi
 * @Description:    个人详情页
 * @Author:         张璐婷
 * @CreateDate:     2020/5/15 11:12
 * @Version:        1.0
 */
public class OtherMsgActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView userName;
    private ImageView userHeader;
    private ImageView userSex;
    private TextView userJob;
    private TextView userSign;
    private Button btnOtherTrends;
    private Button btnSendMsg;
    private Button btnAddAttentaion;
    private User user;
    private String ip;
    private MyApplication data;
    private int tag;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    btnAddAttentaion.setText("取消关注");
                    tag=1;
                    break;
                case 1002:
                    btnAddAttentaion.setText("添加关注");
                    tag=0;
                    break;
                case 1003:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_msg);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        getViews();
        data = (MyApplication)getApplication();
        ip = data.getIp();

        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        userName.setText(user.getUserName());
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(this).load("http://" + ip + ":8080/MoJi/"+user.getUserHeadImg()).apply(options).into(userHeader);

        if("girl".equals(user.getSex())){
            userSex.setImageResource(R.mipmap.girl);
        }else if("boy".equals(user.getSex())){
            userSex.setImageResource(R.mipmap.boy);
        }
        userJob.setText(user.getOccupation());
        userSign.setText(user.getSignature());

        if(data.getUser()!=null){
            if(data.getUser().getUserId().equals(user.getUserId())){
                btnAddAttentaion.setText("myself");
            }else {
                checkFollow();
            }
        }

        //点击返回按钮返回主页面
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registerListener();
    }

    private void getViews(){
        toolbar = findViewById(R.id.mine_toolbar);
        userName = findViewById(R.id.tv_userName);
        userHeader = findViewById(R.id.iv_userHeader);
        userSex = findViewById(R.id.iv_userSex);
        userJob = findViewById(R.id.tv_userJob);
        userSign = findViewById(R.id.tv_userSign);
        btnAddAttentaion = findViewById(R.id.btn_add_attention);
        btnOtherTrends = findViewById(R.id.btn_other_trends);
        btnSendMsg = findViewById(R.id.btn_send_msg);
    }

    private void registerListener(){
        CustomOnClickListener listener = new CustomOnClickListener();
        btnAddAttentaion.setOnClickListener(listener);
        btnOtherTrends.setOnClickListener(listener);
        btnSendMsg.setOnClickListener(listener);
    }

    class CustomOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.btn_add_attention:
                    //TODO 添加关注 @author ming
                    //Log.e("******",btnAddAttentaion.toString());
                    if(!data.getUser().getUserId().equals(user.getUserId())){
                        if(tag==0){
                            if(data.getUser() == null){
                                Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                            }else {
                                if(data.getUser().getUserId().equals(user.getUserId())){
                                    //btnAddAttentaion.setText("myself");
                                }else {
                                    follow();
                                }
                            }
                        }else {
                            noFollow();
                        }
                    }
                    break;
                case R.id.btn_other_trends:
                    intent = new Intent(OtherMsgActivity.this, MyFootPrintActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    break;
                case R.id.btn_send_msg:
                    //发送消息
                    SQLiteUtil sqLiteUtil = new SQLiteUtil(getApplicationContext());
                    if(!sqLiteUtil.queryExistUser(user.getUserId())) {
                        sqLiteUtil.insert(user);
                    }
                    intent = new Intent(OtherMsgActivity.this, ChatActivity.class);
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, user.getUserId());
                    startActivity(intent);
                    break;
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    //@author ming
    private void follow() {
        new Thread() {
            @Override
            public void run() {
                try {
                    if(DetermineConnServer.isConnByHttp(getApplicationContext())) {
                        URL url = new URL("http://" + ip + ":8080/MoJi/user/follow?oneId="+data.getUser().getUserId()+"&twoId="+user.getUserId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String info = reader.readLine();
                        Message message = Message.obtain();

                        message.what = 1001;
                        message.obj = "new follow";

                        mHandler.sendMessage(message);
                    }else{
                        Message message = Message.obtain();
                        message.what = 1003;
                        message.obj = "未连接到服务器";
                        mHandler.sendMessage(message);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //@author ming
    private void checkFollow() {
        new Thread() {
            @Override
            public void run() {
                try {
                    if(DetermineConnServer.isConnByHttp(getApplicationContext())) {
                        URL url = new URL("http://" + ip + ":8080/MoJi/user/checkFollow?oneId="+data.getUser().getUserId()+"&twoId="+user.getUserId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String info = reader.readLine();
                        Message message = Message.obtain();

                        if ("followed".equals(info)) {
                            message.what = 1001;
                            message.obj = "followed";
                        }else {
                            message.what = 1002;
                            message.obj = "no record";
                        }
                        mHandler.sendMessage(message);
                    }else{
                        Message message = Message.obtain();
                        message.what = 1003;
                        message.obj = "未连接到服务器";
                        mHandler.sendMessage(message);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //@author ming
    private void noFollow() {
        new Thread() {
            @Override
            public void run() {
                try {
                    if(DetermineConnServer.isConnByHttp(getApplicationContext())) {
                        URL url = new URL("http://" + ip + ":8080/MoJi/user/noFollow?oneId="+data.getUser().getUserId()+"&twoId="+user.getUserId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String info = reader.readLine();
                        Message message = Message.obtain();

                        message.what = 1002;
                        message.obj = "delete done";

                        mHandler.sendMessage(message);
                    }else{
                        Message message = Message.obtain();
                        message.what = 1003;
                        message.obj = "未连接到服务器";
                        mHandler.sendMessage(message);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
