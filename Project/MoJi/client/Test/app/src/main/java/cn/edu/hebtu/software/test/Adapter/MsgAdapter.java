package cn.edu.hebtu.software.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.DetailActivity.OtherMsgActivity;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Util.DateUtil;

/**
 * @ProjectName: MoJi
 * @Description: 点滴留言
 * @Author: 李晓萌
 * @CreateDate: 2019/11/27$ 14:34$
 * @Version: 1.0
 */
public class MsgAdapter extends BaseAdapter {
    private List<Note> noteList = new ArrayList<>();//数据源
    private int itemLayoutId;
    private Context context;
    private String ip;

    public MsgAdapter(Context context, List<Note> noteList, int itemLayoutId) {
        this.noteList = noteList;
        this.itemLayoutId = itemLayoutId;
        this.context = context;
    }


    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    @Override
    public int getCount() {
        if (null!=noteList) {
            return noteList.size();//返回List里的元素个数，严格是要判空
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (null!=noteList) {
            return noteList.get(position);
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
        MyApplication data = (MyApplication)context.getApplicationContext();
        double latitude = 0,longitude = 0;
        if(data.getLatitude()!=null){
            latitude = data.getLatitude();
            longitude = data.getLongitude();
        }
        ip = data.getIp();
        ViewHolder viewHolder = null;
        if (null == convertView){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(itemLayoutId,null);
            viewHolder.headphoto = convertView.findViewById(R.id.iv_headphoto);
            viewHolder.tvPersonalMsg = convertView.findViewById(R.id.tv_personalmsg);
            viewHolder.ivMsgPic = convertView.findViewById(R.id.iv_msgpic);
            viewHolder.tvTime = convertView.findViewById(R.id.tv_time);
            viewHolder.tvKilo = convertView.findViewById(R.id.tv_kilo);
            viewHolder.tvDate = convertView.findViewById(R.id.tv_preDate);
            viewHolder.userMsg = convertView.findViewById(R.id.ll_headmsg);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String str = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(noteList.get(position).getTime());
            DateUtil.DateComponents dateComponents = DateUtil.calculateDateDeltaYMD(date, new Date());
            if(dateComponents.year > 0){
                str = dateComponents.year + "年前";
            }else if(dateComponents.month > 0){
                str = dateComponents.month + "月前";
            }else if(dateComponents.day > 1){
                str = dateComponents.day-1 + "天前";
            }else{
                str = "今天";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        RequestOptions options = new RequestOptions().circleCrop();
        if(noteList != null && noteList.size() > 0){
            if(null == noteList.get(position).getUser().getUserHeadImg() ){
                 viewHolder.headphoto.setImageResource(R.drawable.headportrait);
            }else {
                Glide.with(convertView).load("http://" + ip + ":8080/MoJi/" + noteList.get(position).getUser().getUserHeadImg()).apply(options).into(viewHolder.headphoto);
            }
            if(noteList.get(position).getImgList().size() > 0) {
                Glide.with(convertView).load("http://" + ip + ":8080/MoJi/" + noteList.get(position).getImgList().get(0)).into(viewHolder.ivMsgPic);
            }else{
                viewHolder.ivMsgPic.setImageResource(R.mipmap.default_bg);
            }
            viewHolder.tvPersonalMsg.setText(noteList.get(position).getUser().getUserName());
            String distance = getDistance(noteList.get(position).getLongitude(), noteList.get(position).getLatitude(),longitude,latitude );
            viewHolder.tvKilo.setText(distance);
            viewHolder.tvDate.setText(noteList.get(position).getTime().substring(0,7));
            viewHolder.tvTime.setText(str);
            CustomerButtonListener listener = new CustomerButtonListener(position);
            viewHolder.userMsg.setOnClickListener(listener);
        }

        return convertView;
    }

    final static class ViewHolder{
        private ImageView headphoto;
        private TextView tvPersonalMsg;
        private ImageView ivMsgPic;
        private TextView tvTime;
        private TextView tvKilo;
        private TextView tvDate;
        private RelativeLayout userMsg;
    }

    public  String getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double distance = DistanceUtil.getDistance(new LatLng(latitude1,longitude1),new LatLng(latitude2, longitude2));
        int dkm = (int)distance/1000;
        String  d = null;
        if(dkm > 1){
            d = dkm + "km";
        }else{
            d = (int)distance +"m";
        }
        return d+"";
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/5/15  11:20
     *  @Description: 区分item中的点击事件
     */
    class CustomerButtonListener implements View.OnClickListener{
        private int position;

        public CustomerButtonListener(int position){
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, OtherMsgActivity.class);
            intent.putExtra("user",noteList.get(position).getUser());
            context.startActivity(intent);

        }

    }
}
