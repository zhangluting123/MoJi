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

    private FirstViewHolder firstViewHolder ;
    private SecondViewHolder secondViewHolder ;
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
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_my_like_first, null);
                    firstViewHolder = new FirstViewHolder();
                    firstViewHolder.videoTitle = convertView.findViewById(R.id.tv_video_title);
                    firstViewHolder.videoPlayer = convertView.findViewById(R.id.video_player);
                    firstViewHolder.share = convertView.findViewById(R.id.share);
                    firstViewHolder.goodNum = convertView.findViewById(R.id.tv_good_num);
                    firstViewHolder.good = convertView.findViewById(R.id.good);
                    firstViewHolder.comment = convertView.findViewById(R.id.comment);
                    convertView.setTag(firstViewHolder);
                } else {
                    firstViewHolder = (FirstViewHolder) convertView.getTag();
                }
                Video video = userLikeList.get(position).getVideoLike();
                String path = "http://"+ ip +":8080/MoJi/"+ video.getPath();
                firstViewHolder.videoTitle.setText(video.getTitle());
                firstViewHolder.good.setImageResource(R.drawable.nogood);

                String numStr = NumStrUtil.getNumStr(video.getLike());
                firstViewHolder.goodNum.setText(numStr);

                firstViewHolder.videoPlayer.setUp(path, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "MoJi");
                Glide.with(context)
                        .setDefaultRequestOptions(
                                new RequestOptions()
                                        .frame(4000000)
                                        .centerCrop()
                                        .error(R.drawable.fail)
                                        .placeholder(R.drawable.fail)
                        )
                        .load(path)
                        .into(firstViewHolder.videoPlayer.thumbImageView);

                CustomeOnClickListener listener = new CustomeOnClickListener(position,path);
                firstViewHolder.share.setOnClickListener(listener);
                firstViewHolder.good.setOnClickListener(listener);

                break;
            case TYPE_SECOND:
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_my_like_second, null);
                    secondViewHolder = new SecondViewHolder();
                    secondViewHolder.noteTitle = convertView.findViewById(R.id.tv_note_title);
                    secondViewHolder.noteContent = convertView.findViewById(R.id.tv_note_content);
                    secondViewHolder.img1 = convertView.findViewById(R.id.img1);
                    secondViewHolder.img2 = convertView.findViewById(R.id.img2);
                    secondViewHolder.img3 = convertView.findViewById(R.id.img3);
                    secondViewHolder.shareNote = convertView.findViewById(R.id.iv_share_note);
                    secondViewHolder.loveCount = convertView.findViewById(R.id.tv_love_count);
                    secondViewHolder.love = convertView.findViewById(R.id.iv_love);
                    convertView.setTag(secondViewHolder);
                } else {
                    secondViewHolder = (SecondViewHolder) convertView.getTag();
                }
                secondViewHolder.img1.setImageDrawable(null);
                secondViewHolder.img2.setImageDrawable(null);
                secondViewHolder.img3.setImageDrawable(null);

                Note note = userLikeList.get(position).getNoteLike();
                secondViewHolder.noteTitle.setText(note.getTitle());
                secondViewHolder.noteContent.setText(note.getContent());

                String numStr2 = NumStrUtil.getNumStr(note.getLike());
                secondViewHolder.loveCount.setText(numStr2);
                for(int i = 0; i < note.getImgList().size() && i < 3;i++){
                    switch (i){
                        case 0:
                            Glide.with(context).load("http://"+ ip +":8080/MoJi/"+note.getImgList().get(0)).into(secondViewHolder.img1);
                            break;
                        case 1:
                            Glide.with(context).load("http://"+ ip +":8080/MoJi/"+note.getImgList().get(1)).into(secondViewHolder.img2);
                            break;
                        case 2:
                            Glide.with(context).load("http://"+ ip +":8080/MoJi/"+note.getImgList().get(2)).into(secondViewHolder.img3);
                            break;
                    }
                }
                secondViewHolder.love.setImageResource(R.drawable.nolove);

                CustomeOnClickListener listener2 = new CustomeOnClickListener(position,null);
                secondViewHolder.shareNote.setOnClickListener(listener2);
                secondViewHolder.love.setOnClickListener(listener2);
                break;
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
                case R.id.good:
                    //视频点赞
                    Video video = userLikeList.get(position).getVideoLike();
                    if(firstViewHolder.good.getDrawable().getCurrent().getConstantState() == context.getResources().getDrawable(R.drawable.good).getConstantState()){
                        changeGood(true, video.getVideoId(), null);
                        firstViewHolder.good.setImageResource(R.drawable.nogood);
                        video.setLike(video.getLike()+1);
                        firstViewHolder.goodNum.setText(NumStrUtil.getNumStr(video.getLike()));
                    }else{
                        changeGood(false, video.getVideoId(), userLikeList.get(position).getId());
                        firstViewHolder.good.setImageResource(R.drawable.good);
                        video.setLike(video.getLike()-1);
                        firstViewHolder.goodNum.setText(NumStrUtil.getNumStr(video.getLike()));
                    }
                    break;
                case R.id.iv_share_note:
                    shareNote(position);
                    break;
                case R.id.iv_love:
                    Note note = userLikeList.get(position).getNoteLike();
                    if(secondViewHolder.love.getDrawable().getCurrent().getConstantState() == context.getResources().getDrawable(R.drawable.love).getConstantState()){
                        //点赞
                        changeLove(true,note.getNoteId(),null);
                        secondViewHolder.love.setImageResource(R.drawable.nolove);
                        note.setLike(note.getLike()+1);
                        secondViewHolder.loveCount.setText(NumStrUtil.getNumStr(note.getLike()));
                    }else{//取消点赞
                        changeLove(false, note.getNoteId(),userLikeList.get(position).getId());
                        secondViewHolder.love.setImageResource(R.drawable.love);
                        note.setLike(note.getLike()-1);
                        secondViewHolder.loveCount.setText(NumStrUtil.getNumStr(note.getLike()));
                    }
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


    final static class FirstViewHolder{
        TextView videoTitle;
        JCVideoPlayerStandard videoPlayer;
        ImageView share;
        TextView goodNum;
        ImageView good;
        ImageView comment;
    }

    final static class SecondViewHolder{
        TextView noteTitle;
        TextView noteContent;
        ImageView img1;
        ImageView img2;
        ImageView img3;
        ImageView shareNote;
        TextView loveCount;
        ImageView love;
        ImageView comment;
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
