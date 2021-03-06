package cn.edu.hebtu.software.test.Fragment;

import android.content.Intent;
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
import cn.edu.hebtu.software.test.Data.UserLike;
import cn.edu.hebtu.software.test.DetailActivity.DropsDetailActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;

/**
 * @Author: 邸祯策
 * @Date: 2020/04/23
 * @Describe: 推荐页
 */
public class MileageFragmentItem1 extends MyBaseFragment {

    private MyApplication data;
    private List<Note> noteList = new ArrayList<>();
    private List<UserLike> userLikes = new ArrayList<>();
    private String ip;
    private StaggeredGridAdapter adapter;
    private RecyclerView mRvPu;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "onCreate");
        this.mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentViewId(), container, false);
        Log.e("TAG", "onCreateView");

        data = (MyApplication)getActivity().getApplication();
        ip = data.getIp();

        Thread thread = new getMessage();
        Thread getUserLike = new GetUserLike();
        thread.start();
        getUserLike.start();
        try {
            thread.join();
            getUserLike.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        init();

        return mRootView;
    }

    private void init(){
        refreshlayout = mRootView.findViewById(R.id.refreshlayout);
        mRvPu=(RecyclerView)mRootView.findViewById(R.id.rv_pu);

        //设置布局方式,2列，垂直
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRvPu.setLayoutManager(staggeredGridLayoutManager);
        adapter = new StaggeredGridAdapter(getActivity(),noteList,userLikes);

        adapter.setOnItemClickListener(new StaggeredGridAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(), DropsDetailActivity.class);
                Bundle bundle = new Bundle();
                Log.e("position",position+"");
                bundle.putParcelable("note", adapter.getNoteList().get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mRvPu.setAdapter(adapter);

        //设置下拉刷新和上拉加载监听
        refreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.replaceAll(getData(),getUserLike());
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
        return  R.layout.mileage_common;
    }

    private List<Note>  getData() {
        new getMessage().start();
        return noteList;
    }

    private List<UserLike> getUserLike(){
        Thread getUserLike = new GetUserLike();
        getUserLike.start();
        try {
            getUserLike.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userLikes;
    }

    class getMessage extends Thread{
        @Override
        public void run() {
            try {
                if (DetermineConnServer.isConnByHttp(getActivity().getApplicationContext())) {
                    URL url = new URL("http://"+ip+":8080/MoJi/note/query?userId="+data.getUser().getUserId());
                    Log.e("****getUserId():",data.getUser().getUserId()+"");
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
}
