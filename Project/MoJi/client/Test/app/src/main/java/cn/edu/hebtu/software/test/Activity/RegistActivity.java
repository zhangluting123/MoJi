package cn.edu.hebtu.software.test.Activity;

import androidx.appcompat.app.AppCompatActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class RegistActivity extends AppCompatActivity {
    private EditText edtTel;
    private EditText edtCode;
    private EditText edtPwd;
    private EditText edtPwd2;
    private Button btnCode;
    private Button btnReg;
    private String ip;

    private EventHandler handler;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistActivity.this,LoginInActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 1002:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        getViews();
        register();
        MyApplication data = (MyApplication)getApplication();
        ip = data.getIp();

        handler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE){
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sendToServer();
                                edtCode.requestFocus();
                            }
                        });

                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegistActivity.this,"验证码已发送",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else{
                    ((Throwable)data).printStackTrace();
                    Throwable throwable = (Throwable) data;
                    try {
                        JSONObject obj = new JSONObject(throwable.getMessage());
                        final String des = obj.optString("detail");
                        if (!TextUtils.isEmpty(des)){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegistActivity.this,"提交错误信息", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        SMSSDK.registerEventHandler(handler);
    }


    private void getViews() {
        edtTel = findViewById(R.id.edtTel);
        edtCode = findViewById(R.id.edtCode);
        edtPwd = findViewById(R.id.edtPwd);
        edtPwd2 = findViewById(R.id.edtPwd2);
        btnCode = findViewById(R.id.btnCode);
        btnReg = findViewById(R.id.btnReg);

    }
    private void register(){
        CustomOnClickListener listener = new CustomOnClickListener();
        btnCode.setOnClickListener(listener);
        btnReg.setOnClickListener(listener);
    }

    class CustomOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnCode:
                    if (edtTel.getText().toString().length() ==11){
                        play();
                    }else{
                        Toast.makeText(RegistActivity.this,"请输入正确的手机号！",Toast.LENGTH_SHORT).show();
                        edtTel.requestFocus();
                    }
                    break;
                case R.id.btnReg:
                    if (edtPwd.getText().toString().equals(edtPwd2.getText().toString()) ){
                        tijiao();
                    }else{
                        Toast.makeText(RegistActivity.this,"两次输入密码不一致！",Toast.LENGTH_SHORT).show();
                        edtPwd2.requestFocus();
                    }
                    break;
            }
        }
    }
    //获取验证码
    public void play() {
        SMSSDK.getVerificationCode("86",edtTel.getText().toString());
    }

    //后台验证
    public void tijiao() {
        SMSSDK.submitVerificationCode("86",edtTel.getText().toString(),edtCode.getText().toString());
    }

    private void sendToServer() {
        new Thread() {
            @Override
            public void run() {
                try {
                    if(DetermineConnServer.isConnByHttp(getApplicationContext())) {
                        URL url = new URL("http://" + ip + ":8080/MoJi/RegistUserServlet?phone=" + edtTel.getText().toString() + "&password=" + edtPwd.getText().toString());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String info = reader.readLine();
                        Message message = Message.obtain();

                        if ("OK".equals(info)) {
                            message.what = 1001;
                        }else if("Regist".equals(info)){
                            message.what = 1002;
                            message.obj = "该手机号已被注册";
                        }
                        mHandler.sendMessage(message);
                    }else{
                        Message message = Message.obtain();
                        message.what = 1002;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(handler);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
