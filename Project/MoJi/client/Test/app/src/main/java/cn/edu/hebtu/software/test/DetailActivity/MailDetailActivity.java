package cn.edu.hebtu.software.test.DetailActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.test.LeftNavigation.MyMailActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Map;

/**
 *  @author: 张璐婷
 *  @time: 2019/12/12  10:38
 *  @Description: 评论页
 */
public class MailDetailActivity extends AppCompatActivity {
    private Button btnDelete;
    private TextView myName;
    private TextView otherName;
    private TextView commentContent;
    private TextView commentTime;
    private Toolbar toolbar;
    private MyApplication data;
    private String ip;
    private String mailId;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MailDetailActivity.this, MyMailActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                    finish();
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_item);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.MyTheam_color));

        }

        data = (MyApplication)getApplication();
        ip = data.getIp();
        getViews();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToServer();
            }
        });

        Intent intent = getIntent();
        String extra = intent.getStringExtra("extra");
        if(null != extra){
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String,String>>(){}.getType();
            Map<String,String> map = gson.fromJson(intent.getStringExtra("extra"), type);
            Log.e("extra", extra);
            if(map.get("mailId") != null) {
                mailId = map.get("mailId");
            }
            myName.setText(data.getUser().getUserName());
            otherName.setText(map.get("otherName"));
            commentContent.setText(map.get("commentContent"));
            commentTime.setText(map.get("acceptTime"));
        }
    }

    private void getViews() {
        toolbar = findViewById(R.id.mailItem_toolbar);
        btnDelete = findViewById(R.id.btnDelete);
        myName = findViewById(R.id.tv_myName);
        otherName = findViewById(R.id.tv_otherName);
        commentContent = findViewById(R.id.tv_commentContent);
        commentTime = findViewById(R.id.tv_commentTime);
    }

    private void sendToServer() {
        new Thread() {
            @Override
            public void run() {

                try {
                    if(DetermineConnServer.isConnByHttp(getApplicationContext())) {
                        URL url = new URL("http://" + ip + ":8080/MoJi/DeleteMailServlet?mailId=" + mailId);
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String info = reader.readLine();
                        Message msg = new Message();
                        if ("OK".equals(info)) {
                            msg.what = 1002;
                            msg.obj = "删除成功";
                        }else if("ERROR".equals(info)){
                            msg.what = 1001;
                            msg.obj = "删除失败";
                        }
                        handler.sendMessage(msg);
                    }else{
                        Message msg = new Message();
                        msg.what = 1001;
                        msg.obj = "未连接到服务器";
                        handler.sendMessage(msg);
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
