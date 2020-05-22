package cn.edu.hebtu.software.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.test.Activity.MainActivity;
import cn.edu.hebtu.software.test.Data.MyMultipleItem;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.Data.Video;
import cn.edu.hebtu.software.test.DetailActivity.DropsDetailActivity;
import cn.edu.hebtu.software.test.DetailActivity.OtherMsgActivity;
import cn.edu.hebtu.software.test.DetailActivity.VideoDetailActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @Author: 邸凯扬
 * @Date: 2020/05/12
 * @Describe:
 */
public class MultipleItemAdapter extends BaseMultiItemQuickAdapter<MyMultipleItem, BaseViewHolder> {

    private Context mContext;
    private List<Video> videoList = new ArrayList<>();

    public MultipleItemAdapter(Context mContext, List data, List<Video> videoList) {
        super(data);
        this.mContext=mContext;
        this.videoList=videoList;
        //必须绑定type和layout的关系
        addItemType(MyMultipleItem.FIRST_TYPE, R.layout.mileage_layout_item6_recycler_item);
        addItemType(MyMultipleItem.SECOND_TYPE, R.layout.mileage_layout_item6_recycler_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyMultipleItem item) {
        String ip = item.getData().get("ip").toString();
        String videoPath = item.getData().get("videoPath").toString();
        String UserHeadImg = item.getData().get("UserHeadImg").toString();
        String UserName = item.getData().get("UserName").toString();
        String videoTitle = item.getData().get("videoTitle").toString();
        User user = (User) item.getData().get("User");
        Video video = (Video) item.getData().get("Video");

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
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareVideo(videoPath);
            }
        });
        ImageView good = helper.itemView.findViewById(R.id.good);
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

        String path = "http://"+ ip +":8080/MoJi/"+item.getData().get("videoPath").toString();
        JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) helper.itemView.findViewById(R.id.videoplayer);
        jcVideoPlayerStandard.setUp(item.getData().get("videoPath").toString(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "MoJi");

        Glide.with(mContext)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(4000000)
                                .centerCrop()
                                .error(R.drawable.fail)
                                .placeholder(R.drawable.fail)
                )
                .load(item.getData().get("videoPath").toString())
                .into(jcVideoPlayerStandard.thumbImageView);
    }

    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        this.onBackPressed();
    }

    protected void onPause() {
        this.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    private void shareVideo(String path){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putExtra(Intent.EXTRA_TEXT,"MoJi视频分享 "+path);
        mContext.startActivity(shareIntent);
    }
}

