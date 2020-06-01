package cn.edu.hebtu.software.test.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.SharedPreferencesUtils;
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
    private User user = new User();

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
                    SharedUtil.putString("isGuide",getApplicationContext(),"notimsg","open");
                    data.setMsgPermission("open");
                    startActivity(intent);
                    finish();
                }else{
                    String notimsg = SharedUtil.getString("isGuide", getApplicationContext(),"notimsg");
                    data.setMsgPermission(notimsg);
                    //获取保存在本地的用户信息
                    SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(StartActivity.this,"userInfo");
                    user.setUserId(sharedPreferencesUtils.getString("userIdInfo"));
                    user.setUserHeadImg(sharedPreferencesUtils.getString("userHeadImgInfo"));
                    user.setUserName(sharedPreferencesUtils.getString("userNameInfo"));
                    user.setSex(sharedPreferencesUtils.getString("sexInfo"));
                    user.setSignature(sharedPreferencesUtils.getString("signatureInfo"));
                    user.setOccupation(sharedPreferencesUtils.getString("occupationInfo"));
                    user.setPassword(sharedPreferencesUtils.getString("passwordInfo"));
                    user.setPhone(sharedPreferencesUtils.getString("phoneInfo"));
                    data.setUser(user);

                    intent = new Intent(StartActivity.this, WelcomeActivity.class );
                    startActivity(intent);
                    finish();
                }
            }
        }, 1);


    }
}
