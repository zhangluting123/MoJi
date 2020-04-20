package cn.edu.hebtu.software.test.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.edu.hebtu.software.test.Data.ReplyComment;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;

public class ReplyCommentAdapter extends BaseAdapter {
    private List<ReplyComment> replyCommentList;
    private Context context;
    private int itemLayoutId;

    private ViewHolder viewHolder;

    private MyApplication data;
    private String ip;

    public ReplyCommentAdapter() {
    }

    public ReplyCommentAdapter(List<ReplyComment> replyCommentList, Context context, int itemLayoutId) {
        this.replyCommentList = replyCommentList;
        this.context = context;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        if(null != replyCommentList){
            return replyCommentList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(null != replyCommentList){
            return replyCommentList.get(position);
        }else{
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        data = (MyApplication)context.getApplicationContext();
        ip = data.getIp();
        if(null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(itemLayoutId, null);
            viewHolder.replyContent = convertView.findViewById(R.id.tv_replyContent);
            viewHolder.userName = convertView.findViewById(R.id.user_name);
            viewHolder.replyDate = convertView.findViewById(R.id.tv_replyDate);
            viewHolder.replyHead = convertView.findViewById(R.id.iv_replyHead);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        RequestOptions options = new RequestOptions().circleCrop();
        if(replyCommentList != null && replyCommentList.size() > 0){
            if(null != replyCommentList.get(position).getReplyUser().getUserHeadImg()){
                Glide.with(context).load("http://" + ip + ":8080/MoJi/" + replyCommentList.get(position).getReplyUser().getUserHeadImg()).apply(options).into(viewHolder.replyHead);
            }
            viewHolder.replyContent.setText(replyCommentList.get(position).getReplyContent());
            viewHolder.replyDate.setText(replyCommentList.get(position).getReplyTime());
            viewHolder.userName.setText(replyCommentList.get(position).getReplyUser().getUserName());
        }

        return convertView;
    }

    final static class ViewHolder{
        private ImageView replyHead;
        private TextView userName;
        private TextView replyContent;
        private TextView replyDate;
    }
}
