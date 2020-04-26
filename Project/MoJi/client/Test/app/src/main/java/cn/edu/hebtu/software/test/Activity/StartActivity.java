package cn.edu.hebtu.software.test.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Util.SharedUtil;

/**
 * @ProjectName:    MoJi
 * @Description:    启动页
 * @Author:         张璐婷
 * @CreateDate:     2019/11/26 15:47
 * @Version:        1.0
 */
public class StartActivity extends AppCompatActivity {
    boolean isFirstIn = false;
    private Intent intent ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        final MyApplication data = (MyApplication) getApplication();
        data.setIp(getString(R.string.internet_ip));

        //判断是否第一次下载APP
        isFirstIn = SharedUtil.getBoolean("isGuide", this,"isFirstIn",true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isFirstIn){
                    intent = new Intent(StartActivity.this, GuidePageActivity.class);
                    SharedUtil.putBoolean("isGuide", getApplicationContext(),"isFirstIn", false);
                    startActivity(intent);
                    finish();
                }else{
                    intent = new Intent(StartActivity.this, MainActivity.class );//WelcomeActivity.class
                    startActivity(intent);
                    finish();
                }
            }
        }, 1);


    }
}
