package cn.edu.hebtu.software.test.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.test.Data.MailMyComment;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
/**
 * @ProjectName:    MoJi
 * @Description:    评论消息适配器
 * @Author:         张璐婷
 * @CreateDate:     2020/4/24 13:32
 * @Version:        1.0
 */
public class MyCommentAdapter extends BaseAdapter {
    private List<MailMyComment> myComments = new ArrayList<>();
    private int itemLayoutId;
    private Context context;
    String ip;


    public MyCommentAdapter(List<MailMyComment> myComments, int itemLayoutId, Context context) {
        this.myComments = myComments;
        this.itemLayoutId = itemLayoutId;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(null != myComments){
            return myComments.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(null != myComments){
            return  myComments.get(position);
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
        ViewHolder viewHolder = new ViewHolder();
        if(null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(itemLayoutId, null);
            viewHolder.readFlag = convertView.findViewById(R.id.iv_read_flag);
            viewHolder.afterHead = convertView.findViewById(R.id.iv_after_Head);
            viewHolder.afterUserName = convertView.findViewById(R.id.tv_after_userName);
            viewHolder.afterContent = convertView.findViewById(R.id.tv_after_content);
            viewHolder.afterDate = convertView.findViewById(R.id.tv_after_Date);
            viewHolder.beforeNote = convertView.findViewById(R.id.ll_before_note);
            viewHolder.noteTitle = convertView.findViewById(R.id.tv_noteTitle);
            viewHolder.beforeComment = convertView.findViewById(R.id.rl_before_comment);
            viewHolder.beforeUserName = convertView.findViewById(R.id.tv_before_userName);
            viewHolder.beforeContent = convertView.findViewById(R.id.tv_before_content);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        if(myComments.get(position).getReadFlag() == 0){//未读
            viewHolder.readFlag.setImageResource(R.mipmap.unread_flag);
        }else{
            viewHolder.readFlag.setImageResource(0);
        }
        RequestOptions options = new RequestOptions().circleCrop();
        if(myComments.get(position).getCrFlag() == 'C') {//我的动态评论信息
            Glide.with(context).load("http://" + ip + ":8080/MoJi/" + myComments.get(position).getComment().getUser().getUserHeadImg()).apply(options).into(viewHolder.afterHead);
            viewHolder.afterUserName.setText(myComments.get(position).getComment().getUser().getUserName());
            viewHolder.afterContent.setText(myComments.get(position).getComment().getCommentContent());
            viewHolder.afterDate.setText(myComments.get(position).getComment().getCommentTime());
            //动态可见，评论不可见
            viewHolder.beforeNote.setVisibility(View.VISIBLE);
            viewHolder.beforeComment.setVisibility(View.GONE);
            viewHolder.noteTitle.setText(myComments.get(position).getComment().getNote().getTitle());
        }else if(myComments.get(position).getCrFlag() == 'R'){//我的评论回复信息
            Glide.with(context).load("http://" + ip + ":8080/MoJi/" + myComments.get(position).getReplyComment().getReplyUser().getUserHeadImg()).apply(options).into(viewHolder.afterHead);
            viewHolder.afterUserName.setText(myComments.get(position).getReplyComment().getReplyUser().getUserName());
            viewHolder.afterContent.setText(myComments.get(position).getReplyComment().getReplyContent());
            viewHolder.afterDate.setText(myComments.get(position).getReplyComment().getReplyTime());
            //动态不可见，评论可见
            viewHolder.beforeNote.setVisibility(View.GONE);
            viewHolder.beforeComment.setVisibility(View.VISIBLE);
            if(null != myComments.get(position).getReplyComment().getReplyComment()) {//有父元素
                viewHolder.beforeUserName.setText(myComments.get(position).getReplyComment().getReplyComment().getReplyUser().getUserName());
                viewHolder.beforeContent.setText(myComments.get(position).getReplyComment().getReplyComment().getReplyContent());
            }else{
                viewHolder.beforeUserName.setText(myComments.get(position).getReplyComment().getComment().getUser().getUserName());
                viewHolder.beforeContent.setText(myComments.get(position).getReplyComment().getComment().getCommentContent());
            }
        }

        return convertView;
    }

    final static class ViewHolder{
        private ImageView readFlag;
        private ImageView afterHead;
        private TextView afterUserName;
        private TextView afterContent;
        private TextView afterDate;
        private LinearLayout beforeNote;
        private TextView noteTitle;
        private RelativeLayout beforeComment;
        private TextView beforeUserName;
        private TextView beforeContent;
    }

    public void refresh(List<MailMyComment> myComments){
        this.myComments = myComments;
        notifyDataSetChanged();
    }
}
