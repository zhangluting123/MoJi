package cn.edu.hebtu.software.test.LeftNavigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.ActivityManager;
import cn.edu.hebtu.software.test.Util.SharedUtil;
import cn.jpush.android.api.JPushInterface;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MsgPermissionActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RadioButton open;
    private RadioButton close;
    private RadioGroup radioGroup;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_permission);

        ActivityManager.getInstance().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.MyTheam_color));

        }

        MyApplication data = (MyApplication)getApplication();
        user = data.getUser();

        getViews();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent response = new Intent(MsgPermissionActivity.this, SettingActivity.class);
                startActivity(response);
                overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                finish();
            }
        });


        if("open".equals(data.getMsgPermission())){
            open.setChecked(true);
        }else if("close".equals(data.getMsgPermission())){
            close.setChecked(true);
            JPushInterface.stopPush(getApplicationContext());
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_open:
                        data.setMsgPermission("open");
                        SharedUtil.putString("isGuide",getApplicationContext(),"notimsg","open");
                        if(JPushInterface.isPushStopped(getApplicationContext())){
                            JPushInterface.resumePush(getApplicationContext());
                        }
                        break;
                    case R.id.rb_close:
                        data.setMsgPermission("close");
                        SharedUtil.putString("isGuide",getApplicationContext(),"notimsg","close");
                        JPushInterface.stopPush(getApplicationContext());
                        break;
                }
            }
        });

    }

    private void getViews(){
        toolbar = findViewById(R.id.phone_toolbar);
        open = findViewById(R.id.rb_open);
        close = findViewById(R.id.rb_close);
        radioGroup = findViewById(R.id.radioGroup);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
