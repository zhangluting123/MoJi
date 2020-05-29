package cn.edu.hebtu.software.test.ChatIM;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import cn.edu.hebtu.software.test.R;

public class ChatActivity extends EaseBaseActivity {
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    private MyUserProvider userProvider;
    private EaseUI easeUI;
    String toChatUsername;
    protected static final int REQUEST_CODE_CAMERA = 2;
//    private User user;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_chat);
        if (!ActivityCompat.shouldShowRequestPermissionRationale(ChatActivity.this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(ChatActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        }
        activityInstance = this;
        //user or group id
        toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        chatFragment = new EaseChatFragment();
        //set arguments
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

        easeUI = EaseUI.getInstance();
        //需要EaseUI库显示用户头像和昵称设置此provider
        easeUI.setUserProfileProvider(userProvider.getInstance());
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        // enter to chat activity when click notification bar, here make sure only one chat activiy
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
    }
    
    public String getToChatUsername(){
        return toChatUsername;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        chatFragment.onActivityResult(requestCode, resultCode, data);
    }
}
