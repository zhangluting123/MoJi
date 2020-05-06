package cn.edu.hebtu.software.test.Adapter;

import android.view.ViewGroup;

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

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //viewpager+fragment来回滑动fragment重新加载的简单解决办法：注释下面的代码
        //不建议使用，因为当选项卡过多的时候，如果不销毁的是，担心内存溢出
        //http://blog.csdn.net/qq_28058443/article/details/51519663
        //super.destroyItem(container, position, object);
    }
}
