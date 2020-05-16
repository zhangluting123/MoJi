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
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import cn.edu.hebtu.software.test.Adapter.MyVideoAdaper;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.Data.Video;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * @ProjectName:    MoJi
 * @Description:    我的视频
 * @Author:         张璐婷
 * @CreateDate:     2020/5/15 15:34
 * @Version:        1.0
 */
public class MyFootPrintVideoFragment extends Fragment {
    private List<Video> videoList;

    private JCVideoPlayer.JCAutoFullscreenListener mSensorEventListener;
    private SensorManager mSensorManager;

    private MyApplication data;
    private String ip;
    private User nowUser;
    private boolean flag;//flag=true 自己查看动态

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

        Intent intent = getActivity().getIntent();
        User otherUser = intent.getParcelableExtra("user");
        if(null == otherUser){
            nowUser = data.getUser();
            flag = true;
        }else{
            nowUser = otherUser;
            flag = false;
        }

        init();

        ListView listView = view.findViewById(R.id.video_list);
        MyVideoAdaper adaper = new MyVideoAdaper(videoList, R.layout.item_foot_print_video, getActivity(),flag);
        listView.setAdapter(adaper);
        mSensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
        mSensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();

        SmartRefreshLayout refreshLayout = view.findViewById(R.id.refreshlayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        init();
                        adaper.refresh(videoList);
                        refreshLayout.finishRefresh();
                    }
                },1000);
            }
        });
        return view;
    }

    private void init(){
        Thread thread = new getVideoList();
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    class getVideoList extends Thread{
        @Override
        public void run() {
            if(DetermineConnServer.isConnByHttp(getActivity().getApplicationContext())){
                try {
                    URL url = new URL("http://" + ip + ":8080/MoJi/video/myList?userId="+nowUser.getUserId());
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String str = null;
                    if((str = reader.readLine())!=null){
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Video>>() {}.getType();
                        videoList = gson.fromJson(str,type);
                        Collections.reverse(videoList);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{
                Message msg = Message.obtain();
                msg.what = 1001;
                msg.obj = "未连接到服务器";
                handler.sendMessage(msg);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
        JCVideoPlayer.releaseAllVideos();
    }
}
