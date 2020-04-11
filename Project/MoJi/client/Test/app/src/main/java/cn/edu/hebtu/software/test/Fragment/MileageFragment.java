package cn.edu.hebtu.software.test.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;

/**
 * @ProjectName:    MoJi
 * @Description:    里程界面
 * @Author:         王佳成
 * @CreateDate:     2019/11/27 15:40
 * @Version:        1.0
 */
public class MileageFragment extends Fragment {
    private RecyclerView recycler_view;
    private MyRecycleAdapter adapter;
    private List<Note> noteList = new ArrayList<>();
    private MyApplication data;
    private String ip;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(getActivity().getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mileage_layout,container,false);
        TextView title = getActivity().findViewById(R.id.toorbar_title);
        title.setText("里程");

        data = (MyApplication)getActivity().getApplication();
        ip = data.getIp();


        recycler_view = view.findViewById(R.id.recycler_view);
        initData();

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        adapter = new MyRecycleAdapter(getActivity(),noteList);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setAdapter(adapter);

        return  view;
    }


    public void initData(){
        Thread getNote = new GetNote();
        getNote.start();
        try {
            getNote.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyViewHolder> {
        private LayoutInflater inflater;
        private List<Note> list;
        public MyRecycleAdapter(Context context, List<Note> list) {
            inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_mileage, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
                holder.tvTime.setText(list.get(position).getTime());
                holder.tvText.setText(list.get(position).getLocation());
                if(list.get(position).getImgList().size() > 0){
                    Glide.with(getActivity().getApplication()).load("http://"+ip+":8080/MoJi/"+list.get(position).getImgList().get(0)).into(holder.pic);
                }else{
                    holder.pic.setImageResource(R.mipmap.default_bg);
                }
                if (position != getItemCount()-1){
                    holder.tvUnderLine.setBackgroundResource(R.mipmap.ruler3);
                }

        }

        @Override
        public int getItemCount() {
            if(list != null ){
                return list.size();
            }else{
                return 0;
            }

        }

        public  class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvTime, tvText;
            ImageView tvUnderLine;
            ImageView pic;
            public MyViewHolder(View itemView) {
                super(itemView);
                tvTime = itemView.findViewById(R.id.tvTime);
                tvText = itemView.findViewById(R.id.tvText);
                pic = itemView.findViewById(R.id.pic);
                tvUnderLine = itemView.findViewById(R.id.tvUnderLine);
            }
        }
    }


    class GetNote extends Thread{
        @Override
        public void run() {
            try {
                boolean b = DetermineConnServer.isConnByHttp(getActivity().getApplicationContext());
                if(b){
                    URL url = new URL("http://"+getResources().getString(R.string.internet_ip)+":8080/MoJi/DownloadNoteServlet?userId=" + data.getUser().getUserId());
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                    String str = reader.readLine();
                    if(null != str){
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Note>>(){}.getType();
                        noteList = gson.fromJson(str,type);
                        sort();
                    }else{
                        Note note  = new Note();
                        List<String> imgList = new ArrayList<>();
                        imgList.add("image/default_bg.jpg");
                        note.setImgList(imgList);
                        note.setTime("快来添加你的足迹吧~");
                        note.setLocation(data.getLocation());
                        noteList.add(note);
                    }

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
    }

    private void sort(){
        for(int i = 0; i < noteList.size(); i++) {
            Log.e("ago",noteList.get(i).getTime());
        }

        sortClass sort =  new sortClass();
        Collections.sort(noteList,sort);

        for(int i = 0; i < noteList.size(); i++) {
            Log.e("now",noteList.get(i).getTime());
        }
    }

    public class sortClass implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            Note n1 = (Note)o1;
            Note n2 = (Note)o2;
            int flag = ((n1.getTime()).compareTo(n2.getTime()));
            return flag;
        }

    }
}