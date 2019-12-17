package cn.edu.hebtu.software.test.Fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import cn.edu.hebtu.software.test.Quotation.CardFragment;
import cn.edu.hebtu.software.test.Quotation.CardFragmentPagerAdapter;
import cn.edu.hebtu.software.test.Quotation.DataBean;
import cn.edu.hebtu.software.test.Quotation.Link;
import cn.edu.hebtu.software.test.Quotation.RecyclerViewAdapter;
import cn.edu.hebtu.software.test.Quotation.ShadowTransformer;
import cn.edu.hebtu.software.test.Quotation.StaticX;
import cn.edu.hebtu.software.test.R;

/**
 * @ProjectName:    MoJi
 * @Description:    语录界面
 * @Author:         邸祯策
 * @CreateDate:     2019/11/26 15:41
 * @Version:        1.0
 */
public class QuotationFragment extends Fragment {
    private View view;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.quotation_layout, container, false);
        TextView title = getActivity().findViewById(R.id.toorbar_title);
        title.setText("语录");

        // 上部滑动效果
        initView();
        initData();
        LinearLayoutManager m = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        m.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(m);
        adapter=new RecyclerViewAdapter(lists, getContext());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getActivity(), "click " + position, Toast.LENGTH_SHORT).show();
                Log.d("TAG", "click: "+position);
                mViewPager.setCurrentItem(position);
            }
        });

        adapter.setOnItemLongClickListener(new RecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getActivity(), "long click " + position, Toast.LENGTH_SHORT).show();
            }
        });
        // 下部滑动效果
        mViewPager = view.findViewById(R.id.viewPager);
        mViewPager.setPageMargin(-15);
        recyclerView = view.findViewById(R.id.recycler_View);
        staticX = new StaticX();
        mFragmentCardAdapter = new CardFragmentPagerAdapter(getActivity().getSupportFragmentManager(),
                dpToPixels(2, getContext()));
        for(int i = 1;i<staticX.getList().size();i++){
            mFragmentCardAdapter.addCardFragment(new CardFragment(lists.get(i).getIndex(),lists.get(i).getImage()));
        }
        mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter, recyclerView, adapter, mFragmentCardAdapter);
        mFragmentCardShadowTransformer.enableScaling(true);
        mViewPager.setAdapter(mFragmentCardAdapter);
        mViewPager.setPageTransformer(false, mFragmentCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);

        // 开场动画效果
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        ObjectAnimator animator_viewPager = ObjectAnimator.ofFloat(viewPager,"translationX",1500f,0f)
                .setDuration(1200);
        animator_viewPager.start();

        return view;
    }

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    private void initData() {
        lists = new StaticX().getList();
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recycler_View);
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