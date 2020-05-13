package cn.edu.hebtu.software.test.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import cn.edu.hebtu.software.test.Adapter.MultipleItemAdapter;
import cn.edu.hebtu.software.test.Data.MyMultipleItem;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;

/**
 * @Author: 邸凯扬
 * @Date: 2020/05/05
 * @Describe: 视频推荐
 */
public class MileageFragmentItem6 extends MyBaseFragment {
    private MyApplication data;
    RecyclerView recyclerView;
    List<MyMultipleItem> lineData;
    MultipleItemAdapter adapter;

    String[] urls = {
            "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",
            "http://vjs.zencdn.net/v/oceans.mp4",
            "https://media.w3.org/2010/05/sintel/trailer.mp4",
    };

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

        data = (MyApplication)getActivity().getApplication();
        init();

        return mRootView;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.mileage_layout_item6;
    }

    @Override
    protected void lazyLoad() {

    }

    private void init(){
        recyclerView = mRootView.findViewById(R.id.recyclear_view);
        lineData = new ArrayList<>();
        addData();
        // 设置为线性布局
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // 设置适配器
        //recyclerView.setAdapter(new RecyclerLineAdapter(getContext(), lineData));
        //adapter = new RecyclerCommonAdapter(R.layout.recyclear_item, lineData);
        adapter = new MultipleItemAdapter(lineData);
        adapter.openLoadAnimation();
        adapter.isFirstOnly(false);
        //上拉监听
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                Toast.makeText(getActivity(), "正在加载", Toast.LENGTH_SHORT).show();
                addData();
                adapter.loadMoreComplete();
            }
        }, recyclerView);
        //item监听
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getActivity(), "点击了第" + (position + 1) + "条条目", Toast.LENGTH_SHORT).show();
            }
        });
        //item子view
        /*adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_recy_item_1_name:
                        Toast.makeText(getActivity(), "点击了第" + (position + 1) + "名字", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.img_recy_item_1_pic:
                        Toast.makeText(getActivity(), "点击了第" + (position + 1) + "张图片", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });*/
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
    }

    private void addData() {
        Map<String, Object> map = null;
        Random random = new Random();
        for (int i = 0; i < urls.length; i++) {
            map = new HashMap<>();
            map.put("url", urls[i]);
            //map.put("name", names[i]);
            //map.put("desc", "我是一只" + names[i]);
            lineData.add(new MyMultipleItem(1, map));
            /*if (i % 2 == 0) {
                lineData.add(new MyMultipleItem(1, map));
            } else {
                lineData.add(new MyMultipleItem(2, map));
            }*/
        }
    }
}
