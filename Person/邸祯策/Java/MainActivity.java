package com.example.ana3;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.ana3.CardView.CardFragment;
import com.example.ana3.CardView.CardFragmentPagerAdapter;
import com.example.ana3.CardView.ShadowTransformer;
import com.example.ana3.Dialog.BottomDialogFragment;
import com.example.ana3.LinkToDB.DataBean;
import com.example.ana3.LinkToDB.Link;
import com.example.ana3.RecyclerView.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /*private Integer[] mImgIds = {R.drawable.photo, R.drawable.timg1, R.drawable.timg2, R.drawable.timg3, R.drawable.timg4,
            R.drawable.timg5, R.drawable.timg6, R.drawable.timg7,R.drawable.timg8,R.drawable.timg9,R.drawable.timg10};*/
    //private List<Integer> datas;//图片ID
    Link link = new Link(4);
    private static ViewPager mViewPager;
    private static CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;
    private RecyclerView recyclerView;
    private static RecyclerViewAdapter adapter;
    private static List<DataBean> lists;
    StaticX staticX;

    // 手指按下的点为(x1,y1)手指离开屏幕的点为(x2,y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 上部滑动效果
        initView();
        initData();
        LinearLayoutManager m = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        m.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(m);
        adapter=new RecyclerViewAdapter(lists, this);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(MainActivity.this, "click " + position, Toast.LENGTH_SHORT).show();
                Log.d("TAG", "click: "+position);
                mViewPager.setCurrentItem(position);
            }
        });

        adapter.setOnItemLongClickListener(new RecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(MainActivity.this, "long click " + position, Toast.LENGTH_SHORT).show();
            }
        });
        // 下部滑动效果
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setPageMargin(-15);
        recyclerView = findViewById(R.id.recycler_View);
        staticX = new StaticX();
        mFragmentCardAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(),
                dpToPixels(2, this));
        for(int i = 1;i<staticX.getList().size();i++){
            mFragmentCardAdapter.addCardFragment(new CardFragment(lists.get(i).getIndex(),lists.get(i).getImage()));
        }
        mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter, recyclerView, adapter, mFragmentCardAdapter);
        mFragmentCardShadowTransformer.enableScaling(true);
        mViewPager.setAdapter(mFragmentCardAdapter);
        mViewPager.setPageTransformer(false, mFragmentCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);

        // 开场动画效果
        ViewPager viewPager = findViewById(R.id.viewPager);
        ObjectAnimator animator_viewPager = ObjectAnimator.ofFloat(viewPager,"translationX",1500f,0f)
                .setDuration(1200);
        animator_viewPager.start();
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    private void initData() {
        lists = new StaticX().getList();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_View);
    }

    // 添加下部上滑事件
    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(
            10);

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }

    // 显示底部Dialog
    public void showBottomDialog(View view) {
        FragmentManager fm = getSupportFragmentManager();
        BottomDialogFragment bottomDialogFragment = new BottomDialogFragment();
        bottomDialogFragment.show(fm, "fragment_add_card_dialog");
    }

    public CardFragmentPagerAdapter getmFragmentCardAdapter() {
        return mFragmentCardAdapter;
    }

    public RecyclerViewAdapter getAdapter() {
        return adapter;
    }

    public static List<DataBean> getLists() {
        return lists;
    }

    public static ViewPager getmViewPager() {
        return mViewPager;
    }

    public DataBean getDataBeanByIndex(int index){
        for(int i = 0;i<lists.size();i++){
            if(lists.get(i).getIndex()==index){
                return lists.get(i);
            }
        }
        return null;
    }
}
