package cn.edu.hebtu.software.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.test.Data.Comment;
import cn.edu.hebtu.software.test.DetailActivity.ReplyCommentActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;

/**
 * @ProjectName:    MoJi
 * @Description:
 * @Author:         张璐婷
 * @CreateDate:     2020/4/16 9:29
 * @Version:        1.0
 */
public class CommentAdapter extends BaseAdapter {
    private List<Comment> commentList = new ArrayList<>();//数据源
    private int itemLayoutId;
    private Context context;
    private String ip;

    private ViewHolder viewHolder;

    public CommentAdapter(List<Comment> commentList,int itemLayoutId,Context context){
        this.commentList = commentList;
        this.itemLayoutId = itemLayoutId;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(null != commentList){
            return commentList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(null != commentList){
            return commentList.get(position);
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
        MyApplication data = (MyApplication)context.getApplicationContext();
        ip = data.getIp();
        if(null == convertView){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(itemLayoutId, null);
            viewHolder.commentHead = convertView.findViewById(R.id.iv_commentHead);
            viewHolder.userName = convertView.findViewById(R.id.user_name);
            viewHolder.commentContent = convertView.findViewById(R.id.tv_commentContent);
            viewHolder.commentDate = convertView.findViewById(R.id.tv_commentDate);
            viewHolder.replyNumber = convertView.findViewById(R.id.tv_reply_number);
            viewHolder.replyComment = convertView.findViewById(R.id.ll_reply_comment);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RequestOptions options = new RequestOptions().circleCrop();
        if(commentList != null && commentList.size() > 0){
            if(null != commentList.get(position).getUser().getUserHeadImg()) {
                Glide.with(context).load("http://" + ip + ":8080/MoJi/" + commentList.get(position).getUser().getUserHeadImg()).apply(options).into(viewHolder.commentHead);
            }
            viewHolder.userName.setText(commentList.get(position).getUser().getUserName());
            viewHolder.commentContent.setText(commentList.get(position).getCommentContent());
            viewHolder.commentDate.setText(commentList.get(position).getCommentTime());
            CustomerButtonListener listener = new CustomerButtonListener(position);
            viewHolder.replyComment.setOnClickListener(listener);
            //TODO 查找回复评论个数
//            viewHolder.replyNumber.setText(0);
        }


        return convertView;
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/4/16  9:30
     *  @Description: 区分item中的button
     */
    class CustomerButtonListener implements View.OnClickListener{
        private int position;

        public CustomerButtonListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == viewHolder.replyComment.getId()){
                    Intent intent = new Intent(context, ReplyCommentActivity.class);
                    intent.putExtra("comment",commentList.get(position));
                    context.startActivity(intent);
            }
        }

    }

    final static class ViewHolder{
        private ImageView commentHead;
        private TextView userName;
        private TextView commentContent;
        private TextView commentDate;
        private LinearLayout replyComment;
        private TextView replyNumber;
    }

    public void flush(List<Comment> commentList){
        this.commentList = commentList;
        notifyDataSetChanged();
    }
}
