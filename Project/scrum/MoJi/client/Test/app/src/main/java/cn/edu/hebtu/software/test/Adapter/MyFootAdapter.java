package cn.edu.hebtu.software.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.DetailActivity.ShowNoteActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;

public class MyFootAdapter extends BaseAdapter {
    private int itemLayoutId;
    private Context context;
    private List<Note> noteList;
    private String ip;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    Toast.makeText(context.getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 200:
                    int pos = (int) msg.obj;
                    noteList.remove(pos);
                    notifyDataSetChanged();
                    break;
            }
        }
    };

    public MyFootAdapter() {

    }

    public MyFootAdapter(int itemLayoutId, Context context, List<Note> noteList) {
        this.itemLayoutId = itemLayoutId;
        this.context = context;
        this.noteList = noteList;
    }

    @Override
    public int getCount() {
        if(null != noteList){
            return noteList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(null != noteList.get(position)){
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
        final MyApplication data = (MyApplication) context.getApplicationContext();
        ip = data.getIp();
        ViewHolder viewHolder = null;
        if(null == convertView){
            convertView = LayoutInflater.from(context).inflate(itemLayoutId,null);
            viewHolder = new ViewHolder();
            viewHolder.noteImg = convertView.findViewById(R.id.iv_noteImg);
            viewHolder.noteTime = convertView.findViewById(R.id.tv_noteTime);
            viewHolder.noteAddress= convertView.findViewById(R.id.tv_noteAddress);
            viewHolder.noteDelete = convertView.findViewById(R.id.tv_delete);
            viewHolder.linearFootDetail = convertView.findViewById(R.id.linearFootDetail);

            viewHolder.linearFootDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final MyApplication data = (MyApplication) context.getApplicationContext();
                    data.setCurrentNoteId(noteList.get(position).getNoteId());

                    Intent intent = new Intent(context, ShowNoteActivity.class);
                    Gson gson = new Gson();
                    String noteStr = gson.toJson(noteList.get(position));
                    intent.putExtra("noteJsonStr", noteStr);
                    context.startActivity(intent);
                }
            });

            viewHolder.noteDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                boolean b = DetermineConnServer.isConnByHttp(context.getApplicationContext());
                                if(b){
                                    URL url = new URL("http://" + ip +":8080/MoJi/DeleteNoteServlet?noteId=" + noteList.get(position).getNoteId());
                                    URLConnection conn = url.openConnection();
                                    InputStream in = conn.getInputStream();
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                                    String str = reader.readLine();
                                    if("1".equals(str)){
                                        Message message = new Message();
                                        message.what = 200;
                                        int pos = position;
                                        message.obj = pos;
                                        handler.sendMessage(message);
                                    }else {
                                        Message message = new Message();
                                        message.what = 100;
                                        message.obj ="删除足迹出错";
                                        handler.sendMessage(message);
                                    }
                                    in.close();
                                    reader.close();
                                }else{
                                    Message message = new Message();
                                    message.what = 100;
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
            });

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(noteList.get(position).getImgList().size() > 0) {
            Glide.with(convertView).load("http://" + ip + ":8080/MoJi/" + noteList.get(position).getImgList().get(0)).into(viewHolder.noteImg);
        }else {
            viewHolder.noteImg.setImageResource(R.color.MyTheam_color);
        }
        viewHolder.noteTime.setText(noteList.get(position).getTime());
        viewHolder.noteAddress.setText(noteList.get(position).getLocation());

        return convertView;
    }

    final static class ViewHolder{
        private ImageView noteImg;
        private TextView noteTime;
        private TextView noteAddress;
        private TextView noteDelete;
        private LinearLayout linearFootDetail;
    }
}
