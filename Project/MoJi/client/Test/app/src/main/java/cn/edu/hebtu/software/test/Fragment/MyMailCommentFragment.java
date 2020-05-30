package cn.edu.hebtu.software.test.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.edu.hebtu.software.test.Adapter.MyCommentAdapter;
import cn.edu.hebtu.software.test.Data.MailMyComment;
import cn.edu.hebtu.software.test.Data.Video;
import cn.edu.hebtu.software.test.DetailActivity.MailCommentDetailActivity;
import cn.edu.hebtu.software.test.DetailActivity.MailReplyActivity;
import cn.edu.hebtu.software.test.DetailActivity.VideoMailCommentDetailActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;
/**
 * @ProjectName:    MoJi
 * @Description:    评论消息
 * @Author:         张璐婷
 * @CreateDate:     2020/4/24 16:31
 * @Version:        1.0
 */
public class MyMailCommentFragment extends Fragment {
    private View view;
    private List<MailMyComment> myComments;
    private List<MailMyComment> myComments2;
    private PopupWindow popupWindow;
    private MyCommentAdapter adapter;
    private MyCommentAdapter adapter2;
    private int pos;
    private int pos2;
    private MyApplication data;
    private String ip;
    int tag;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    Toast.makeText(getActivity().getApplicationContext(), (CharSequence) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1002:
                    myComments = (List<MailMyComment>)msg.obj;
                    init();
                    break;
                case 1003:
                    if(pos < myComments.size() && pos > -1){
                        myComments.get(pos).setReadFlag(1);
                        adapter.refresh(myComments);
                        pos = -1;
                    }
                    if(pos2 < myComments2.size() && pos2 > -1){
                        myComments2.get(pos2).setReadFlag(1);
                        adapter2.refresh(myComments2);
                        pos2 = -1;
                    }
                    break;
                case 1004:
                    Toast.makeText(getActivity().getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                    if(pos < myComments.size() && pos > -1) {
                        myComments.remove(pos);
                        adapter.refresh(myComments);
                        pos = -1;
                    }
                    if(pos2 < myComments2.size() && pos2 > -1){
                        myComments2.remove(pos2);
                        adapter2.refresh(myComments2);
                        pos2 = -1;
                    }
                    break;
                case 1005:
                    myComments2 = (List<MailMyComment>)msg.obj;
                    init();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mymail_comment_layout, container,false);

        data = (MyApplication)getActivity().getApplication();
        ip = data.getIp();

        getMailList();
        getMailListVideo();

        return view;
    }

    private void init(){
        ListView listView = view.findViewById(R.id.lv_mycomment);
        ListView listView2 = view.findViewById(R.id.lv_myvideocomment);
        adapter = new MyCommentAdapter(myComments,R.layout.item_mycomment,getActivity().getApplicationContext());
        adapter2 = new MyCommentAdapter(myComments2,R.layout.item_mycomment,getActivity().getApplicationContext());
        listView.setAdapter(adapter);
        listView2.setAdapter(adapter2);
        pos = -1;
        pos2 = -1;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tag=1;
                if(myComments.get(position).getReadFlag() == 0) {
                    setRead(myComments.get(position).getId());
                }
                pos = position;
                showPopupWindow(parent,myComments.get(position));
            }
        });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tag=2;
                if(myComments2.get(position).getReadFlag() == 0) {
                    setRead(myComments2.get(position).getId());
                }
                pos2 = position;
                showPopupWindow(parent,myComments2.get(position));
            }
        });
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/4/13  16:53
     *  @Description: 点击评论弹出菜单
     */

    private void showPopupWindow(AdapterView<?> parent, MailMyComment mailMyComment){
        View v = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.popup_menu2,null);
        popupWindow = new PopupWindow(getActivity().getApplicationContext());
        popupWindow.setContentView(v);
        popupWindow.setBackgroundDrawable(null);
        registerListener(mailMyComment,v);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true); //设置点击menu以外其他地方以及返回键退出
        popupWindow.setOutsideTouchable(true);  //设置触摸外面时消失
        //设置弹出位置
        popupWindow.showAtLocation(parent, Gravity.CENTER,0,0);
        popupWindow.setAnimationStyle(R.style.popup_menu);
        //设置弹出时背景色变暗
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.4f;
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);
        //关闭时背景色恢复
        popupWindow.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp1 = getActivity().getWindow().getAttributes();
            lp1.alpha = 1f;
//            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getActivity().getWindow().setAttributes(lp1);
        });

    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/4/14  17:05
     *  @Description: 注册PopupWindow中元素的监听器
     *  必须是同一个视图点击才有效
     */
    private void registerListener(MailMyComment mailMyComment, View v){
        CustomerOnClickListener listener = new CustomerOnClickListener(mailMyComment);
        Button reply = v.findViewById(R.id.reply);
        Button delete = v.findViewById(R.id.delete);
        Button detail = v.findViewById(R.id.detail);
        reply.setOnClickListener(listener);
        delete.setOnClickListener(listener);
        detail.setOnClickListener(listener);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/4/14  17:06
     *  @Description: 监听器类
     */
    class CustomerOnClickListener implements View.OnClickListener {
        private MailMyComment mailMyComment;

        public CustomerOnClickListener(MailMyComment mailMyComment) {
            this.mailMyComment = mailMyComment;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.reply:
                    Intent intent = new Intent(getActivity().getApplicationContext(), MailReplyActivity.class);
                    intent.putExtra("mailMyComment", mailMyComment);
                    intent.putExtra("tag", tag);
                    startActivity(intent);
                    break;
                case R.id.delete:
                    delete(mailMyComment.getId());
                    break;
                case R.id.detail:
                    if(tag==1){
                        Log.e("************note:","MailCommentDetailActivity");
                        Intent intent1 = new Intent(getActivity().getApplicationContext(), MailCommentDetailActivity.class);
                        intent1.putExtra("mailMyComment", mailMyComment);
                        startActivity(intent1);
                    }else {
                        Log.e("************video:","VideoMailCommentDetailActivity");
                        Intent intent1 = new Intent(getActivity().getApplicationContext(), VideoMailCommentDetailActivity.class);
                        intent1.putExtra("mailMyComment", mailMyComment);
                        startActivity(intent1);
                    }
                    break;
            }
            popupWindow.dismiss();
        }

    }

    private void getMailList(){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getActivity().getApplicationContext())){
                    List<MailMyComment> mails = new ArrayList<>();
                    try {
                        URL url = new URL("http://"+ ip +":8080/MoJi/mailmycomment/list?userId=" + data.getUser().getUserId() );
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        String str = null;
                        if((str = reader.readLine()) != null){
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<MailMyComment>>(){}.getType();
                            mails = gson.fromJson(str, type);
                            Collections.reverse(mails);
                            Message msg = Message.obtain();
                            msg.what = 1002;
                            msg.obj = mails;
                            handler.sendMessage(msg);
                        }

                        reader.close();
                        in.close();
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
        }.start();
    }

    private void getMailListVideo(){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getActivity().getApplicationContext())){
                    List<MailMyComment> videoMails = new ArrayList<>();
                    try {
                        URL url = new URL("http://"+ ip +":8080/MoJi/mailmycomment/listVideo?userId=" + data.getUser().getUserId() );
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        String str = null;
                        if((str = reader.readLine()) != null){
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<MailMyComment>>(){}.getType();
                            videoMails = gson.fromJson(str, type);
                            Collections.reverse(videoMails);
                            Message msg = Message.obtain();
                            msg.what = 1005;
                            msg.obj = videoMails;
                            handler.sendMessage(msg);
                        }

                        reader.close();
                        in.close();
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
        }.start();
    }
    /**
     *  @author: 张璐婷
     *  @time: 2020/4/25  11:53
     *  @Description: 设置消息为已读
     */
    private void setRead(Integer myCommentId){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getActivity().getApplicationContext())){
                    try {
                        URL url = new URL("http://"+ ip +":8080/MoJi/mailmycomment/update?myCommentId=" + myCommentId);
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        if(reader.readLine().equals("OK")){
                            Message msg = Message.obtain();
                            msg.what = 1003;
                            handler.sendMessage(msg);
                        }

                        reader.close();
                        in.close();
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
        }.start();
    }

    /**
     *  @author: 张璐婷
     *  @time: 2020/4/25  14:47
     *  @Description: 删除消息
     */
    private void delete(Integer myCommentId){
        new Thread(){
            @Override
            public void run() {
                if(DetermineConnServer.isConnByHttp(getActivity().getApplicationContext())){
                    try {
                        URL url = new URL("http://"+ ip +":8080/MoJi/mailmycomment/delete?myCommentId=" + myCommentId);
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        if(reader.readLine().equals("OK")){
                            Message msg = Message.obtain();
                            msg.what = 1004;
                            handler.sendMessage(msg);
                        }

                        reader.close();
                        in.close();
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
        }.start();
    }
}
