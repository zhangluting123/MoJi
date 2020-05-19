package cn.edu.hebtu.software.test.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTabHost;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.DetailActivity.AddNoteActivity;
import cn.edu.hebtu.software.test.DetailActivity.UploadVideoActivity;
import cn.edu.hebtu.software.test.Fragment.DropsFragment;
import cn.edu.hebtu.software.test.Fragment.FootprintFragment;
import cn.edu.hebtu.software.test.Fragment.MileageFragment;
import cn.edu.hebtu.software.test.LeftNavigation.AboutUsActivity;
import cn.edu.hebtu.software.test.LeftNavigation.MineActivity;
import cn.edu.hebtu.software.test.LeftNavigation.MyFootPrintActivity;
import cn.edu.hebtu.software.test.LeftNavigation.MyMailActivity;
import cn.edu.hebtu.software.test.LeftNavigation.SettingActivity;
import cn.edu.hebtu.software.test.LeftNavigation.TimelineActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.ActivityManager;
import cn.edu.hebtu.software.test.Util.SharedPreferencesUtils;
import cn.edu.hebtu.software.test.Util.SharedUtil;
import cn.jpush.android.api.JPushInterface;

/**
 * @ProjectName:    MoJi
 * @Description:    主界面 | 左侧菜单栏
 * @Author:         李晓萌 | 张璐婷
 * @CreateDate:     2019/11/26 15:32
 * @Version:        1.0
 */

public class MainActivity extends AppCompatActivity  {
    private long exitTime;
    private boolean flag;
    private int tab;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle toggle;
    private ImageView img;
    private TextView user_name;
    private Map<String, ImageView> imageViewMap = new HashMap<>();
    private Map<String, TextView> textViewMap = new HashMap<>();
    //退出登录
    private RelativeLayout logout;
    //数据
    private User user;
    private String ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityManager.getInstance().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        //从全局变量中获取数据
        final MyApplication data = (MyApplication) getApplication();
        user = data.getUser();
        ip = data.getIp();
        img = findViewById(R.id.img);
        user_name = findViewById(R.id.btn_user_name);

        if(null != user.getUserId()){
            RequestOptions options = new RequestOptions().circleCrop();
            Glide.with(this).load("http://" + ip + ":8080/MoJi/"+user.getUserHeadImg()).apply(options).into(img);
            user_name.setText(user.getUserName());
        }else{
            img.setImageResource(R.drawable.headportrait);
            user_name.setText("登录");
        }

        Intent response = getIntent();
        tab = data.getTabId();
        flag = response.getBooleanExtra("flag", false);

        initView();

        FragmentTabHost fragmentTabHost = findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this,
                getSupportFragmentManager(),//FragmentManager对象用来管理多个Fragment
                android.R.id.tabcontent);//真正显示内容页面的容器的id

        //点滴
        TabHost.TabSpec drops= fragmentTabHost.newTabSpec("drops")
                .setIndicator(getTabSpecView("drops",R.mipmap.water2,"点滴"));
        fragmentTabHost.addTab(drops,
                DropsFragment.class,
                null);
        //足迹
        TabHost.TabSpec footprint = fragmentTabHost.newTabSpec("footprint")
                .setIndicator(getTabSpecView("footprint",R.mipmap.foot2,"足迹"));
        fragmentTabHost.addTab(footprint,
                FootprintFragment.class,
                null);

        //远行
        TabHost.TabSpec music= fragmentTabHost.newTabSpec("mileage")
                .setIndicator(getTabSpecView("mileage",R.mipmap.yuanxing2,"远行"));
        fragmentTabHost.addTab(music,
                MileageFragment.class,
                null);

        //默认选中第一项
        fragmentTabHost.setCurrentTab(tab);
        switch (tab){
            case 0:
                imageViewMap.get("drops").setImageResource(R.mipmap.water1);
                textViewMap.get("drops").setTextColor(getResources().getColor(R.color.MyTheam_color));
                break;
            case 1:
                imageViewMap.get("footprint").setImageResource(R.mipmap.foot1);
                textViewMap.get("footprint").setTextColor(getResources().getColor(R.color.MyTheam_color));
                break;
            case 2:
                imageViewMap.get("mileage").setImageResource(R.mipmap.yuanxing1);
                textViewMap.get("mileage").setTextColor(getResources().getColor(R.color.MyTheam_color));
                break;
        }


        //切换选项卡的事件监听器
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch (tabId){
                    case "drops":
                        data.setTabId(0);
                        imageViewMap.get("footprint").setImageResource(R.mipmap.foot2);
                        imageViewMap.get("drops").setImageResource(R.mipmap.water1);
                        imageViewMap.get("mileage").setImageResource(R.mipmap.yuanxing2);
                        textViewMap.get("footprint").setTextColor(Color.BLACK);
                        textViewMap.get("drops").setTextColor(getResources().getColor(R.color.MyTheam_color));
                        textViewMap.get("mileage").setTextColor(Color.BLACK);
                        break;
                    case "footprint":
                        data.setTabId(1);
                        imageViewMap.get("footprint").setImageResource(R.mipmap.foot1);
                        imageViewMap.get("drops").setImageResource(R.mipmap.water2);
                        imageViewMap.get("mileage").setImageResource(R.mipmap.yuanxing2);
                        textViewMap.get("footprint").setTextColor(getResources().getColor(R.color.MyTheam_color));
                        textViewMap.get("drops").setTextColor(Color.BLACK);
                        textViewMap.get("mileage").setTextColor(Color.BLACK);
                        break;

                    case "mileage":
                        data.setTabId(2);
                        imageViewMap.get("footprint").setImageResource(R.mipmap.foot2);
                        imageViewMap.get("drops").setImageResource(R.mipmap.water2);
                        imageViewMap.get("mileage").setImageResource(R.mipmap.yuanxing1);
                        textViewMap.get("footprint").setTextColor(Color.BLACK);
                        textViewMap.get("drops").setTextColor(Color.BLACK);
                        textViewMap.get("mileage").setTextColor(getResources().getColor(R.color.MyTheam_color));
                        break;
                }
            }
        });
    }

    private void initView() {
        mToolbar = findViewById(R.id.id_drawer_layout_toolbar);
        mDrawerlayout = findViewById(R.id.id_drawer_layout);
        //菜单
        mToolbar.inflateMenu(R.menu.menu_drawer);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(null != user.getUserId()) {
                    switch (item.getItemId()) {
                        case R.id.addNote:
                            MyApplication data = (MyApplication) getApplication();
                            Intent jumpToAddNote = new Intent(MainActivity.this, AddNoteActivity.class);
                            jumpToAddNote.putExtra("latitude", data.getLatitude());//维度
                            jumpToAddNote.putExtra("longitude", data.getLongitude());//经度
                            jumpToAddNote.putExtra("location", data.getLocation());
                            startActivity(jumpToAddNote);
                            break;
                        case R.id.addVideo:
                            Intent jumpToAddVideo = new Intent(MainActivity.this, UploadVideoActivity.class);
                            startActivity(jumpToAddVideo);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
        //退出登录
        logout = findViewById(R.id.ll_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清除保存在本地的user信息
                SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(MainActivity.this,"userInfo");
                sharedPreferencesUtils.clear();
                //删除JPush别名信息
                SharedUtil.remove("isGuide", getApplicationContext(), user.getUserId());
                JPushInterface.stopPush(getApplicationContext());
                //IM退出登录
                logoutIM();

                user.setUserId(null);
                img.setImageResource(R.drawable.headportrait);
                user_name.setText("登录");
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                ActivityManager.getInstance().exit();
            }
        });

        //DrawerLayout监听器,监听开关
        toggle = new ActionBarDrawerToggle(
                this,
                mDrawerlayout,
                mToolbar,
                R.string.app_name,
                R.string.app_name
        );
        //同步drawerLayout
        toggle.syncState();
        //给drawerLayout添加监听
        mDrawerlayout.addDrawerListener(toggle);
        if(flag){
            mDrawerlayout.openDrawer(Gravity.LEFT);
        }

        List<Map<String,String>>  list = new ArrayList<>();
        Map map1 = new HashMap<String,String>();
        map1.put("img", R.mipmap.licheng1);
        map1.put("msg", "我的里程碑");
        list.add(map1);
        Map map2 = new HashMap<String,String>();
        map2.put("img",R.mipmap.foot1);
        map2.put("msg", "我的足迹");
        list.add(map2);
        Map map3 = new HashMap<String,String>();
        map3.put("img", R.mipmap.mail2);
        map3.put("msg", "我的消息");
        list.add(map3);
        Map map4 = new HashMap<String,String>();
        map4.put("img", R.mipmap.setting);
        map4.put("msg", "设置");
        list.add(map4);
        Map map5 = new HashMap<String,String>();
        map5.put("img", R.mipmap.aboutus);
        map5.put("msg", "关于我们");
        list.add(map5);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                list,
                R.layout.item_choose,
                new String[]{"img","msg"},
                new int[]{R.id.tv_choose_img,R.id.tv_choose_msg});
        ListView listView = findViewById(R.id.list_select);
        listView.setAdapter(simpleAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent ;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        intent = new Intent(MainActivity.this, TimelineActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, MyFootPrintActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, MyMailActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, AboutUsActivity.class);
                        startActivity(intent);
                        break;
                }

            }
        });


        //点击进入个人中心页
        LinearLayout header = findViewById(R.id.draw_header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != user.getUserId()){
                    Intent intent = new Intent(MainActivity.this, MineActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MainActivity.this, LoginInActivity.class);
                    startActivity(intent);
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
        textViewMap.put(tag,textView);
        return view;
    }

    private void logoutIM(){
        EMClient.getInstance().logout(true,new EMCallBack(){

            @Override
            public void onSuccess() {
                Log.e("chat", user.getUserId()+"退出登录");
            }

            @Override
            public void onError(int code, String error) {

            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了返回键
        if(keyCode == KeyEvent.KEYCODE_BACK ){
            //获取当前时间毫秒数
            if(System.currentTimeMillis()-exitTime>2000){
                Toast.makeText(this,"再次点击退出程序",Toast.LENGTH_LONG).show();
                exitTime=System.currentTimeMillis();
            }else{
                flag = false;
                ActivityManager.getInstance().exit();
                this.finish();
            }
        }
        return true;
    }


}
