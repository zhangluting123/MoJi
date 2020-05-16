package cn.edu.hebtu.software.test.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.test.Adapter.TraceListAdapter;
import cn.edu.hebtu.software.test.Data.Mail;
import cn.edu.hebtu.software.test.DetailActivity.MailDetailActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;

/**
 * @ProjectName:    MoJi
 * @Description:    通知列表界面
 * @Author:         邸凯扬
 * @CreateDate:     2020/4/11 10:50
 * @Version:        1.0
 */
public class MyMailNotificationFragment extends Fragment {
    private RecyclerView rvTrace;
    private View view;
    public static List<Mail> mailList;
    private TraceListAdapter adapter;
    private String ip;
    private MyApplication data;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(getActivity().getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    mailList = (List<Mail>)msg.obj;
                    initData();
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mymail_notification_layout, container, false);


        data = (MyApplication)getActivity().getApplication();
        ip = data.getIp();


        findView();
        sendToServer();

        return view;

    }

    private void findView() {
        rvTrace = view.findViewById(R.id.rvTrace);
    }

    private  void initData() {
        adapter = new TraceListAdapter(getActivity().getApplicationContext(), mailList);
        rvTrace.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        rvTrace.setAdapter(adapter);
        adapter.setOnItemClickListener(new TraceListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(), MailDetailActivity.class);
                Gson gson = new Gson();
                String str = gson.toJson(mailList.get(position));
                intent.putExtra("extra",str );
                startActivity(intent);
            }
        });
    }

    private void sendToServer() {
        new Thread() {
            @Override
            public void run() {

                try {
                    if(DetermineConnServer.isConnByHttp(getActivity().getApplicationContext())) {
                        List<Mail> list = new ArrayList<>();
                        URL url = new URL("http://" + ip + ":8080/MoJi/mail/list?userId=" + data.getUser().getUserId());
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String info = reader.readLine();
                        if (info != null) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Mail>>(){}.getType();
                            list = gson.fromJson(info, type);
                            Message msg = new Message();
                            msg.what = 1002;
                            msg.obj = list;
                            handler.sendMessage(msg);
                        }
                    }else{
                        Message msg = new Message();
                        msg.what = 1001;
                        msg.obj = "未连接到服务器";
                        handler.sendMessage(msg);
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

}
