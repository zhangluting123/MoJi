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
import android.widget.Toast;

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

import cn.edu.hebtu.software.test.Data.Scene;
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

        convertView = LayoutInflater.from(context).inflate(itemLayout, null);

        RelativeLayout left= convertView.findViewById(R.id.left);
        RelativeLayout right= convertView.findViewById(R.id.right);
        ImageView scene= convertView.findViewById(R.id.iv_scene);
        TextView place = convertView.findViewById(R.id.tv_scene_place);
        TextView content= convertView.findViewById(R.id.tv_scene_content);
        ImageView share = convertView.findViewById(R.id.iv_share_scene);
        TextView loveCount= convertView.findViewById(R.id.tv_love_count);
        ImageView love = convertView.findViewById(R.id.iv_love);
        ImageView comment = convertView.findViewById(R.id.iv_comment);
        ImageView scene2= convertView.findViewById(R.id.iv_scene2);
        TextView place2 = convertView.findViewById(R.id.tv_scene_place_2);
        TextView content2 = convertView.findViewById(R.id.tv_scene_content2);

        //设置位置
        if(position % 2 == 0){
            left.setVisibility(View.VISIBLE);
            right.setVisibility(View.GONE);
            Glide.with(context).load("http://"+ip+":8080/MoJi/"+sceneList.get(position).getPath()).into(scene);
            place.setText(sceneList.get(position).getPlace());
            content.setText(sceneList.get(position).getContent());

        }else{
            left.setVisibility(View.GONE);
            right.setVisibility(View.VISIBLE);
            Glide.with(context).load("http://"+ip+":8080/MoJi/"+sceneList.get(position).getPath()).into(scene2);
            place2.setText(sceneList.get(position).getPlace());
            content2.setText(sceneList.get(position).getContent());
        }

        love.setImageResource(R.drawable.love);
        loveCount.setText(NumStrUtil.getNumStr(sceneList.get(position).getLike()));

        love.setImageResource(R.drawable.love);
        loveCount.setText(NumStrUtil.getNumStr(sceneList.get(position).getLike()));

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareScene(position);
            }
        });

        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Scene scene = sceneList.get(position);
                if (love.getDrawable().getCurrent().getConstantState() == context.getResources().getDrawable(R.drawable.love).getConstantState()) {
                    love.setImageResource(R.drawable.nolove);
                    scene.setLike(scene.getLike()+1);
                    loveCount.setText(NumStrUtil.getNumStr(scene.getLike()));
                    changeLove(true,scene.getId());
                }else{
                    love.setImageResource(R.drawable.love);
                    scene.setLike(scene.getLike()-1);
                    loveCount.setText(NumStrUtil.getNumStr(scene.getLike()));
                    changeLove(false,scene.getId());
                }
            }
        });

        return convertView;
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
