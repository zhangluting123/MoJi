package cn.edu.hebtu.software.test.LeftNavigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTabHost;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import cn.edu.hebtu.software.test.Activity.MainActivity;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.Fragment.MyFollowPrintFolledFragment;
import cn.edu.hebtu.software.test.Fragment.MyFollowPrintLikedFragment;
import cn.edu.hebtu.software.test.Fragment.MyFootPrintVideoFragment;
import cn.edu.hebtu.software.test.Fragment.MyFootPrintWordFragment;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.ActivityManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @Author: 邸祯策
 * @Date: 2020/05/22
 * @Describe: 我的关注页
 */
public class MyFollowActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private MyApplication data;
    private int footTabId;
    private Map<String, TextView> textViewMap = new HashMap<>();

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_follow_print);

        ActivityManager.getInstance().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.MyTheam_color));

        }
        data = (MyApplication)getApplication();
        footTabId = data.getFootTabId();

        Intent intent = getIntent();
        user = data.getUser();

        toolbar = findViewById(R.id.foot_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null == user) {
                    Intent response = new Intent(MyFollowActivity.this, MainActivity.class);
                    response.putExtra("flag", true);
                    startActivity(response);
                    overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                    finish();
                }else{
                    overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                    finish();
                }
            }
        });

        //开启读取权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }


        FragmentTabHost fragmentTabHost = findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this,
                getSupportFragmentManager(),
                android.R.id.tabcontent);

        //图文
        TabHost.TabSpec message = fragmentTabHost.newTabSpec("followed").setIndicator(getTabSpecView("followed","我的关注"));
        fragmentTabHost.addTab(message, MyFollowPrintFolledFragment.class, null);
        //视频
        TabHost.TabSpec mycomment = fragmentTabHost.newTabSpec("liked").setIndicator(getTabSpecView("liked","我的点赞"));
        fragmentTabHost.addTab(mycomment, MyFollowPrintLikedFragment.class, null);

        //默认选中第一项
        fragmentTabHost.setCurrentTab(footTabId);
        switch (footTabId) {
            case 0:
                textViewMap.get("followed").setTextColor(getResources().getColor(R.color.colorActionBarBackground));
                break;
            case 1:
                textViewMap.get("liked").setTextColor(getResources().getColor(R.color.colorActionBarBackground));
                break;
        }

        //切换选项卡的事件监听器
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId){
                    case "followed":
                        data.setFootTabId(0);
                        textViewMap.get("followed").setTextColor(getResources().getColor(R.color.colorActionBarBackground));
                        textViewMap.get("liked").setTextColor(Color.BLACK);
                        break;
                    case "liked":
                        data.setFootTabId(1);
                        textViewMap.get("followed").setTextColor(Color.BLACK);
                        textViewMap.get("liked").setTextColor(getResources().getColor(R.color.colorActionBarBackground));
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
    public void onBackPressed() { //"全屏竖屏切换的时候继续播放"
        if (JCVideoPlayerStandard.backPress()){
            return;
        }
        super.onBackPressed();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }
}
