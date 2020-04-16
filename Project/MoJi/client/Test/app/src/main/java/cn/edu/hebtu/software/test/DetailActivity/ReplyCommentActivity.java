package cn.edu.hebtu.software.test.DetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.test.Data.Comment;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.ActivityManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ReplyCommentActivity extends AppCompatActivity {
    private ImageView commentHead;
    private TextView commentContent;
    private TextView userName;
    private TextView commentDate;
    private MyApplication data;
    private String ip;
    private RequestOptions options = new RequestOptions().circleCrop();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_comment);

        ActivityManager.getInstance().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#22000000"));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        }

        Toolbar toolbar = findViewById(R.id.reply_comment_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                finish();
            }
        });

        getViews();
        data = (MyApplication)getApplication();
        ip = data.getIp();

        Intent intent = getIntent();
        Comment comment = intent.getParcelableExtra("comment");
        if(null != comment.getUser().getUserHeadImg()) {
            Glide.with(this).load("http://" + ip + ":8080/MoJi/" + comment.getUser().getUserHeadImg()).apply(options).into(commentHead);
        }
        commentContent.setText(comment.getCommentContent());
        userName.setText(comment.getUser().getUserName());
        commentDate.setText(comment.getCommentTime());
    }

    private void getViews(){
        commentHead = findViewById(R.id.iv_commentHead);
        commentContent = findViewById(R.id.tv_commentContent);
        userName = findViewById(R.id.user_name);
        commentDate = findViewById(R.id.tv_commentDate);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
