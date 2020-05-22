package cn.edu.hebtu.software.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import cn.edu.hebtu.software.test.Activity.MainActivity;
import cn.edu.hebtu.software.test.Data.MyMultipleItem;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.DetailActivity.OtherMsgActivity;
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

    public MultipleItemAdapter(Context mContext,List data) {
        super(data);
        this.mContext=mContext;
        //必须绑定type和layout的关系
        addItemType(MyMultipleItem.FIRST_TYPE, R.layout.mileage_layout_item6_recycler_item);
        addItemType(MyMultipleItem.SECOND_TYPE, R.layout.mileage_layout_item6_recycler_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyMultipleItem item) {
        //点击头像个人详情页
//        RoundedImageView head = helper.itemView.findViewById(R.id.head);
//        head.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, OtherMsgActivity.class);
//                intent.putExtra("user",((MyApplication)mContext.getApplicationContext()).getUser());
//                mContext.startActivity(intent);
//            }
//        });
        String ip = item.getData().get("ip").toString();
        String videoPath = item.getData().get("videoPath").toString();
        String UserHeadImg = item.getData().get("UserHeadImg").toString();
        String UserName = item.getData().get("UserName").toString();
        String videoTitle = item.getData().get("videoTitle").toString();

        RoundedImageView head = helper.itemView.findViewById(R.id.head);
        Glide.with(mContext).load("http://"+ ip +":8080/MoJi/" + UserHeadImg).into(head);
        TextView petname = helper.itemView.findViewById(R.id.petname);
        petname.setText(UserName);
        TextView title = helper.itemView.findViewById(R.id.title);
        title.setText(videoTitle);

        ImageView share = helper.itemView.findViewById(R.id.share);
        ImageView good = helper.itemView.findViewById(R.id.good);
        ImageView comment = helper.itemView.findViewById(R.id.comment);

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



        /*String image = item.getData().get("url").toString();
        Uri uri = Uri.parse (image);
        jcVideoPlayerStandard.thumbImageView.setImageURI (uri);
        Glide.with (mContext).load (uri).into (jcVideoPlayerStandard.thumbImageView);

        jcVideoPlayerStandard.backButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                jcVideoPlayerStandard.release ();
            }
        });*/
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
}

