package cn.edu.hebtu.software.test.Fragment;

import android.util.Log;
import android.widget.TextView;
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
    private String[] mTitles = {"推荐", "关注", "景点", "小街","小店","Vlog"};

    @Override
    protected int getContentViewId() {
        return R.layout.mileage_layout;
    }

    @Override
    protected void lazyLoad() {
        Log.e("MileageFragment", "MileageFragment");
    }
    @Override
    protected void initData() {
        TextView title = getActivity().findViewById(R.id.toorbar_title);
        title.setText("远行");
        final ViewPager viewPager = mRootView.findViewById(R.id.view_pager2);
        tablayout = mRootView.findViewById(R.id.shape_table_layout);

        mFagments = new ArrayList<>();
        mFagments.add(new MileageFragmentItem1());
        mFagments.add(new MileageFragmentItem2());
        mFagments.add(new MileageFragmentItem3());
        mFagments.add(new MileageFragmentItem4());
        mFagments.add(new MileageFragmentItem5());
        mFagments.add(new MileageFragmentItem6());
        // 在activity中FragmentManager通过getSupportFragmentManager()去获取，如果在是在fragment中就需要通过getChildFragmentManager()去获取
        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager(),mFagments,mTitles);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(false, new CustPagerTransformer(getActivity()));
        tablayout.setViewPager(viewPager, mTitles);
    }
}