package cn.edu.hebtu.software.test.Quotation;

import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class CardFragmentPagerAdapter extends FragmentStatePagerAdapter implements CardAdapter {

    private static List<CardFragment> mFragments;
    private float mBaseElevation;
    StaticX staticX = new StaticX();

    public CardFragmentPagerAdapter(FragmentManager fm, float baseElevation) {
        super(fm);
        mFragments = new ArrayList<>();
        mBaseElevation = baseElevation;
        for(int i = 0;i<1;i++){
            mFragments.add(new CardFragment(-1,"images/photo.jpg"));
        }
    }


    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mFragments.get(position).getCardView();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        mFragments.set(position, (CardFragment) fragment);
        return fragment;
    }

    public void addCardFragment(CardFragment fragment) {
        mFragments.add(fragment);
        notifyDataSetChanged();
    }

    public void deleteCardFragment(CardFragment fragment) {
        mFragments.remove(fragment);
        notifyDataSetChanged();
    }

    public void fresh() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return PagerAdapter.POSITION_NONE;
    }
}
