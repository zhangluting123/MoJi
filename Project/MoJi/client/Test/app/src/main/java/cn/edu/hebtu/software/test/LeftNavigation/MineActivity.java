package cn.edu.hebtu.software.test.LeftNavigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.test.Activity.MainActivity;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.ActivityManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * @ProjectName:    MoJi
 * @Description:    个人中心界面
 * @Author:         张璐婷
 * @CreateDate:     2019/11/26 15:48
 * @Version:        1.0
 */
public class MineActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView userName;
    private ImageView userHeader;
    private ImageView userSex;
    private TextView userJob;
    private TextView userSign;
    private LinearLayout toInfo1;
    private RelativeLayout toInfo2;
    private int tabId;

    private String ip;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);

        ActivityManager.getInstance().addActivity(this);
        final MyApplication data = (MyApplication) getApplication();
        user = new User();
        user.setUserId(data.getUser().getUserId());
        user.setUserHeadImg(data.getUser().getUserHeadImg());
        user.setOccupation(data.getUser().getOccupation());
        user.setPassword(data.getUser().getPassword());
        user.setSex(data.getUser().getSex());
        user.setPhone(data.getUser().getPhone());
        user.setSignature(data.getUser().getSignature());
        user.setUserName(data.getUser().getUserName());

        ip = data.getIp();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        }

        getViews();

        userName.setText(user.getUserName());
        RequestOptions options = new RequestOptions().circleCrop();
        Glide.with(this).load("http://" + ip + ":8080/MoJi/"+user.getUserHeadImg()).apply(options).into(userHeader);
        if("girl".equals(user.getSex())){
            userSex.setImageResource(R.mipmap.girl);
        }else if("boy".equals(user.getSex())){
            userSex.setImageResource(R.mipmap.boy);
        }
        userJob.setText(user.getOccupation());
        userSign.setText(user.getSignature());

        Intent request= getIntent();
        tabId = request.getIntExtra("tab", 0);

        //点击返回按钮返回主页面
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent response = new Intent(MineActivity.this, MainActivity.class);
                response.putExtra("flag",true);
                response.putExtra("tab", tabId);
                startActivity(response);
                overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                finish();
            }
        });

        registerListener();
    }

    private void getViews(){
        toolbar = findViewById(R.id.mine_toolbar);
        userName = findViewById(R.id.tv_userName);
        userHeader = findViewById(R.id.iv_userHeader);
        userSex = findViewById(R.id.iv_userSex);
        userJob = findViewById(R.id.tv_userJob);
        userSign = findViewById(R.id.tv_userSign);
        toInfo1 = findViewById(R.id.ll_toInfo1);
        toInfo2 = findViewById(R.id.ll_toInfo2);
    }

    private void registerListener(){
        CustomOnClickListener listener = new CustomOnClickListener();
        toInfo1.setOnClickListener(listener);
        toInfo2.setOnClickListener(listener);
    }

    class CustomOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MineActivity.this,UserInfoActivity.class);
            intent.putExtra("tab", tabId);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
