package cn.edu.hebtu.software.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zhy.base.fileprovider.FileProvider7;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import androidx.core.widget.ImageViewCompat;
import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.Data.UserLike;
import cn.edu.hebtu.software.test.Data.Video;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;
import cn.edu.hebtu.software.test.Util.NumStrUtil;
import cn.edu.hebtu.software.test.Util.ShareImage;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @ProjectName:    MoJi
 * @Description:    我的点赞适配器
 * @Author:         张璐婷
 * @CreateDate:     2020/5/23 11:27
 * @Version:        1.0
 */
public class MyLikeAdapter extends BaseAdapter {
    public static final int TYPE_FIRST = 0;//视频
    public static final int TYPE_SECOND = 1;//图文
    private Context context;
    private List<UserLike> userLikeList;
    private MyApplication data;
    private String ip;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(context, (CharSequence) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    public MyLikeAdapter() {
    }

    public MyLikeAdapter(Context context, List<UserLike> userLikeList) {
        this.context = context;
        this.userLikeList = userLikeList;
        data = (MyApplication)context.getApplicationContext();
        ip = data.getIp();
    }

    @Override
    public int getItemViewType(int position) {
        if (null != userLikeList.get(position).getNoteLike().getNoteId()) {
            return TYPE_SECOND;
        } else {
            return TYPE_FIRST;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        if(null != userLikeList){
            return userLikeList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
       if(null != userLikeList){
           return userLikeList.get(position);
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

        switch (getItemViewType(position)) {
            case TYPE_FIRST:
                convertView = LayoutInflater.from(context).inflate(R.layout.item_my_like_first, null);
                TextView videoTitle = convertView.findViewById(R.id.tv_video_title);
                JCVideoPlayerStandard videoPlayer = convertView.findViewById(R.id.video_player);
                ImageView comment = convertView.findViewById(R.id.comment);
                ImageView share = convertView.findViewById(R.id.share);
                TextView goodNum = convertView.findViewById(R.id.tv_good_num);
                ImageView good = convertView.findViewById(R.id.good);
                Video video = userLikeList.get(position).getVideoLike();
                String path = "http://"+ ip +":8080/MoJi/"+ video.getPath();
                videoTitle.setText(video.getTitle());
                good.setImageResource(R.drawable.nogood);

                String numStr = NumStrUtil.getNumStr(video.getLike());
                goodNum.setText(numStr);

                videoPlayer.setUp(path, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "MoJi");
                Glide.with(context)
                        .setDefaultRequestOptions(
                                new RequestOptions()
                                        .frame(4000000)
                                        .centerCrop()
                                        .error(R.drawable.fail)
                                        .placeholder(R.drawable.fail)
                        )
                        .load(path)
                        .into(videoPlayer.thumbImageView);

                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareVideo(path);
                    }
                });
                good.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //视频点赞
                        Video video = userLikeList.get(position).getVideoLike();
                        if(good.getDrawable().getCurrent().getConstantState() == context.getResources().getDrawable(R.drawable.good).getConstantState()){
                            changeGood(true, video.getVideoId(), null);
                            good.setImageResource(R.drawable.nogood);
                            video.setLike(video.getLike()+1);
                            goodNum.setText(NumStrUtil.getNumStr(video.getLike()));
                        }else{
                            changeGood(false, video.getVideoId(), userLikeList.get(position).getId());
                            good.setImageResource(R.drawable.good);
                            video.setLike(video.getLike()-1);
                            goodNum.setText(NumStrUtil.getNumStr(video.getLike()));
                        }
                    }
                });

                break;
            case TYPE_SECOND:
                convertView = LayoutInflater.from(context).inflate(R.layout.item_my_like_second, null);
                TextView noteTitle = convertView.findViewById(R.id.tv_note_title);
                TextView noteContent = convertView.findViewById(R.id.tv_note_content);
                ImageView img1 = convertView.findViewById(R.id.img1);
                ImageView img2 = convertView.findViewById(R.id.img2);
                ImageView img3 = convertView.findViewById(R.id.img3);
                ImageView shareNote = convertView.findViewById(R.id.iv_share_note);
                TextView loveCount = convertView.findViewById(R.id.tv_love_count);
                ImageView love = convertView.findViewById(R.id.iv_love);
//                secondViewHolder.img1.setImageDrawable(null);
//                secondViewHolder.img2.setImageDrawable(null);
//                secondViewHolder.img3.setImageDrawable(null);

                Note note = userLikeList.get(position).getNoteLike();
                noteTitle.setText(note.getTitle());
                noteContent.setText(note.getContent());

                String numStr2 = NumStrUtil.getNumStr(note.getLike());
                loveCount.setText(numStr2);
                for(int i = 0; i < note.getImgList().size() && i < 3;i++){
                    switch (i){
                        case 0:
                            Glide.with(context).load("http://"+ ip +":8080/MoJi/"+note.getImgList().get(0)).into(img1);
                            break;
                        case 1:
                            Glide.with(context).load("http://"+ ip +":8080/MoJi/"+note.getImgList().get(1)).into(img2);
                            break;
                        case 2:
                            Glide.with(context).load("http://"+ ip +":8080/MoJi/"+note.getImgList().get(2)).into(img3);
                            break;
                    }
                }
                love.setImageResource(R.drawable.nolove);

                shareNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareNote(position);
                    }
                });
                love.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Note note = userLikeList.get(position).getNoteLike();
                        if (love.getDrawable().getCurrent().getConstantState() == context.getResources().getDrawable(R.drawable.love).getConstantState()) {
                            //点赞
                            changeLove(true, note.getNoteId(), null);
                            love.setImageResource(R.drawable.nolove);
                            note.setLike(note.getLike() + 1);
                            loveCount.setText(NumStrUtil.getNumStr(note.getLike()));
                        } else {//取消点赞
                            changeLove(false, note.getNoteId(), userLikeList.get(position).getId());
                            love.setImageResource(R.drawable.love);
                            note.setLike(note.getLike() - 1);
                            loveCount.setText(NumStrUtil.getNumStr(note.getLike()));
                        }
                    }
                });
        }


        return convertView;
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
     *  @time: 2020/4/21  21:02
     *  @Description: 分享Note
     */
    private void shareNote(int position){
        new Thread(){
            @Override
            public void run() {
                Bitmap head = null;
                Note note = userLikeList.get(position).getNoteLike();
                try {
                    URL url = new URL("http://"+ip+":8080/MoJi/"+note.getUser().getUserHeadImg());
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    head = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bitmap newImg = ShareImage.drawTextAtBitmap(context,head,note.getUser().getUserName(),note.getContent());
                //分享图片
                File filePath = ShareImage.saveImageToGallery(context, newImg);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                Uri uri = FileProvider7.getUriForFile(context,filePath);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, uri);

                // 目标应用选择对话框的标题
                context.startActivity(Intent.createChooser(intent, "请选择"));
            }
        }.start();

    }



    /**
     *  @author: 张璐婷
     *  @time: 2020/5/22  14:59
     *  @Description: 点赞 或 取消点赞
     *  flag = true 点赞
     *  flag = false 取消点赞
     */
    public void changeLove(boolean flag,String noteId,String likeId) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (DetermineConnServer.isConnByHttp(context.getApplicationContext())) {
                        String urlString = null;
                        if (flag) {
                            urlString = "http://" + ip + ":8080/MoJi/userLike/addLike?userId=" + data.getUser().getUserId() + "&noteId=" + noteId;
                        } else {
                            urlString = "http://" + ip + ":8080/MoJi/userLike/deleteLike?likeId=" + likeId + "&noteId=" + noteId;
                        }
                        URL url = new URL(urlString);
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String str = reader.readLine();
                        Message message = Message.obtain();
                        if (str.equals("OK")) {
                            message.what = 1001;
                            message.obj = "取消成功";
                        } else if (str.equals("ERROR")) {
                            message.what = 1001;
                            message.obj = "更改失败";
                        } else {
                            message.what = 1002;
                            message.obj = str;
                        }
                        handler.sendMessage(message);
                        in.close();
                        reader.close();
                    } else {
                        Message message = Message.obtain();
                        message.what = 1001;
                        message.obj = "未连接到服务器";
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

    /**
     *  @author: 张璐婷
     *  @time: 2020/5/23  8:58
     *  @Description: 点赞 或 取消点赞 Video
     */
    public void changeGood(boolean flag,String videoId,String likeId) {
        new Thread() {
            @Override
            public void run() {
                try {
                    if (DetermineConnServer.isConnByHttp(context.getApplicationContext())) {
                        String urlString = null;
                        if (flag) {
                            urlString = "http://" + ip + ":8080/MoJi/userLike/addLike?userId=" + data.getUser().getUserId() + "&videoId=" + videoId;
                        } else {
                            urlString = "http://" + ip + ":8080/MoJi/userLike/deleteLike?likeId=" + likeId + "&videoId=" + videoId;
                        }
                        URL url = new URL(urlString);
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String str = reader.readLine();
                        Message message = Message.obtain();
                        if (str.equals("OK")) {
                            message.what = 1001;
                            message.obj = "取消成功";
                        } else if (str.equals("ERROR")) {
                            message.what = 1001;
                            message.obj = "更改失败";
                        } else {
                            message.what = 1002;
                            message.obj = str;
                        }
                        handler.sendMessage(message);
                        in.close();
                        reader.close();
                    } else {
                        Message message = Message.obtain();
                        message.what = 1001;
                        message.obj = "未连接到服务器";
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

    public void refresh(List<UserLike> userLikes){
        this.userLikeList = userLikes;
        notifyDataSetChanged();
    }
}
