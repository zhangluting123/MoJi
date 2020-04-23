package cn.edu.hebtu.software.test.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import cn.edu.hebtu.software.test.Adapter.CustPagerTransformer;
import cn.edu.hebtu.software.test.Adapter.MyPagerAdapter;
import cn.edu.hebtu.software.test.R;

/**
 * @ProjectName:    MoJi
 * @Description:    远行界面
 * @Author:         邸祯策
 * @CreateDate:     2020/4/10 15:39
 * @Version:        1.0
 */
public class MileageFragment extends MyBaseFragment {

    ArrayList<MyBaseFragment> mFagments;
    SlidingTabLayout tablayout;
    private String[] mTitles = {"推荐", "关注", "景点", "小街","小店"};

    @Override
    protected int getContentViewId() {
        return R.layout.fragment2_layout;
    }

    @Override
    protected void lazyLoad() {
        Log.e("fragment2", "fragment2");
    }
    @Override
    protected void initData() {
        TextView title = getActivity().findViewById(R.id.toorbar_title);
        title.setText("远行");
        final ViewPager viewPager = mRootView.findViewById(R.id.view_pager2);
        tablayout = mRootView.findViewById(R.id.shape_table_layout);

        mFagments = new ArrayList<>();
        mFagments.add(new Fragment2Item1());
        mFagments.add(new Fragment2Item2());
        mFagments.add(new Fragment2Item3());
        mFagments.add(new Fragment2Item4());
        mFagments.add(new Fragment2Item5());
        // 在activity中FragmentManager通过getSupportFragmentManager()去获取，如果在是在fragment中就需要通过getChildFragmentManager()去说去
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager(),mFagments,mTitles);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(false, new CustPagerTransformer(getActivity()));
        tablayout.setViewPager(viewPager, mTitles);

    }



}