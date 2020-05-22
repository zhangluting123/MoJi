package cn.edu.hebtu.software.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.util.List;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.DetailActivity.OtherMsgActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;

public class MyFollowAdapter extends BaseAdapter {
    private int itemLayoutId;
    private Context context;
    private List<User> followList;
    private boolean flag;
    private String ip;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    Toast.makeText(context.getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public MyFollowAdapter(int itemLayoutId, Context context, List<User> followList, boolean flag) {
        this.itemLayoutId = itemLayoutId;
        this.context = context;
        this.followList = followList;
        this.flag = flag;
    }

    @Override
    public int getCount() {
        if(null != followList){
            return followList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(null != followList.get(position)){
            return followList.get(position);
        }else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyApplication data = (MyApplication) context.getApplicationContext();
        ip = data.getIp();
        ViewHolder viewHolder = null;
        if(null == convertView){
            convertView = LayoutInflater.from(context).inflate(itemLayoutId,null);
            viewHolder = new ViewHolder();
            viewHolder.head = convertView.findViewById(R.id.head);
            viewHolder.petname = convertView.findViewById(R.id.petname);
            viewHolder.signature = convertView.findViewById(R.id.signature);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(convertView).load("http://" + ip + ":8080/MoJi/" + followList.get(position).getUserHeadImg()).into(viewHolder.head);

        viewHolder.petname.setText(followList.get(position).getUserName());
        viewHolder.signature.setText(followList.get(position).getSignature());



        //CustomWordOnClickListener listener = new CustomWordOnClickListener(position);
        //viewHolder.head.setOnClickListener(listener);

        return convertView;
    }

    class CustomWordOnClickListener implements View.OnClickListener{
        private int position;
        public CustomWordOnClickListener(int position) {
            this.position = position;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.head:
                    Intent intent = new Intent(context, OtherMsgActivity.class);
                    intent.putExtra("user",followList.get(position));
                    context.startActivity(intent);
                    break;
            }
        }
    }

    final static class ViewHolder{
        private ImageView head;
        private TextView petname;
        private TextView signature;
    }

    public void refresh(List<User> noteList){
        this.followList = noteList;
        notifyDataSetChanged();
    }
}
