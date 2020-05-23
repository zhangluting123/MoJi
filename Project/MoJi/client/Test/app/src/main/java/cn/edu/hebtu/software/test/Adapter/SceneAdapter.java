package cn.edu.hebtu.software.test.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.mob.tools.RxMob;
import com.zhy.base.fileprovider.FileProvider7;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cn.edu.hebtu.software.test.Data.Scene;
import cn.edu.hebtu.software.test.Data.Video;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;
import cn.edu.hebtu.software.test.Util.NumStrUtil;

/**
 * @ProjectName:    MoJi
 * @Description:    景点适配器
 * @Author:         张璐婷
 * @CreateDate:     2020/5/23 11:26
 * @Version:        1.0
 */
public class SceneAdapter extends BaseAdapter {
    private Context context;
    private List<Scene> sceneList;
    private int itemLayout;
    private String ip;
    private ViewHolder viewHolder;

    public SceneAdapter() {
    }

    public SceneAdapter(Context context, List<Scene> sceneList, int itemLayout) {
        this.context = context;
        this.sceneList = sceneList;
        this.itemLayout = itemLayout;
        MyApplication data = (MyApplication)context.getApplicationContext();
        ip = data.getIp();
    }

    @Override
    public int getCount() {
        if(null != sceneList){
            return sceneList.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(null != sceneList){
            return sceneList.get(position);
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
        if(null == convertView){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(itemLayout, null);
            viewHolder.left= convertView.findViewById(R.id.left);
            viewHolder.right = convertView.findViewById(R.id.right);
            viewHolder.scene = convertView.findViewById(R.id.iv_scene);
            viewHolder.place = convertView.findViewById(R.id.tv_scene_place);
            viewHolder.content = convertView.findViewById(R.id.tv_scene_content);
            viewHolder.share = convertView.findViewById(R.id.iv_share_scene);
            viewHolder.loveCount = convertView.findViewById(R.id.tv_love_count);
            viewHolder.love = convertView.findViewById(R.id.iv_love);
            viewHolder.comment = convertView.findViewById(R.id.iv_comment);
            viewHolder.scene2 = convertView.findViewById(R.id.iv_scene2);
            viewHolder.place2 = convertView.findViewById(R.id.tv_scene_place_2);
            viewHolder.content2 = convertView.findViewById(R.id.tv_scene_content2);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //设置位置
        if(position % 2 == 0){
            viewHolder.left.setVisibility(View.VISIBLE);
            viewHolder.right.setVisibility(View.GONE);
            Glide.with(context).load("http://"+ip+":8080/MoJi/"+sceneList.get(position).getPath()).into(viewHolder.scene);
            viewHolder.place.setText(sceneList.get(position).getPlace());
            viewHolder.content.setText(sceneList.get(position).getContent());

        }else{
            viewHolder.left.setVisibility(View.GONE);
            viewHolder.right.setVisibility(View.VISIBLE);
            Glide.with(context).load("http://"+ip+":8080/MoJi/"+sceneList.get(position).getPath()).into(viewHolder.scene2);
            viewHolder.place2.setText(sceneList.get(position).getPlace());
            viewHolder.content2.setText(sceneList.get(position).getContent());
        }

        viewHolder.loveCount.setText(NumStrUtil.getNumStr(sceneList.get(position).getLike()));
        CustomOnClickListener listener = new CustomOnClickListener(position);
        viewHolder.share.setOnClickListener(listener);
        viewHolder.love.setOnClickListener(listener);

        return convertView;
    }

    class CustomOnClickListener implements View.OnClickListener {
        private int position;

        public CustomOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_share_scene:
                    shareScene(position);
                    break;
                case R.id.iv_love:
                    Scene scene = sceneList.get(position);
                    if (viewHolder.love.getDrawable().getCurrent().getConstantState() == context.getResources().getDrawable(R.drawable.love).getConstantState()) {
                        changeLove(true,scene.getId());
                        viewHolder.love.setImageResource(R.drawable.nolove);
                        scene.setLike(scene.getLike()+1);
                        viewHolder.loveCount.setText(NumStrUtil.getNumStr(scene.getLike()));
                    }else{
                        changeLove(false,scene.getId());
                        viewHolder.love.setImageResource(R.drawable.love);
                        scene.setLike(scene.getLike()-1);
                        viewHolder.loveCount.setText(NumStrUtil.getNumStr(scene.getLike()));
                    }
                    break;
            }
        }
    }


    /**
     *  @author: 张璐婷
     *  @time: 2020/5/23  13:02
     *  @Description: 分享景点
     */
    private void shareScene(int position) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        String path  = "http://"+ip+":8080/MoJi/"+sceneList.get(position).getPath();
        shareIntent.putExtra(Intent.EXTRA_TEXT,"MoJi景点推荐: "+sceneList.get(position).getPlace()+"\n"
                +"简介："+sceneList.get(position).getContent()+"\n"
                +"图片链接："+path);
        context.startActivity(shareIntent);
    }

    final static class ViewHolder{
        RelativeLayout left;
        RelativeLayout right;
        ImageView scene;
        TextView place;
        TextView content;
        ImageView share;
        TextView loveCount;
        ImageView love;
        ImageView comment;
        ImageView scene2;
        TextView place2;
        TextView content2;

    }

    private void changeLove(boolean flag, Integer id) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (DetermineConnServer.isConnByHttp(context.getApplicationContext())) {
                        String urlString = null;
                        if (flag) {
                            urlString = "http://" + ip + ":8080/MoJi/scene/addLike?sceneId=" + id;
                        } else {
                            urlString = "http://" + ip + ":8080/MoJi/scene/deleteLike?sceneId=" + id;
                        }
                        URL url = new URL(urlString);
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        in.close();
                        reader.close();
                    } else {
                        Log.e("SceneAdapter", "未连接到服务器");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void replaceAll(List<Scene> sceneList){
        this.sceneList = sceneList;
        notifyDataSetChanged();
    }

    public void addAll(List<Scene> sceneList){
        this.sceneList.addAll(sceneList);
        notifyDataSetChanged();
    }
}
