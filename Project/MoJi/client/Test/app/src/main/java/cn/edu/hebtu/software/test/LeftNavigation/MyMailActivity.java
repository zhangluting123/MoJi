package cn.edu.hebtu.software.test.LeftNavigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTabHost;
import cn.edu.hebtu.software.test.Activity.MainActivity;
import cn.edu.hebtu.software.test.Fragment.MyCommentFragment;
import cn.edu.hebtu.software.test.Fragment.MessageFragment;
import cn.edu.hebtu.software.test.Fragment.NotificationFragment;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.ActivityManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

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
        //评论
        TabHost.TabSpec mycomment = fragmentTabHost.newTabSpec("mycomment").setIndicator(getTabSpecView("mycomment","评论"));
        fragmentTabHost.addTab(mycomment, MyCommentFragment.class, null);
        //通知
        TabHost.TabSpec notification = fragmentTabHost.newTabSpec("notification").setIndicator(getTabSpecView("notification","通知"));
        fragmentTabHost.addTab(notification, NotificationFragment.class, null);

        //默认选中第一项
        fragmentTabHost.setCurrentTab(mailId);
        switch (mailId){
            case 0:
                textViewMap.get("message").setTextColor(getResources().getColor(R.color.colorActionBarBackground));
                break;
            case 1:
                textViewMap.get("mycomment").setTextColor(getResources().getColor(R.color.colorActionBarBackground));
                break;
            case 2:
                textViewMap.get("notification").setTextColor(getResources().getColor(R.color.colorActionBarBackground));
                break;
        }

        //切换选项卡的事件监听器
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId){
                    case "message":
                        data.setMailTabId(0);
                        textViewMap.get("message").setTextColor(getResources().getColor(R.color.colorActionBarBackground));
                        textViewMap.get("mycomment").setTextColor(Color.BLACK);
                        textViewMap.get("notification").setTextColor(Color.BLACK);
                        break;
                    case "mycomment":
                        data.setMailTabId(1);
                        textViewMap.get("message").setTextColor(Color.BLACK);
                        textViewMap.get("mycomment").setTextColor(getResources().getColor(R.color.colorActionBarBackground));
                        textViewMap.get("notification").setTextColor(Color.BLACK);
                        break;
                    case "notification":
                        data.setMailTabId(2);
                        textViewMap.get("message").setTextColor(Color.BLACK);
                        textViewMap.get("mycomment").setTextColor(Color.BLACK);
                        textViewMap.get("notification").setTextColor(getResources().getColor(R.color.colorActionBarBackground));
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
