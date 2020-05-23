package cn.edu.hebtu.software.test.Fragment;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
import java.util.List;

import cn.edu.hebtu.software.test.Adapter.MyLikeAdapter;
import cn.edu.hebtu.software.test.Data.UserLike;
import cn.edu.hebtu.software.test.Data.Video;
import cn.edu.hebtu.software.test.DetailActivity.DropsDetailActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * @ProjectName:    MoJi
 * @Description:    我的点赞
 * @Author:         张璐婷
 * @CreateDate:     2020/5/22
 * @Version:        1.0
 */
public class MyFollowPrintLikedFragment extends Fragment {
    private List<UserLike> userLikes = new ArrayList<>();
    private MyLikeAdapter adaper;


    private MyApplication data;
    private String ip;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(getActivity().getApplicationContext(), (CharSequence) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_foot_print_video, container, false);

        data = (MyApplication)getActivity().getApplication();
        ip = data.getIp();

        init();

        ListView listView = view.findViewById(R.id.video_list);
        adaper = new MyLikeAdapter(getActivity(),userLikes);
        listView.setAdapter(adaper);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(null != userLikes.get(position).getNoteLike().getNoteId()){
                    Intent intent = new Intent(getActivity(), DropsDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("note", userLikes.get(position).getNoteLike());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });

        SmartRefreshLayout refreshLayout = view.findViewById(R.id.refreshlayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        init();
                        adaper.refresh(userLikes);
                        refreshLayout.finishRefresh();
                    }
                },1000);
            }
        });
        return view;
    }

    private void init(){
        Thread thread = new GetUserLike();
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/5/22  14:59
     *  @Description: 获取用户点赞信息
     */
    class GetUserLike extends Thread{
        @Override
        public void run() {
            try {
                if (DetermineConnServer.isConnByHttp(getActivity().getApplicationContext())) {
                    URL url = new URL("http://"+ip+":8080/MoJi/userLike/list?userId="+data.getUser().getUserId());
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String str = null;
                    if ((str = reader.readLine()) != null) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<UserLike>>() {}.getType();
                        userLikes = gson.fromJson(str,type);
                    }
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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
