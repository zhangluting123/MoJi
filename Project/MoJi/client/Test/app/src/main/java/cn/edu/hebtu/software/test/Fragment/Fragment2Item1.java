package cn.edu.hebtu.software.test.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.edu.hebtu.software.test.Adapter.StaggeredGridAdapter;
import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;

/**
 * @Author: 邸祯策
 * @Date: 2020/04/23
 * @Describe: 推荐页
 */
public class Fragment2Item1 extends MyBaseFragment {

    private MyApplication data;
    private List<Note> noteList = new ArrayList<>();

    private StaggeredGridAdapter adapter;
    private RecyclerView mRvPu;
    private SmartRefreshLayout refreshlayout;
    private List<Integer> list=new ArrayList<>();
    private int[] ids = {R.drawable.demo1,R.drawable.demo2,R.drawable.demo3,R.drawable.demo4,R.drawable.demo5,R.drawable.demo6,R.drawable.demo7,R.drawable.demo8};

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "onCreate");

        this.mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentViewId(), container, false);
        Log.e("TAG", "onCreateView");

        for(int i = 0 ; i < ids.length;i++){
            list.add(ids[i]);
        }

        data = (MyApplication)getActivity().getApplication();
        new getMessage().start();

        refreshlayout = mRootView.findViewById(R.id.refreshlayout);

        mRvPu=(RecyclerView)mRootView.findViewById(R.id.rv_pu);
        //设置布局方式,2列，垂直
        mRvPu.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new StaggeredGridAdapter(getActivity(),noteList);
        //adapter.replaceAll(list);
        mRvPu.setAdapter(adapter);
        //adapter.notifyDataSetChanged();

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
                        adapter.addData(adapter.getItemCount(),getData());
                        refreshLayout.finishLoadMore();
                    }
                },1000);
            }
        });

        return mRootView;
    }


    @Override
    protected void lazyLoad() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getContentViewId() {
        return  R.layout.add;
    }

    private ArrayList<Integer>  getData() {
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0 ; i < ids.length;i++){
            list.add(ids[i]);
        }
        return list;
    }

    class getMessage extends Thread{
        @Override
        public void run() {
            try {
                if (DetermineConnServer.isConnByHttp(getActivity().getApplicationContext())) {
                    URL url = new URL("http://123.56.175.200:8080/MoJi/note/query?userId="+data.getUser().getUserId());
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String str = null;
                    while ((str = reader.readLine()) != null) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Note>>() {
                        }.getType();
                        noteList = gson.fromJson(str, type);
                        Collections.reverse(noteList);
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
