package cn.edu.hebtu.software.test.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import androidx.annotation.Nullable;
import cn.edu.hebtu.software.test.Adapter.SceneAdapter;
import cn.edu.hebtu.software.test.Data.Scene;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;

/**
 * @Author: 邸祯策
 * @Date: 2020/04/23
 * @Describe: 景点
 */
public class MileageFragmentItem3 extends MyBaseFragment {

    private MyApplication data;
    private List<Scene> sceneList;
    private String ip;
    private SceneAdapter adapter;
    private ListView listView;
    private SmartRefreshLayout refreshlayout;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    Toast.makeText(getActivity().getApplicationContext(), (CharSequence) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentViewId(), container, false);

        data = (MyApplication)getActivity().getApplication();
        ip = data.getIp();

        Thread thread = new getMessage();
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        init();

        return mRootView;
    }

    private void init(){
        refreshlayout = mRootView.findViewById(R.id.refreshlayout);
        listView = mRootView.findViewById(R.id.listView);

        adapter = new SceneAdapter(getActivity(),sceneList,R.layout.item_scene);
        listView.setAdapter(adapter);

        //设置下拉刷新和上拉加载监听
        refreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.replaceAll(getData());
                        refreshLayout.finishRefresh();
                    }
                },1000);
            }
        });

        refreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addAll(getData());
                        refreshLayout.finishLoadMore();
                    }
                },1000);
            }
        });

    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void initData() {
        new getMessage().start();
    }

    @Override
    protected int getContentViewId() {
        return  R.layout.fragment_scene_list;
    }

    private List<Scene>  getData() {
        new getMessage().start();
        return sceneList;
    }

    class getMessage extends Thread{
        @Override
        public void run() {
            try {
                if (DetermineConnServer.isConnByHttp(getActivity().getApplicationContext())) {
                    URL url = new URL("http://"+ip+":8080/MoJi/scene/list");
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String str = null;
                    if ((str = reader.readLine()) != null) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Scene>>() {
                        }.getType();
                        sceneList = gson.fromJson(str, type);
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
}
