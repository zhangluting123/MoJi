package cn.edu.hebtu.software.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.test.Activity.MainActivity;
import cn.edu.hebtu.software.test.Data.MyMultipleItem;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.Data.UserLike;
import cn.edu.hebtu.software.test.Data.Video;
import cn.edu.hebtu.software.test.DetailActivity.DropsDetailActivity;
import cn.edu.hebtu.software.test.DetailActivity.OtherMsgActivity;
import cn.edu.hebtu.software.test.DetailActivity.VideoDetailActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;
import cn.edu.hebtu.software.test.Util.NumStrUtil;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @Author: ming
 * @Date: 2020/05/12
 * @Describe:
 */
public class MultipleItemAdapter extends BaseMultiItemQuickAdapter<MyMultipleItem, BaseViewHolder> {

    private Context mContext;
    private List<Video> videoList = new ArrayList<>();
    private List<UserLike> userLikes = new ArrayList<>();
    private Map<Integer,String> map = new HashMap<>();
    private MyApplication application;
    private String ip;
    private int pos;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(mContext, (CharSequence) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    map.put(pos, (String) msg.obj);
                    break;
                case 1003:
                    Toast.makeText(mContext, (CharSequence) msg.obj, Toast.LENGTH_SHORT).show();
                    map.remove(pos);
                    break;
            }
        }
    };

    public MultipleItemAdapter(Context mContext, List data, List<Video> videoList,List<UserLike> userLikes) {
        super(data);
        this.mContext=mContext;
        this.videoList=videoList;
        this.userLikes = userLikes;
        //必须绑定type和layout的关系
        addItemType(MyMultipleItem.FIRST_TYPE, R.layout.mileage_layout_item6_recycler_item);
        addItemType(MyMultipleItem.SECOND_TYPE, R.layout.mileage_layout_item6_recycler_item);
        application = (MyApplication)mContext.getApplicationContext();
        ip = application.getIp();
    }

    @Override
    protected void convert(BaseViewHolder helper, MyMultipleItem item) {
        String videoPath = item.getData().get("videoPath").toString();
        String UserHeadImg = item.getData().get("UserHeadImg").toString();
        String UserName = item.getData().get("UserName").toString();
        String videoTitle = item.getData().get("videoTitle").toString();
        User user = (User) item.getData().get("User");
        Video video = (Video) item.getData().get("Video");
        int position = (int)item.getData().get("position");


        RoundedImageView head = helper.itemView.findViewById(R.id.head);
        Glide.with(mContext).load("http://"+ ip +":8080/MoJi/" + UserHeadImg).into(head);
        //点击头像个人详情页
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OtherMsgActivity.class);
                intent.putExtra("user",user);
                mContext.startActivity(intent);
            }
        });
        TextView petname = helper.itemView.findViewById(R.id.petname);
        petname.setText(UserName);
        TextView title = helper.itemView.findViewById(R.id.title);
        title.setText(videoTitle);


        ImageView share = helper.itemView.findViewById(R.id.share);
        String path = "http://"+ ip +":8080/MoJi/"+item.getData().get("videoPath").toString();
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareVideo(path);
            }
        });

        ImageView comment = helper.itemView.findViewById(R.id.comment);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VideoDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("video", video);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        ImageView good = helper.itemView.findViewById(R.id.good);
        //初始化点赞情况
        for(int i = 0;i < userLikes.size();i++){
            Video v = userLikes.get(i).getVideoLike();
            if(null != v && v.getVideoId().equals(video.getVideoId())){
                good.setImageResource(R.drawable.nogood);
                map.put(position, userLikes.get(i).getId());
                break;
            }
        }
        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != application.getUser().getUserId()) {
                    pos = position;
                    //视频点赞
                    if(good.getDrawable().getCurrent().getConstantState() == mContext.getResources().getDrawable(R.drawable.good).getConstantState()){
                        changeGood(true, video.getVideoId(), null);
                        good.setImageResource(R.drawable.nogood);
                    }else{
                        changeGood(false, video.getVideoId(),map.get(position));
                        good.setImageResource(R.drawable.good);
                    }
                }else{
                    Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
                }

            }
        });

        JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;//横向
        JCVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;//纵向
        JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) helper.itemView.findViewById(R.id.videoplayer);
        jcVideoPlayerStandard.setUp(path, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "MoJi");

        Glide.with(mContext)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(4000000)
                                .centerCrop()
                                .error(R.drawable.fail)
                                .placeholder(R.drawable.fail)
                )
                .load(videoPath)
                .into(jcVideoPlayerStandard.thumbImageView);
    }


    private void shareVideo(String path){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putExtra(Intent.EXTRA_TEXT,"MoJi视频分享 "+path);
        mContext.startActivity(shareIntent);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/5/23  8:58
     *  @Description: 点赞 或 取消点赞 Video
     */
    public void changeGood(boolean flag,String videoId,String likeId) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (DetermineConnServer.isConnByHttp(mContext.getApplicationContext())) {
                        String urlString = null;
                        if (flag) {
                            urlString = "http://" + ip + ":8080/MoJi/userLike/addLike?userId=" + application.getUser().getUserId() + "&videoId=" + videoId;
                        } else {
                            urlString = "http://" + ip + ":8080/MoJi/userLike/deleteLike?likeId=" + likeId + "&videoId=" + videoId;
                        }
                        URL url = new URL(urlString);
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String str = reader.readLine();
                        Message message = Message.obtain();
                        if (str.equals("OK")) {
                            message.what = 1003;
                            message.obj = "取消成功";
                        } else if (str.equals("ERROR")) {
                            message.what = 1001;
                            message.obj = "更改失败";
                        } else {
                            message.what = 1002;
                            message.obj = str;
                        }
                        handler.sendMessage(message);
                        in.close();
                        reader.close();
                    } else {
                        Message message = Message.obtain();
                        message.what = 1001;
                        message.obj = "未连接到服务器";
                        handler.sendMessage(message);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

