package cn.edu.hebtu.software.test.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.test.Adapter.StaggeredGridAdapter;
import cn.edu.hebtu.software.test.R;

/**
 * @Author: 邸祯策
 * @Date: 2020/04/23
 * @Describe:
 */
public class Fragment2Item1 extends MyBaseFragment {


    private RecyclerView mRvPu;
    private SmartRefreshLayout refreshlayout;
    private List<Integer> list=new ArrayList<>();
    private int[] ids = {R.drawable.demo1,R.drawable.demo2,R.drawable.demo3,R.drawable.demo4,R.drawable.demo5,R.drawable.demo6,R.drawable.demo7,R.drawable.demo8};

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

        for(int i = 0 ; i < ids.length;i++){
            list.add(ids[i]);
        }

        refreshlayout = mRootView.findViewById(R.id.refreshlayout);

        mRvPu=(RecyclerView)mRootView.findViewById(R.id.rv_pu);
        //设置布局方式,2列，垂直
        mRvPu.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        StaggeredGridAdapter adapter = new StaggeredGridAdapter(getActivity());
        adapter.replaceAll(list);
        mRvPu.setAdapter(adapter);

        //设置下拉刷新和上拉加载监听
        refreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.replaceAll(getData());
                        refreshLayout.finishRefresh();
                    }
                },1500);
            }
        });

        refreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addData(adapter.getItemCount(),getData());
                        refreshLayout.finishLoadMore();
                    }
                },1500);
            }
        });


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

    private ArrayList<Integer>  getData() {
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 0 ; i < 8;i++){
            list.add(ids[i]);
        }
        return list;
    }
}
