package cn.edu.hebtu.software.test.LeftNavigation;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTabHost;
import cn.edu.hebtu.software.test.Activity.MainActivity;
import cn.edu.hebtu.software.test.Fragment.MyFootPrintVideoFragment;
import cn.edu.hebtu.software.test.Fragment.MyFootPrintWordFragment;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.ActivityManager;
import cn.edu.hebtu.software.test.R;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

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

public class MyFootPrintActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private MyApplication data;
    private int footTabId;
    private Map<String, TextView> textViewMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_foot_print);

        ActivityManager.getInstance().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.MyTheam_color));

        }
        data = (MyApplication)getApplication();
        footTabId = data.getFootTabId();

        toolbar = findViewById(R.id.foot_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent response = new Intent(MyFootPrintActivity.this, MainActivity.class);
                response.putExtra("flag",true);
                startActivity(response);
                overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                finish();
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
        TabHost.TabSpec message = fragmentTabHost.newTabSpec("word").setIndicator(getTabSpecView("word","图文"));
        fragmentTabHost.addTab(message, MyFootPrintWordFragment.class, null);
        //视频
        TabHost.TabSpec mycomment = fragmentTabHost.newTabSpec("video").setIndicator(getTabSpecView("video","视频"));
        fragmentTabHost.addTab(mycomment, MyFootPrintVideoFragment.class, null);

        //默认选中第一项
        fragmentTabHost.setCurrentTab(footTabId);
        switch (footTabId) {
            case 0:
                textViewMap.get("word").setTextColor(getResources().getColor(R.color.colorActionBarBackground));
                break;
            case 1:
                textViewMap.get("video").setTextColor(getResources().getColor(R.color.colorActionBarBackground));
                break;
        }

        //切换选项卡的事件监听器
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId){
                    case "word":
                        data.setFootTabId(0);
                        textViewMap.get("word").setTextColor(getResources().getColor(R.color.colorActionBarBackground));
                        textViewMap.get("video").setTextColor(Color.BLACK);
                        break;
                    case "video":
                        data.setFootTabId(1);
                        textViewMap.get("word").setTextColor(Color.BLACK);
                        textViewMap.get("video").setTextColor(getResources().getColor(R.color.colorActionBarBackground));
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
