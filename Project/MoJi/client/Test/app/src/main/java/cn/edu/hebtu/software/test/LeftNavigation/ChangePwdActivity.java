package cn.edu.hebtu.software.test.LeftNavigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;

import android.content.Intent;
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
 * @Description:    java类作用描述
 * @Author:         张璐婷
 * @CreateDate:     2019/11/27 15:41
 * @Version:        1.0
 */
public class ChangePwdActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Button btnSubmit;
    private EditText oldPwd;
    private EditText newPwd;
    private EditText confirmPwd;
    private int tabId;
    private String ip;
    private MyApplication data;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.MyTheam_color));

        }

        getViews();
        data = (MyApplication)getApplication();
        ip = data.getIp();
        Intent intent = getIntent();
        tabId = intent.getIntExtra("tab",0);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent response = new Intent(ChangePwdActivity.this, SettingActivity.class);
                response.putExtra("tab", tabId);
                startActivity(response);
                overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                finish();
            }
        });


        /**
         *  @author: 王佳成
         *  @time: 2019/12/15  15:42
         *  @Description: 修改密码
         */
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldPwd.getText().toString().equals(data.getUser().getPassword())){
                    if(newPwd.getText().toString().equals(confirmPwd.getText().toString())){
                        sendToServer();
                    }else{
                        Toast.makeText(ChangePwdActivity.this,"密码输入不一致！", Toast.LENGTH_SHORT).show();
                        confirmPwd.requestFocus();
                    }
                }else{
                    Toast.makeText(ChangePwdActivity.this, "原密码不正确!", Toast.LENGTH_SHORT).show();
                    oldPwd.requestFocus();
                }
            }
        });
    }

    private void getViews(){
        toolbar = findViewById(R.id.pwd_toolbar);
        btnSubmit = findViewById(R.id.btnSubmit);
        oldPwd = findViewById(R.id.et_oldPwd);
        newPwd = findViewById(R.id.et_newPwd);
        confirmPwd = findViewById(R.id.et_confirmPwd);
    }

    private void sendToServer() {
        new Thread() {
            @Override
            public void run() {
                try {
                    if(DetermineConnServer.isConnByHttp(getApplicationContext())) {
                        URL url = new URL("http://" + ip + ":8080/MoJi/ChangeUserPwdServlet?newPwd=" + newPwd.getText().toString() + "&userId=" + data.getUser().getUserId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String info = reader.readLine();
                        Message msg = Message.obtain();
                        msg.what = 1001;
                        if("OK".equals(info)){
                            msg.obj = "修改成功";
                        }else{
                            msg.obj = "修改失败";
                        }
                        mHandler.sendMessage(msg);
                    }else{
                        Message msg = Message.obtain();
                        msg.what = 1001;
                        msg.obj = "未连接到服务器";
                        mHandler.sendMessage(msg);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
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
