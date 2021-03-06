package cn.edu.hebtu.software.test.LeftNavigation;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.test.Activity.MainActivity;
import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.ActivityManager;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import java.util.List;

public class TimelineActivity extends AppCompatActivity {
    private RecyclerView recycler_view;
    private MyRecycleAdapter adapter;
    private List<Note> noteList = new ArrayList<>();
    private MyApplication data;
    private Toolbar toolbar;
    private String ip;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ActivityManager.getInstance().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.MyTheam_color));

        }

        data = (MyApplication)getApplication();
        ip = data.getIp();

        recycler_view = findViewById(R.id.recycler_view);
        initData();

        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        adapter = new MyRecycleAdapter(this,noteList);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setAdapter(adapter);

        toolbar = findViewById(R.id.timeline_toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent response = new Intent(TimelineActivity.this, MainActivity.class);
                response.putExtra("flag",true);
                startActivity(response);
                overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                finish();
            }
        });

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
                Glide.with(getApplicationContext()).load("http://"+ip+":8080/MoJi/"+list.get(position).getImgList().get(0)).into(holder.pic);
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
                boolean b = DetermineConnServer.isConnByHttp(getApplicationContext());
                if(b){
                    URL url = new URL("http://" + ip + ":8080/MoJi/note/download?userId=" + data.getUser().getUserId()+"&flag="+true);
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
