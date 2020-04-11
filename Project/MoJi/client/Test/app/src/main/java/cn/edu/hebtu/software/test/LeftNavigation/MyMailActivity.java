package cn.edu.hebtu.software.test.LeftNavigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.test.Activity.MainActivity;
import cn.edu.hebtu.software.test.Adapter.TraceListAdapter;
import cn.edu.hebtu.software.test.Data.Mail;
import cn.edu.hebtu.software.test.DetailActivity.MailDetailActivity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleBiFunction;
/**
 * @ProjectName:    MoJi
 * @Description:    我的消息
 * @Author:         邸凯扬
 * @CreateDate:     2019/12/16 17:33
 * @Version:        1.0
 */
public class MyMailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private int tabId;
    private RecyclerView rvTrace;

    public static List<Mail> mailList;
    private TraceListAdapter adapter;
    private MyApplication data;
    private String ip;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    mailList = (List<Mail>)msg.obj;
                    initData();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.MyTheam_color));

        }

        data = (MyApplication)getApplication();
        ip = data.getIp();

        toolbar = findViewById(R.id.myMail_toolbar);
        Intent request= getIntent();
        tabId = request.getIntExtra("tab", 0);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent response = new Intent(MyMailActivity.this, MainActivity.class);
                response.putExtra("flag",true);
                response.putExtra("tab", tabId);
                startActivity(response);
                overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                finish();
            }
        });


        findView();
        sendToServer();

    }

    private void findView() {
        rvTrace = findViewById(R.id.rvTrace);
    }

    private  void initData() {
        adapter = new TraceListAdapter(this, mailList);
        rvTrace.setLayoutManager(new LinearLayoutManager(this));
        rvTrace.setAdapter(adapter);
        adapter.setOnItemClickListener(new TraceListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MyMailActivity.this, MailDetailActivity.class);
                Gson gson = new Gson();
                String str = gson.toJson(mailList.get(position));
                intent.putExtra("extra",str );
                startActivity(intent);
            }
        });
    }

    private void sendToServer() {
        new Thread() {
            @Override
            public void run() {

                try {
                    if(DetermineConnServer.isConnByHttp(getApplicationContext())) {
                        List<Mail> list = new ArrayList<>();
                        URL url = new URL("http://" + ip + ":8080/MoJi/QueryMailServlet?userId=" + data.getUser().getUserId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String info = reader.readLine();
                        if (info != null) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Mail>>(){}.getType();
                            list = gson.fromJson(info, type);
                            Message msg = new Message();
                            msg.what = 1002;
                            msg.obj = list;
                            handler.sendMessage(msg);
                        }
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
