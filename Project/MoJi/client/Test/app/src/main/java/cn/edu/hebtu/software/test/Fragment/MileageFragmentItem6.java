package cn.edu.hebtu.software.test.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import cn.edu.hebtu.software.test.Adapter.MultipleItemAdapter;
import cn.edu.hebtu.software.test.Data.MyMultipleItem;
import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.Data.Video;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;

/**
 * @Author: ming
 * @Date: 2020/05/05
 * @Describe: 视频推荐
 */
public class MileageFragmentItem6 extends MyBaseFragment {
    private MyApplication data;
    private String ip;
    private List<Video> videoList = new ArrayList<>();
    RecyclerView recyclerView;
    List<MyMultipleItem> lineData;
    MultipleItemAdapter adapter;
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
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        init();

        return mRootView;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.mileage_layout_item6;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void initData() {
        new getMessage().start();
    }

    private void init(){
        recyclerView = mRootView.findViewById(R.id.recyclear_view);
        lineData = new ArrayList<>();
        addData();
        // 设置为线性布局
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // 设置适配器
        //recyclerView.setAdapter(new RecyclerLineAdapter(getContext(), lineData));
        //adapter = new RecyclerCommonAdapter(R.layout.recyclear_item, lineData);
        adapter = new MultipleItemAdapter(getActivity(),lineData,videoList);
        adapter.openLoadAnimation();
        adapter.isFirstOnly(false);
        //上拉监听
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Toast.makeText(getActivity(), "正在加载", Toast.LENGTH_SHORT).show();
                addData();
                adapter.loadMoreComplete();
            }
        }, recyclerView);
        //item监听
        /*adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getActivity(), "点击了第" + (position + 1) + "条条目", Toast.LENGTH_SHORT).show();
            }
        });*/
        //item子view
        /*adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.share:
                        //不明原因失效
                        //share(getContext(),videoList.get(position).getPath());
                        break;
                    default:
                        break;
                }
            }
        });*/
        recyclerView.setAdapter(adapter);
    }

    private void addData() {
        Map<String, Object> map = null;
        for (int i = 0; i < videoList.size(); i++) {
            map = new HashMap<>();
            map.put("ip", ip);
            map.put("videoPath", videoList.get(i).getPath());
            map.put("UserHeadImg", videoList.get(i).getUser().getUserHeadImg());
            map.put("UserName", videoList.get(i).getUser().getUserName());
            map.put("videoTitle", videoList.get(i).getTitle());
            map.put("User", videoList.get(i).getUser());
            map.put("Video", videoList.get(i));
            lineData.add(new MyMultipleItem(1, map));
        }
    }

    public static void share(Context context, String shareText) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        context.startActivity(Intent.createChooser(intent, "MoJi分享"));
    }

    class getMessage extends Thread{
        @Override
        public void run() {
            try {
                if (DetermineConnServer.isConnByHttp(getActivity().getApplicationContext())) {
                    URL url = new URL("http://"+ip+":8080/MoJi/video/queryAll?userId="+data.getUser().getUserId());
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String str = null;
                    while ((str = reader.readLine()) != null) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Video>>() {
                        }.getType();
                        videoList = gson.fromJson(str, type);
                        Collections.reverse(videoList);
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
