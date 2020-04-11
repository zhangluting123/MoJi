package cn.edu.hebtu.software.test.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.test.Data.Comment;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;

/**
 * @author 春波
 * @ProjectName MoJi
 * @PackageName：com.example.moji
 * @Description：
 * @time 2019/11/28 10:44
 */
public class ListBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private List<Comment> list = new ArrayList<>();
    private Handler handler;
    private String ip;
    private String currentNoteId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final MyApplication data = (MyApplication) getActivity().getApplication();
        ip = data.getIp();
        currentNoteId = data.getCurrentNoteId();

        //给dialog设置主题为透明背景 不然会有默认的白色背景
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomBottomSheetDialogTheme);

    }

    /**
     * 如果想要点击外部消失的话 重写此方法
     *
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        //设置点击外部可消失
        dialog.setCanceledOnTouchOutside(true);
        //设置使软键盘弹出的时候dialog不会被顶起
        Window win = dialog.getWindow();
        WindowManager.LayoutParams params = win.getAttributes();
        //设置软键盘通常是不可见的
        win.setSoftInputMode(params.SOFT_INPUT_ADJUST_NOTHING);
        //这里设置dialog的进出动画
        win.setWindowAnimations(R.style.animate_dialog);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 在这里将view的高度设置为精确高度，即可屏蔽向上滑动不占全屏的手势。
        //如果不设置高度的话 会默认向上滑动时dialog覆盖全屏
        View view = inflater.inflate(R.layout.fragment_item_list_dialog, container, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getScreenHeight(getActivity()) * 3 / 5));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        //列表再底部开始展示，反转后由上面开始展示
        layout.setStackFromEnd(true);
        //列表翻转
        layout.setReverseLayout(true);
        recyclerView.setLayoutManager(layout);

        final TextView commentTitle = (TextView) view.findViewById(R.id.commentTitle);
        final Button imgbtnFinish = (Button) view.findViewById(R.id.btnFinish);
//        final TextView tv = (TextView) view.findViewById(R.id.tv);


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        list = (List<Comment>)msg.obj;
                        commentTitle.setText(list.size() + "条评论");
                        //往适配器传数据
                        recyclerView.setAdapter(new ItemAdapter(list.size(),list));
                        break;
                    case 2:
                        Toast.makeText(getActivity().getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                }
            }
        };

        new Thread(){
            public void run(){
                List<Comment> threadList = new ArrayList<>();
                URL url = null;
                URLConnection conn = null;
                try {
                    if(DetermineConnServer.isConnByHttp(getActivity().getApplicationContext())){
                        url = new URL("http://" + ip + ":8080/MoJi/comment/list?noteId=" + currentNoteId);
                        conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String str = null;
                        while ((str = reader.readLine()) !=null){
                            Gson gson = new Gson();
                            Type type = new TypeToken<List<Comment>>(){}.getType();
                            threadList = gson.fromJson(str,type);
                        }
                    }else{
                        Message message = Message.obtain();
                        message.what = 2;
                        message.obj = "未连接到服务器";
                        handler.sendMessage(message);
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Message msg = Message.obtain();
                msg.obj = threadList;
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }.start();

//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //弹出评论输入框
//                InputDialog inputDialog = new InputDialog(getActivity());
//                Window window = inputDialog.getWindow();
//                window.getDecorView().setPadding(0, 0, 0, 0);
//                WindowManager.LayoutParams params = window.getAttributes();
//                //设置窗口宽度为充满全屏
//                params.width = WindowManager.LayoutParams.MATCH_PARENT;
//                //设置窗口高度为包裹内容
//                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                //将设置好的属性set回去
//                window.setAttributes(params);
//                //设置软键盘通常是可见的
//                window.setSoftInputMode(params.SOFT_INPUT_STATE_VISIBLE);
//                inputDialog.show();
//            }
//        });

        imgbtnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
        private final int mItemCount;
        private List<Comment> commentList;

        //构造方法
        ItemAdapter(int itemCount, List<Comment> list) {
            commentList = list;
            mItemCount = itemCount;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.circleCrop();
            Glide.with(getActivity().getApplicationContext()).load("http://" + ip + ":8080/MoJi/" + commentList.get(position).getUser().getUserHeadImg()).apply(requestOptions).into(holder.commentAvatar);
            holder.commentUserName.setText(commentList.get(position).getUserName());
            holder.commentContent.setText(commentList.get(position).getCommentContent());
            holder.commentTime.setText(commentList.get(position).getCommentTime());
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //设置全局变量
//                    final Data data = (Data)getActivity().getApplication();
//                    data.setReplyName(commentList.get(position).getUserName());
//                    data.setReplyCommentId(commentList.get(position).getId());
//                    //弹出评论输入框
//                    InputDialog inputDialog = new InputDialog(getActivity());
//                    Window window = inputDialog.getWindow();
//                    window.getDecorView().setPadding(0, 0, 0, 0);
//                    WindowManager.LayoutParams params = window.getAttributes();
//                    //设置窗口宽度为充满全屏
//                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
//                    //设置窗口高度为包裹内容
//                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//                    //将设置好的属性set回去
//                    window.setAttributes(params);
//                    //设置软键盘通常是可见的
//                    window.setSoftInputMode(params.SOFT_INPUT_STATE_VISIBLE);
//                    inputDialog.show();
//                }
//            });

        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            final ImageView commentAvatar;
            final TextView commentUserName;
            final TextView commentContent;
            final TextView commentTime;

            MyViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.fragment_item_list_dialog_item, parent, false));
                commentAvatar = (ImageView) itemView.findViewById(R.id.commentAvatar);
                commentUserName = (TextView) itemView.findViewById(R.id.commentUserName);
                commentContent = (TextView) itemView.findViewById(R.id.commentContent);
                commentTime = (TextView) itemView.findViewById(R.id.commentTime);

            }
        }

    }

    /**
     * 得到屏幕的高
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

}
