package cn.edu.hebtu.software.test.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;

import cn.edu.hebtu.software.test.Fragment.MyBaseFragment;

/**
 * @Author: 邸祯策
 * @Date: 2020/04/23
 * @Describe:
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<MyBaseFragment> mFragments;
    private String[] mTitles;
    public MyPagerAdapter(FragmentManager fm, ArrayList mFragments, String[] mTitles )
    {
        super(fm);
        this.mFragments = mFragments;
        this.mTitles = mTitles;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
