package cn.edu.hebtu.software.test.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.test.Activity.MainActivity;
import cn.edu.hebtu.software.test.ChatIM.ChatActivity;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.LeftNavigation.MineActivity;
import cn.edu.hebtu.software.test.LeftNavigation.MyFootPrintActivity;
import cn.edu.hebtu.software.test.LeftNavigation.UserInfoActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hyphenate.easeui.EaseConstant;

/**
 * @ProjectName:    MoJi
 * @Description:    个人详情页
 * @Author:         张璐婷
 * @CreateDate:     2020/5/15 11:12
 * @Version:        1.0
 */
public class OtherMsgActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView userName;
    private ImageView userHeader;
    private ImageView userSex;
    private TextView userJob;
    private TextView userSign;
    private Button btnOtherTrends;
    private Button btnSendMsg;
    private Button btnAddAttentaion;
    private User user;
    private String ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_msg);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        }

        getViews();
        MyApplication data = (MyApplication)getApplication();
        ip = data.getIp();
        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
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


        //点击返回按钮返回主页面
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        btnAddAttentaion = findViewById(R.id.btn_add_attention);
        btnOtherTrends = findViewById(R.id.btn_other_trends);
        btnSendMsg = findViewById(R.id.btn_send_msg);
    }

    private void registerListener(){
        CustomOnClickListener listener = new CustomOnClickListener();
        btnAddAttentaion.setOnClickListener(listener);
        btnOtherTrends.setOnClickListener(listener);
        btnSendMsg.setOnClickListener(listener);
    }

    class CustomOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.btn_add_attention:
                    //TODO 添加关注
                    break;
                case R.id.btn_other_trends:
                    intent = new Intent(OtherMsgActivity.this, MyFootPrintActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    break;
                case R.id.btn_send_msg:
                    //发送消息
                    intent = new Intent(OtherMsgActivity.this, ChatActivity.class);
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, user.getUserId());
                    startActivity(intent);
                    break;
            }

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
