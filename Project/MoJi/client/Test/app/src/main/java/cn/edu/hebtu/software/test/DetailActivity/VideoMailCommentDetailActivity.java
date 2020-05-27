package cn.edu.hebtu.software.test.DetailActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.edu.hebtu.software.test.Adapter.CommentAdapter;
import cn.edu.hebtu.software.test.Data.Comment;
import cn.edu.hebtu.software.test.Data.MailMyComment;
import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.Data.ReplyComment;
import cn.edu.hebtu.software.test.Data.Video;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;

/**
 * @ProjectName:    MoJi
 * @Description:    视频评论消息详情页
 * @Author:         ming
 * @CreateDate:     2020/5/25
 * @Version:        1.0
 */
public class VideoMailCommentDetailActivity extends AppCompatActivity {

    private ImageView noteImg;
    private TextView noteContent;
    private TextView noteUserName;
    private LinearLayout jumpDropsDetail;

    private ImageView replyHead;
    private TextView replyUserName;
    private TextView replyContent;
    private TextView replyDate;
    private RelativeLayout rlParent;
    private TextView atSomeBody;
    private TextView commentContent;

    private MyApplication data;
    private String ip;

    private Video video;
    private List<Comment> commentList;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    Toast.makeText(getApplicationContext(), (CharSequence) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    commentList = (List<Comment>) msg.obj;
                    allComment();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_comment_detail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.smssdk_common_black));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        }

        data = (MyApplication)getApplication();
        ip = data.getIp();

        Toolbar toolbar = findViewById(R.id.reply_comment_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                finish();
            }
        });

        getViews();
        //获得所有评论
        getComments();

        Intent intent = getIntent();
        MailMyComment mailMyComment = intent.getParcelableExtra("mailMyComment");

        RequestOptions options = new RequestOptions().circleCrop();
        if(mailMyComment.getCrFlag() == 'C'){
            Comment comment = mailMyComment.getComment();
            video = comment.getVideo();

            loadVideoScreenshot(this,"http://"+ ip +":8080/MoJi/"+video.getPath(),noteImg,2);

            noteContent.setText(video.getContent());
            noteUserName.setText(video.getUser().getUserName());
            Glide.with(this).load("http://" + ip + ":8080/MoJi/"+comment.getUser().getUserHeadImg()).apply(options).into(replyHead);
            replyUserName.setText(comment.getUser().getUserName());
            replyContent.setText(comment.getCommentContent());
            replyDate.setText(comment.getCommentTime());
            rlParent.setVisibility(View.GONE);
        }else if(mailMyComment.getCrFlag() == 'R'){
            ReplyComment replyComment = mailMyComment.getReplyComment();
            video = replyComment.getComment().getVideo();

            loadVideoScreenshot(this,"http://"+ ip +":8080/MoJi/"+video.getPath(),noteImg,2);

            noteContent.setText(video.getContent());
            noteUserName.setText(video.getUser().getUserName());
            Glide.with(this).load("http://" + ip + ":8080/MoJi/"+replyComment.getReplyUser().getUserHeadImg()).apply(options).into(replyHead);
            replyUserName.setText(replyComment.getReplyUser().getUserName());
            replyContent.setText(replyComment.getReplyContent());
            replyDate.setText(replyComment.getReplyTime());
            if(replyComment.getReplyComment() != null){//如果是一条回复消息，存在parent
                rlParent.setVisibility(View.VISIBLE);
                atSomeBody.setText(replyComment.getReplyComment().getReplyUser().getUserName());
                commentContent.setText(replyComment.getReplyComment().getReplyContent());
            }else{
                rlParent.setVisibility(View.GONE);
            }
        }
        jumpDropsDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),VideoDetailActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("video", video);
                intent1.putExtras(bundle1);
                startActivity(intent1);
            }
        });
    }

    private void getViews(){
        noteImg = findViewById(R.id.iv_noteImg);
        noteContent = findViewById(R.id.tv_noteContent);
        noteUserName = findViewById(R.id.tv_noteUserName);
        jumpDropsDetail = findViewById(R.id.ll_jump_dropsDetail);
        replyHead = findViewById(R.id.iv_replyHead);
        replyUserName = findViewById(R.id.user_name);
        replyContent = findViewById(R.id.tv_replyContent);
        replyDate = findViewById(R.id.tv_replyDate);
        rlParent = findViewById(R.id.rl_parent);
        atSomeBody = findViewById(R.id.tv_atSomeBody);
        commentContent = findViewById(R.id.tv_comment_content);
    }

    private void allComment(){
        ListView listView = findViewById(R.id.lv_replyList);
        CommentAdapter commentAdapter = new CommentAdapter(commentList, R.layout.item_comment, getApplicationContext());
        listView.setAdapter(commentAdapter);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/4/25  15:49
     *  @Description: 获得所有评论
     *  同DropsDetailActivity 的 getComments()功能一致
     */
    private void getComments(){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getApplicationContext())){
                    try {
                        List<Comment> list = new ArrayList<>();
                        URL url = new URL("http://"+ip+":8080/MoJi/comment/list?noteId="+video.getVideoId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String str = null;
                        if((str = reader.readLine()) != null){
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Comment>>(){}.getType();
                            list = gson.fromJson(str,type);
                            Collections.reverse(list);
                            Message msg = Message.obtain();
                            msg.what = 1002;
                            msg.obj = list;
                            handler.sendMessage(msg);
                        }
                        in.close();
                        reader.close();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }else{
                    Message message  = Message.obtain();
                    message.what = 1001;
                    message.obj = "未连接到服务器";
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void loadVideoScreenshot(final Context context, String uri, ImageView imageView, long frameTimeMicros) {
        RequestOptions requestOptions = RequestOptions.frameOf(frameTimeMicros);
        requestOptions.set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST);
        requestOptions.transform(new BitmapTransformation() {
            @Override
            protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                return toTransform;
            }
            @Override
            public void updateDiskCacheKey(MessageDigest messageDigest) {
                try {
                    messageDigest.update((context.getPackageName() + "RotateTransform").getBytes("utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Glide.with(context).load(uri).apply(requestOptions).into(imageView);
    }
}
