package cn.edu.hebtu.software.test.DetailActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import cn.edu.hebtu.software.test.Adapter.GuidePageAdapter;
import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.Fragment.ListBottomSheetDialogFragment;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;

public class ShowNoteActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    //数据
    private Note note;
    //当前便签状态
    private int noteSelf;
    //ip
    private String ip;
    //控件
    private ImageView imgAvatar;
    private TextView tvTitle;
    private TextView tvLocation;
    private TextView tvContent;
    private TextView tvTime;
    private Button btnComment;
    private Button btnChangeSelf;
    private ViewPager vp;
    private List<View> viewList;//图片资源的集合
    private ViewGroup vg;//放置圆点
    //实例化圆点View
    private ImageView point;
    private ImageView[] ivPointArray;
    private LinearLayout.LayoutParams layoutParams;
    //自定义事件监听器
    private MyCustomListener customListener;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    String string = (String)msg.obj;
                    //返回1或0 代表修改成功，返回noChange代表修改失败
                    if (!("noChange".equals(string))){
                        if("1".equals(string)){
                            btnChangeSelf.setText("私密");
                        }else if ("0".equals(string)){
                            btnChangeSelf.setText("公开");
                        }
                    }
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_note);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        }

        final MyApplication data = (MyApplication) getApplication();
        ip = data.getIp();
        String str = getIntent().getStringExtra("noteJsonStr");
        Gson gson = new Gson();
        note = gson.fromJson(str,Note.class);
        noteSelf = note.getSelf();

        init();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void init(){
        getviews();
        registListener();
        //加载ViewPager
        initViewPager();
        //加载底部圆点
        initPoint();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.circleCrop();
        Glide.with(getApplicationContext()).load("http://" + ip + ":8080/MoJi/" + note.getUser().getUserHeadImg()).apply(requestOptions).into(imgAvatar);
        tvTitle.setText(note.getTitle());
        tvLocation.setText(note.getLocation());
        tvContent.setText(note.getContent());
        tvTime.setText(note.getTime());
        btnChangeSelf.setText(note.getSelf()==1?"私密":"公开");
    }

    private void getviews() {
        imgAvatar = findViewById(R.id.imgAvatar);
        tvTitle = findViewById(R.id.tvTitle);
        tvLocation = findViewById(R.id.tvLocation);
        tvContent = findViewById(R.id.tvContent);
        tvTime = findViewById(R.id.tvTime);
        btnComment = findViewById(R.id.btnComment);
        btnChangeSelf = findViewById(R.id.btn_changeSelf);
        vp = findViewById(R.id.vpNoteImg);
    }

    private void registListener() {
        customListener = new MyCustomListener();
        btnComment.setOnClickListener(customListener);
        btnChangeSelf.setOnClickListener(customListener);
    }

    class MyCustomListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnComment:
                    jumpToDialog();
                    break;
                case R.id.btn_changeSelf:
                    changeSelf();
                    break;
            }
        }
    }

    private void jumpToDialog() {
        ListBottomSheetDialogFragment l = new ListBottomSheetDialogFragment();
        l.show(getSupportFragmentManager(),"myFragment");
    }

    private void changeSelf(){
        noteSelf = noteSelf==1?0:1;
        final int self = noteSelf;
        new Thread() {
            public void run() {
                try {
                    if(DetermineConnServer.isConnByHttp(getApplicationContext())) {
                        List<Note> threadList = new ArrayList<>();
                        URL url = new URL("http://" + ip + ":8080/MoJi/note/changeSelf?noteId=" + note.getNoteId() + "&self=" + self);
                        URLConnection conn = url.openConnection();
                        InputStream in = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String str = reader.readLine();

                        Message msg = Message.obtain();
                        msg.obj = str;
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }else{
                        Message msg = Message.obtain();
                        msg.obj = "未连接到服务器";
                        msg.what = 2;
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
    /**
     * 加载底部圆点
     */
    private void initPoint() {
        //这里实例化LinearLayout
        vg = findViewById(R.id.guide_ll_point);
        //根据ViewPager的item数量实例化数组
        ivPointArray = new ImageView[viewList.size()];
        //循环新建底部圆点ImageView，将生成的ImageView保存到数组中
        int size = viewList.size();
        for (int i = 0;i<size;i++){
            point = new ImageView(this);
            layoutParams = new LinearLayout.LayoutParams(30,30);

            //第一个页面需要设置为选中状态，这里采用两张不同的图片
            if (i == 0){
                point.setBackgroundResource(R.mipmap.ic_page_indicator_focused);
            }else{
                layoutParams.leftMargin=20;
                point.setBackgroundResource(R.mipmap.ic_page_indicator);
            }
            point.setLayoutParams(layoutParams);
            point.setPadding(40,0,40,0);//left,top,right,bottom
            ivPointArray[i] = point;

            //将数组中的ImageView加入到ViewGroup
            vg.addView(ivPointArray[i]);
        }
    }

    /**
     * 加载图片ViewPager
     */
    private void initViewPager() {
        viewList = new ArrayList<>();
        if(note != null){
            for (int i = 0;i<note.getImgList().size();i++){
                //new ImageView并设置图片资源
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(getApplicationContext()).load("http://" + ip + ":8080/MoJi/"+note.getImgList().get(i)).into(imageView);

                //将ImageView加入到集合中
                viewList.add(imageView);
            }
        }

        //View集合初始化好后，设置Adapter
        vp.setAdapter(new GuidePageAdapter(viewList));
        //设置滑动监听
        vp.setOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 滑动后的监听
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        //循环设置当前页的标记图
        int length = note.getImgList().size();
        for (int i = 0;i<length;i++){
            ivPointArray[position].setBackgroundResource(R.mipmap.ic_page_indicator_focused);
            if (position != i){
                ivPointArray[i].setBackgroundResource(R.mipmap.ic_page_indicator);
            }
        }

    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
