package cn.edu.hebtu.software.test.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Util.ActivityManager;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;
import cn.edu.hebtu.software.test.Util.PermissionUtils;
import cn.edu.hebtu.software.test.Util.SharedUtil;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * @ProjectName: MoJi
 * @Description: 登录页面
 * @Author: 李晓萌
 * @CreateDate: 2019/12/2 16:23
 * @Version: 1.0
 */

public class LoginInActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnSignIn;
    private EditText edtPhone;
    private EditText edtPwd;
    private ImageView ivEye;
    private TextView tvfgPwd;
    private TextView tvSignup;
    private boolean isHideFirst = true;//输入框密码是否是隐藏的，默认为true
    private MyApplication data;
    private String ip;

    private User user;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(LoginInActivity.this,"手机号或密码不正确",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    user = (User) msg.obj;
                    final MyApplication data = (MyApplication) getApplication();
                    data.setUser(user);
                    JPushInterface.init(getApplicationContext());
                    JPushInterface.setDebugMode(true);
                    //登陆成功,如果退出登录时已关闭推送功能，则再次登录时恢复此功能
                    if(JPushInterface.isPushStopped(getApplicationContext())){
                        JPushInterface.resumePush(getApplicationContext());
                    }

                    if(!"success".equals(SharedUtil.getString("isGuide", getApplicationContext(), user.getUserId()))){
                        // 调用 Handler 来异步设置别名，一般都是用userId来进行设置别名（唯一性）。
                        JPushInterface.setAlias(getApplicationContext(),user.getUserId() ,mAliasCallback);
                    }
                    Intent in = new Intent(LoginInActivity.this,MainActivity.class);
                    startActivity(in);
                    finish();
                    break;
                case 3:
                    Toast.makeText(LoginInActivity.this, "未连接到服务器", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    if(!"success".equals(SharedUtil.getString("isGuide", getApplicationContext(), user.getUserId()))){
                        // 调用 Handler 来异步设置别名，一般都是用userId来进行设置别名（唯一性）。
                        JPushInterface.setAlias(getApplicationContext(),user.getUserId() ,mAliasCallback);
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginin);
        ActivityManager.getInstance().addActivity(this);
        PermissionUtils.openNotifiPermission(this);

        data = (MyApplication) getApplication();
        ip = data.getIp();
        data.setMsgPermission("open");

        getViews();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        }

        ivEye.setOnClickListener(this);
        ivEye.setImageResource(R.mipmap.biyanjing);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btnSignIn:
                        String phone = edtPhone.getText().toString().trim();
                        String pwd = edtPwd.getText().toString().trim();
                        if(pwd.length() < 6) {
                            Toast.makeText(getApplicationContext(), "密码少于6位", Toast.LENGTH_SHORT).show();
                        }else if(pwd.length() > 12){
                            Toast.makeText(getApplicationContext(), "密码多于12位", Toast.LENGTH_SHORT).show();
                        }else{
                            login(phone,pwd);
                        }
                }
            }
        });


        //TextView的事件回调函数
        tvfgPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginInActivity.this,RegistActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getViews() {
        edtPhone = findViewById(R.id.edt_phone);
        btnSignIn = findViewById(R.id.btnSignIn);
        edtPwd = findViewById(R.id.edt_pwd);
        ivEye = findViewById(R.id.iv_eye);
        tvfgPwd = findViewById(R.id.tv_fgpwd);
        tvSignup = findViewById(R.id.tv_signup);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_eye:
                if (isHideFirst == true) {
                    ivEye.setImageResource(R.mipmap.yanjing);
                    //密文
                    HideReturnsTransformationMethod method1 = HideReturnsTransformationMethod.getInstance();
                    edtPwd.setTransformationMethod(method1);
                    isHideFirst = false;
                } else {
                    ivEye.setImageResource(R.mipmap.biyanjing);
                    //密文
                    TransformationMethod method = PasswordTransformationMethod.getInstance();
                    edtPwd.setTransformationMethod(method);
                    isHideFirst = true;

                }
                // 光标的位置
                int index = edtPwd.getText().toString().length();
                edtPwd.setSelection(index);
                break;

        }
    }

    /**
     *  @author 春波
     *  @time 2019/12/10  16:44
     *  @Description：登录
     */
    private void login(String phone, String pwd) {
        new Thread(){
            public void run(){
                try {
                    Message msg = Message.obtain();
                    if(DetermineConnServer.isConnByHttp(LoginInActivity.this)){
                        User threadUser = new User();
                        URL url = new URL("http://" + ip + ":8080/MoJi/user/login?phone=" + phone + "&password=" + pwd);
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String str = reader.readLine();
                        if("0".equals(str)){
                            msg.what = 1;
                        }else {
                            Gson gson = new Gson();
                            Log.e("user=", str);
                            threadUser = gson.fromJson(str, User.class);
                            msg.what = 2;
                            msg.obj = threadUser;
                        }
                    }else {
                        msg.what = 3;
                    }
                    handler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     *  @author: 张璐婷
     *  @time: 2019/12/12  9:18
     *  @Description: 设置别名回调
     */
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            Log.e("setAliasCallback_code", code + "");
            switch (code) {
                case 0:
                    // 这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    SharedUtil.putString("isGuide",LoginInActivity.this, user.getUserId(), "success");
                    break;
                case 6002:
                    handler.sendMessageDelayed(handler.obtainMessage(4, alias), 1000 * 1);
                    break;
                default:
                    break;
            }
        }
    };

}
