package cn.edu.hebtu.software.test.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import cn.edu.hebtu.software.test.Adapter.StaggeredGridAdapter;
import cn.edu.hebtu.software.test.R;

/**
 * @Author: 邸祯策
 * @Date: 2019/04/23
 * @Describe:
 */
public class Fragment2Item2 extends MyBaseFragment{
    private RecyclerView mRvPu;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "onCreate");

        this.mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getContentViewId(), container, false);
        Log.e("TAG", "onCreateView");

        mRvPu=(RecyclerView)mRootView.findViewById(R.id.rv_pu);
        //设置布局方式,2列，垂直
        mRvPu.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //mRvPu.setAdapter(new StaggeredGridAdapter(getActivity()));

        return mRootView;
    }


    @Override
    protected void lazyLoad() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getContentViewId() {
        return  R.layout.add;
    }
}
