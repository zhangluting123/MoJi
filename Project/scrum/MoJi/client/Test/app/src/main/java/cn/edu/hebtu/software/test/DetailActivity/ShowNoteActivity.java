package cn.edu.hebtu.software.test.DetailActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import cn.edu.hebtu.software.test.Adapter.GuidePageAdapter;
import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.Fragment.ListBottomSheetDialogFragment;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.R;

public class ShowNoteActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    //数据
    private Note note;
    //ip
    private String ip;
    //控件
    private ImageView imgAvatar;
    private TextView tvTitle;
    private TextView tvLocation;
    private TextView tvContent;
    private TextView tvTime;
    private Button btnComment;
    private ViewPager vp;
    private List<View> viewList;//图片资源的集合
    private ViewGroup vg;//放置圆点
    //实例化圆点View
    private ImageView point;
    private ImageView[] ivPointArray;
    private LinearLayout.LayoutParams layoutParams;
    //自定义事件监听器
    private MyCustomListener customListener;

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
    }

    private void getviews() {
        imgAvatar = findViewById(R.id.imgAvatar);
        tvTitle = findViewById(R.id.tvTitle);
        tvLocation = findViewById(R.id.tvLocation);
        tvContent = findViewById(R.id.tvContent);
        tvTime = findViewById(R.id.tvTime);
        btnComment = findViewById(R.id.btnComment);
        vp = findViewById(R.id.vpNoteImg);
    }

    private void registListener() {
        customListener = new MyCustomListener();
        btnComment.setOnClickListener(customListener);
    }

    class MyCustomListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnComment:
                    jumpToDialog();
                    break;
            }
        }
    }

    private void jumpToDialog() {
        ListBottomSheetDialogFragment l = new ListBottomSheetDialogFragment();
        l.show(getSupportFragmentManager(),"myFragment");
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
