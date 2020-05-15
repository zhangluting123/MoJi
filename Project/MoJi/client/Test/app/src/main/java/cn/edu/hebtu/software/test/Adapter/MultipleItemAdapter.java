package cn.edu.hebtu.software.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;
import cn.edu.hebtu.software.test.Data.MyMultipleItem;
import cn.edu.hebtu.software.test.R;
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
        Log.i("tag","FIRST_TYPE==============="+helper.getLayoutPosition());

        ImageView share = helper.itemView.findViewById(R.id.share);
        ImageView good = helper.itemView.findViewById(R.id.good);
        ImageView comment = helper.itemView.findViewById(R.id.comment);

        JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) helper.itemView.findViewById(R.id.videoplayer);
        jcVideoPlayerStandard.setUp(item.getData().get("url").toString(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "MoJi");

        Glide.with(mContext)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(4000000)
                                .centerCrop()
                                .error(R.drawable.fail)
                                .placeholder(R.drawable.fail)
                )
                .load(item.getData().get("url").toString())
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

