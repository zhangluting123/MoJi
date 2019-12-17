package com.net.meng.practicaltrainingdemo;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.net.meng.practicaltrainingdemo.fragment.DropsFragment;
import com.net.meng.practicaltrainingdemo.fragment.FootprintFragment;
import com.net.meng.practicaltrainingdemo.fragment.MileageFragment;
import com.net.meng.practicaltrainingdemo.fragment.QuotationFragment;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private Map<String, ImageView> imageViewMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //使用ToolBar替换ActionBar
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);






        //获取FragmentTabHost对象
        FragmentTabHost fragmentTabHost = findViewById(android.R.id.tabhost);
        //初始化FragmentTabHost
        fragmentTabHost.setup(this,
                getSupportFragmentManager(),//FragmentManager对象用来管理多个Fragment
                android.R.id.tabcontent);//真正显示内容页面的容器的id
        //创建tabspec的对象

        //点滴
        TabHost.TabSpec drops= fragmentTabHost.newTabSpec("drops")
                .setIndicator(getTabSpecView("drops",R.drawable.diandi,"点滴"));
        fragmentTabHost.addTab(drops,
                DropsFragment.class,
                null);
        //足迹
        TabHost.TabSpec footprint = fragmentTabHost.newTabSpec("footprint")
                .setIndicator(getTabSpecView("footprint",R.drawable.zuji,"足迹"));
        fragmentTabHost.addTab(footprint,
                FootprintFragment.class,
                null);
        //语录
        TabHost.TabSpec quotation = fragmentTabHost.newTabSpec("quotation")
                .setIndicator(getTabSpecView("quotation",R.drawable.yangguang,"语录"));
        fragmentTabHost.addTab(quotation,
                QuotationFragment.class,
                null);
        //里程
        TabHost.TabSpec music= fragmentTabHost.newTabSpec("mileage")
                .setIndicator(getTabSpecView("mileage",R.drawable.ziyuan,"里程"));
        fragmentTabHost.addTab(music,
                MileageFragment.class,
                null);

        //默认选中第一项
        fragmentTabHost.setCurrentTab(0);
        imageViewMap.get("drops").setImageResource(R.drawable.diandi2);

        //切换选项卡的事件监听器
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId){
                    case "footprint":
                        imageViewMap.get("footprint").setImageResource(R.drawable.zuji2);
                        imageViewMap.get("drops").setImageResource(R.drawable.diandi);
                        imageViewMap.get("quotation").setImageResource(R.drawable.yangguang);
                        imageViewMap.get("mileage").setImageResource(R.drawable.ziyuan);
                        break;
                    case "drops":
                        imageViewMap.get("footprint").setImageResource(R.drawable.zuji);
                        imageViewMap.get("drops").setImageResource(R.drawable.diandi2);
                        imageViewMap.get("quotation").setImageResource(R.drawable.yangguang);
                        imageViewMap.get("mileage").setImageResource(R.drawable.ziyuan);
                        break;
                    case "quotation":
                        imageViewMap.get("footprint").setImageResource(R.drawable.zuji);
                        imageViewMap.get("drops").setImageResource(R.drawable.diandi);
                        imageViewMap.get("quotation").setImageResource(R.drawable.yangguang2);
                        imageViewMap.get("mileage").setImageResource(R.drawable.ziyuan);
                        break;
                    case "mileage":
                        imageViewMap.get("footprint").setImageResource(R.drawable.zuji);
                        imageViewMap.get("drops").setImageResource(R.drawable.diandi);
                        imageViewMap.get("quotation").setImageResource(R.drawable.yangguang);
                        imageViewMap.get("mileage").setImageResource(R.drawable.ziyuan2);
                        break;
                }
            }
        });
    }



    private View getTabSpecView(String tag, int imageResId, String title) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.tabspec_layout,null);

        //获取控件对象
        ImageView imageView = view.findViewById(R.id.iv_icon);
        imageView.setImageResource(imageResId);

        TextView textView = view.findViewById(R.id.tv_icon);
        textView.setText(title);

        imageViewMap.put(tag,imageView);
        return view;
    }
}
