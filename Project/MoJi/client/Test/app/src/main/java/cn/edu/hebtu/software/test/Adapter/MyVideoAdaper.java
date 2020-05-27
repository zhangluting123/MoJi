package cn.edu.hebtu.software.test.Adapter;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
import com.bumptech.glide.request.RequestOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import cn.edu.hebtu.software.test.Data.Video;
import cn.edu.hebtu.software.test.DetailActivity.VideoDetailActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class MyVideoAdaper extends BaseAdapter {
    private List<Video> list;
    private int itemLayout;
    private Context context;
    private boolean flag;
    private String ip;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(context, (CharSequence) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    Toast.makeText(context, (CharSequence) msg.obj, Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    break;
            }
        }
    };

    public MyVideoAdaper(List<Video> list, int itemLayout, Context context,boolean flag) {
        this.list = list;
        this.itemLayout = itemLayout;
        this.context = context;
        this.flag = flag;
        ip = ((MyApplication)context.getApplicationContext()).getIp();
    }

    @Override
    public int getCount() {
        if(null != list){
            return list.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(null != list){
            return list.get(position);
        }else{
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(itemLayout, null);
            viewHolder.title = convertView.findViewById(R.id.tv_video_title);
            viewHolder.video = convertView.findViewById(R.id.video_player);
            viewHolder.share = convertView.findViewById(R.id.share);
            viewHolder.good = convertView.findViewById(R.id.good);
            viewHolder.comment = convertView.findViewById(R.id.comment);
            viewHolder.delete = convertView.findViewById(R.id.tv_delete);
            viewHolder.goodNum = convertView.findViewById(R.id.tv_good_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        String path = "http://"+ ip +":8080/MoJi/"+list.get(position).getPath();
        viewHolder.title.setText(list.get(position).getTitle());
        String numStr = null;
        int count = list.get(position).getLike();
        if(count < 10000){
            numStr = count + "";
        }else if (count < 100000){
            numStr = count/10000+ "." + count%10000/1000 +"万";
        }else{
            numStr = count/10000 + "万";
        }
        viewHolder.goodNum.setText(numStr+"");
        JCVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;//横向
        JCVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;//纵向
        viewHolder.video.setUp(path, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "MoJi");
        Glide.with(context)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(4000000)
                                .centerCrop()
                                .error(R.drawable.fail)
                                .placeholder(R.drawable.fail)
                )
                .load(path)
                .into(viewHolder.video.thumbImageView);

        CustomeOnClickListener listener = new CustomeOnClickListener(position,path);
        viewHolder.share.setOnClickListener(listener);
        viewHolder.comment.setOnClickListener(listener);
        if(flag) {
            viewHolder.delete.setOnClickListener(listener);
        }else{
            viewHolder.delete.setVisibility(View.GONE);
        }

        return convertView;
    }

    class CustomeOnClickListener implements View.OnClickListener{
        private int position;
        private String path;

        public CustomeOnClickListener(int position,String path) {
            this.position = position;
            this.path = path;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.share:
                    shareVideo(path);
                    break;
                case R.id.tv_delete:
                    deleteVideo(position);
                    break;
                case R.id.comment:
                    Intent intent = new Intent(context, VideoDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("video", list.get(position));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    break;
            }
        }
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/5/15  20:20
     *  @Description: 分享视频链接
     */
    private void shareVideo(String path){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.putExtra(Intent.EXTRA_TEXT,"MoJi视频链接 "+path);
        context.startActivity(shareIntent);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/5/16  11:24
     *  @Description: 删除视频
     */
    private void deleteVideo(int position){
        new Thread(){
            @Override
            public void run() {
                try {
                    boolean b = DetermineConnServer.isConnByHttp(context);
                    if(b){
                        URL url = new URL("http://"+ ip +":8080/MoJi/video/delete?videoId=" + list.get(position).getVideoId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        String str = reader.readLine();
                        Message message = new Message();
                        if(str.equals("1")){
                            message.what = 1002;
                            list.remove(position);
                            message.obj ="删除成功";
                        }else{
                            message.what = 1001;
                            message.obj = "删除失败";
                        }
                        handler.sendMessage(message);
                        in.close();
                        reader.close();
                    }else{
                        Message message = new Message();
                        message.what = 1001;
                        message.obj ="未连接到服务器";
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


    final static class ViewHolder{
        TextView title;
        JCVideoPlayerStandard video;
        ImageView share;
        ImageView good;
        ImageView comment;
        TextView delete;
        TextView goodNum;
    }

    public void refresh(List<Video> videoList){
        this.list = videoList;
        notifyDataSetChanged();
    }
}
