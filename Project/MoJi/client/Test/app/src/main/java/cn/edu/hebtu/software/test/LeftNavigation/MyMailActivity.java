package cn.edu.hebtu.software.test.LeftNavigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTabHost;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.test.Activity.MainActivity;
import cn.edu.hebtu.software.test.Adapter.TraceListAdapter;
import cn.edu.hebtu.software.test.Data.Mail;
import cn.edu.hebtu.software.test.DetailActivity.MailDetailActivity;
import cn.edu.hebtu.software.test.Fragment.DropsFragment;
import cn.edu.hebtu.software.test.Fragment.FootprintFragment;
import cn.edu.hebtu.software.test.Fragment.MessageFragment;
import cn.edu.hebtu.software.test.Fragment.NotificationFragment;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToDoubleBiFunction;
/**
 * @ProjectName:    MoJi
 * @Description:    java类作用描述
 * @Author:         张璐婷
 * @CreateDate:     2020/4/11 10:55
 * @Version:        1.0
 */
public class MyMailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private int mailId;
    private MyApplication data;
    private Map<String, TextView> textViewMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mail);
        ActivityManager.getInstance().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.MyTheam_color));

        }

        toolbar = findViewById(R.id.myMail_toolbar);
        data = (MyApplication)getApplication();
        mailId = data.getMailTabId();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent response = new Intent(MyMailActivity.this, MainActivity.class);
                response.putExtra("flag",true);
                startActivity(response);
                overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                finish();
            }
        });


        FragmentTabHost fragmentTabHost = findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this,
                getSupportFragmentManager(),//FragmentManager对象用来管理多个Fragment
                android.R.id.tabcontent);//真正显示内容页面的容器的id

        //私信
        TabHost.TabSpec message = fragmentTabHost.newTabSpec("message").setIndicator(getTabSpecView("message","私信"));
        fragmentTabHost.addTab(message, MessageFragment.class, null);
        //通知
        TabHost.TabSpec notification = fragmentTabHost.newTabSpec("notification").setIndicator(getTabSpecView("notification","通知"));
        fragmentTabHost.addTab(notification, NotificationFragment.class, null);

        //默认选中第一项
        fragmentTabHost.setCurrentTab(mailId);
        switch (mailId){
            case 0:
                textViewMap.get("message").setTextColor(getResources().getColor(R.color.MyTheam_color));
                break;
            case 1:
                textViewMap.get("notification").setTextColor(getResources().getColor(R.color.MyTheam_color));
                break;
        }

        //切换选项卡的事件监听器
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId){
                    case "message":
                        data.setMailTabId(0);
                        textViewMap.get("message").setTextColor(getResources().getColor(R.color.MyTheam_color));
                        textViewMap.get("notification").setTextColor(Color.BLACK);
                        break;
                    case "notification":
                        data.setMailTabId(1);
                        textViewMap.get("notification").setTextColor(getResources().getColor(R.color.MyTheam_color));
                        textViewMap.get("message").setTextColor(Color.BLACK);
                        break;
                }
            }
        });

    }

    private View getTabSpecView(String tag, String title) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.mail_tabspec_layout,null);

        TextView textView = view.findViewById(R.id.tv_content);
        textView.setText(title);

        textViewMap.put(tag,textView);
        return view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
