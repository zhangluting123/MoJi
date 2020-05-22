package cn.edu.hebtu.software.test.Adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mob.tools.RxMob;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.Data.UserLike;
import cn.edu.hebtu.software.test.DetailActivity.OtherMsgActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;

import static com.baidu.mapapi.BMapManager.getContext;

/**
 * @Author: 邸祯策
 * @Date: 2019/04/23
 * @Describe:
 */

public class StaggeredGridAdapter extends RecyclerView.Adapter<StaggeredGridAdapter.LinearViewHolder>{

    private Context mContext;
    private AdapterView.OnItemClickListener mListener;
    private List<Note> noteList = new ArrayList<>();//数据源
    private List<UserLike> userLikes = new ArrayList<>();
    private Map<Integer,String> map = new HashMap<>();
    private User user;
    private String ip;
    private int pos;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(mContext, (CharSequence) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    map.put(pos, (String) msg.obj);
                    break;
                case 1003:
                    Toast.makeText(mContext, (CharSequence) msg.obj, Toast.LENGTH_SHORT).show();
                    map.remove(pos);
                    break;
            }
        }
    };

    public StaggeredGridAdapter(Context mContext,List<Note> noteList,List<UserLike> userLikes) {
        this.mContext = mContext;
        this.noteList = noteList;
        this.userLikes = userLikes;
        MyApplication data = (MyApplication)mContext.getApplicationContext();
        ip = data.getIp();
        user = data.getUser();
    }

    public void replaceAll(List<Note> newNoteList,List<UserLike> likeList) {
        noteList.clear();
        userLikes.clear();
        if (newNoteList != null && newNoteList.size() > 0) {
            noteList.addAll(newNoteList);
        }
        if(likeList != null && likeList.size() > 0){
            userLikes.addAll(likeList);
        }
        notifyDataSetChanged();
    }

    /**
     * 插入数据使用notifyItemInserted，如果要使用插入动画，必须使用notifyItemInserted
     * 才会有效果。即便不需要使用插入动画，也建议使用notifyItemInserted方式添加数据，
     * 不然容易出现闪动和间距错乱的问题
     * */
    public void addData(int position,List<Note> addNoteList) {
        noteList.addAll(position,addNoteList);
        notifyItemInserted(position);
    }

    //移除数据使用notifyItemRemoved
    public void removeData(int position) {
        noteList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public StaggeredGridAdapter.LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_staggered_grid_item,parent,false));
    }

    @Override
    public void onBindViewHolder(StaggeredGridAdapter.LinearViewHolder holder, int position) {
        holder.setData(noteList.get(position),position);
        holder.itemView.findViewById(R.id.iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    longClickListener.onClick(position);
                }
                return true;
            }
        });
        //点击头像弹出个人信息
        holder.roundedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OtherMsgActivity.class);
                intent.putExtra("user",noteList.get(position).getUser());
                mContext.startActivity(intent);
            }
        });
        //点赞
        holder.love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != user.getUserId()){
                    pos = position;
                    if(holder.love.getDrawable().getCurrent().getConstantState() == mContext.getResources().getDrawable(R.drawable.love).getConstantState()){
                        //点赞
                        changeLove(true,noteList.get(position).getNoteId(),null);
                        holder.love.setImageResource(R.drawable.nolove);
                        noteList.get(position).setLike(noteList.get(position).getLike()+1);
                        holder.loveCount.setText(noteList.get(position).getLike()+"");
                    }else{//取消点赞
                        changeLove(false, noteList.get(position).getNoteId(),map.get(position));
                        holder.love.setImageResource(R.drawable.love);
                        noteList.get(position).setLike(noteList.get(position).getLike()-1);
                        holder.loveCount.setText(noteList.get(position).getLike()+"");
                    }
                }else{
                    Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList != null ? noteList.size() : 0;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private CardView mCardView;
        private RoundedImageView roundedImageView;
        private TextView title;
        private TextView petname;
        private TextView loveCount;
        private ImageView love;
        public LinearViewHolder(View itemView){
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv);
            mCardView = itemView.findViewById(R.id.card);
            roundedImageView = itemView.findViewById(R.id.head);
            title = itemView.findViewById(R.id.title);
            petname = itemView.findViewById(R.id.petname);
            loveCount = itemView.findViewById(R.id.tv_love_count);
            love = itemView.findViewById(R.id.love);
        }

        void setData(Object data,int position) {
            if (data != null) {
                Note note = (Note)data;
                if(null == note.getUser().getUserHeadImg()){
                    roundedImageView.setImageResource(R.drawable.headportrait);
                }else {
                    Glide.with(getContext()).load("http://"+ ip +":8080/MoJi/" + note.getUser().getUserHeadImg()).into(roundedImageView);
                }
                if(note.getImgList().size() > 0){
                    Glide.with(getContext()).load("http://"+ ip +":8080/MoJi/" + note.getImgList().get(0)).into(mImageView);
                }else {
                    mImageView.setImageResource(R.mipmap.default_bg);
                }
                title.setText(note.getTitle());
                petname.setText(note.getUser().getUserName());
                loveCount.setText(note.getLike()+"");
                for(int i = 0;i < userLikes.size();i++){
                    if(userLikes.get(i).getNoteLike().getNoteId().equals(note.getNoteId())){
                        love.setImageResource(R.drawable.nolove);
                        map.put(position, userLikes.get(i).getId());
                        break;
                    }
                }
            }
        }


    }

    //定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener listener;

    //写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemLongClickListener {
        void onClick(int position);
    }

    private OnItemLongClickListener longClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public List<Note> getNoteList() {
        return noteList;
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
                    if (DetermineConnServer.isConnByHttp(mContext.getApplicationContext())) {
                        String urlString = null;
                        if (flag) {
                            urlString = "http://" + ip + ":8080/MoJi/userLike/addLike?userId=" + user.getUserId() + "&noteId=" + noteId;
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
                            message.what = 1003;
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
}
