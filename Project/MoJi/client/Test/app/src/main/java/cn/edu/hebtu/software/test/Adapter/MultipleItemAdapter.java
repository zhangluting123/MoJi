package cn.edu.hebtu.software.test.Adapter;

import android.util.Log;

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

    public MultipleItemAdapter(List data) {
        super(data);
        //必须绑定type和layout的关系
        addItemType(MyMultipleItem.FIRST_TYPE, R.layout.mileage_layout_item6_recycler_item);
        addItemType(MyMultipleItem.SECOND_TYPE, R.layout.mileage_layout_item6_recycler_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, MyMultipleItem item) {
        Log.i("tag","FIRST_TYPE==============="+helper.getLayoutPosition());
        JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) helper.itemView.findViewById(R.id.videoplayer);
        jcVideoPlayerStandard.setUp(item.getData().get("url").toString(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "MoJi");
        //jcVideoPlayerStandard.thumbImageView.setImageURI(Uri.parse("http://pic9.nipic.com/20100826/3320946_024307806453_2.jpg"));
        /*switch (helper.getItemViewType()) {
            case MyMultipleItem.FIRST_TYPE:
                Log.i("tag","FIRST_TYPE==============="+helper.getLayoutPosition());
                helper.setText(R.id.tv_recy_item_1_desc, item.getData().get("desc").toString());
                helper.setText(R.id.tv_recy_item_1_name, item.getData().get("name").toString());
                helper.setImageResource(R.id.img_recy_item_1_pic, (Integer) https://link.jianshu.com/?t=https%3A%2F%2Fwww.w3schools.com%2Fhtml%2Fmovie.mp4);
                helper.addOnClickListener(R.id.img_recy_item_1_pic);
                helper.addOnClickListener(R.id.tv_recy_item_1_name);
                break;
            case MyMultipleItem.SECOND_TYPE:
                Log.i("tag","FIRST_TYPE==============="+helper.getLayoutPosition());
                helper.setText(R.id.tv_recy_item_1_desc, item.getData().get("desc").toString());
                helper.setText(R.id.tv_recy_item_1_name, item.getData().get("name").toString());
                helper.setImageResource(R.id.img_recy_item_1_pic, (Integer) item.getData().get("pic"));
                helper.addOnClickListener(R.id.img_recy_item_1_pic);
                helper.addOnClickListener(R.id.tv_recy_item_1_name);
                break;
        }*/
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

