package cn.edu.hebtu.software.test.Fragment;

import android.content.Intent;
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

import cn.edu.hebtu.software.test.Adapter.MyFollowAdapter;
import cn.edu.hebtu.software.test.Adapter.MyFootAdapter;
import cn.edu.hebtu.software.test.Data.Follow;
import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.Data.User;
import cn.edu.hebtu.software.test.DetailActivity.OtherMsgActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;

/**
 * @ProjectName:    MoJi
 * @Description:    我的关注
 * @Author:         邸祯策
 * @CreateDate:     2020/5/22
 * @Version:        1.0
 */
public class MyFollowPrintFolledFragment extends Fragment {
    private ListView listView;
    private List<User> followList = new ArrayList<>();
    private MyApplication data;
    private String ip;

    private User nowUser;
    private boolean flag;//flag = true,自己查看动态

    private MyFollowAdapter myFollowAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    Toast.makeText(getActivity().getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_foot_print_word,container, false);
        //获取全局变量
        data = (MyApplication) getActivity().getApplication();
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


        listView = view.findViewById(R.id.lv_footList);
        myFollowAdapter = new MyFollowAdapter(R.layout.item_follow_print_followed,getActivity().getApplicationContext(),followList,flag);
        listView.setAdapter(myFollowAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), OtherMsgActivity.class);
                intent.putExtra("user",followList.get(i));
                getContext().startActivity(intent);
            }
        });


        SmartRefreshLayout refreshLayout = view.findViewById(R.id.refreshlayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        followList.clear();
                        init();
                        myFollowAdapter.refresh(followList);
                        refreshLayout.finishRefresh();
                    }
                },1000);
            }
        });

        return view;
    }

    private void init() {
        Thread getNote = new GetNote();
        getNote.start();
        try {
            getNote.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class GetNote extends Thread{
        @Override
        public void run() {

            try {
                boolean b = DetermineConnServer.isConnByHttp(getActivity().getApplicationContext());
                if(b){
                    URL url = new URL("http://"+ ip +":8080/MoJi/user/queryFollowed?oneId=" + nowUser.getUserId());
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                    String str = reader.readLine();
                    if(null != str){
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<User>>(){}.getType();
                        followList = gson.fromJson(str,type);
                        Collections.reverse(followList);
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
    }
}
