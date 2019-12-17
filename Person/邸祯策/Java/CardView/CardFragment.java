package com.example.ana3.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ana3.LinkToDB.DataBean;
import com.example.ana3.LinkToDB.Link;
import com.example.ana3.MainActivity;
import com.example.ana3.R;
import com.example.ana3.StaticX;
import com.example.ana3.CardView.CardToUpdate.Update;

import java.util.List;

public class CardFragment extends Fragment implements GestureDetector.OnGestureListener {

    private int index;// id
    private String title;// 题目
    private String content;// 内容
    private String time;
    private String place;
    private String img;
    private TextView titleView;
    private TextView contentView;
    private TextView indextag;
    public Intent intent;

    protected static Handler mHandler = new Handler(Looper.getMainLooper());
    private CardView mCardView;

    StaticX staticX = new StaticX();
    List<DataBean> list = staticX.getList();

    // 双重判定，保证懒加载
    protected boolean isVisible;// 这个，标记，当前Fragment是否可见
    private boolean isPrepared = false;// 这个，标记当前Fragment是否已经执行了onCreateView
    // 只有两个标记同时满足，才进行数据加载
    // private ProgressBar processBar;转动加载
    ImageView imageView;

    // 滑动手势
    private GestureDetector detector;
    MainActivity.MyOnTouchListener myOnTouchListener;

    public CardFragment(){ }

    public CardFragment(int index){
        this.index = index;
    }

    public CardFragment(int index,String img){
        this.index = index;
        this.img = img;
    }

    public CardFragment(int index,String title,String content,String time,String place){
        this.index = index;
        this.content = content;
        this.title = title;
        this.time = time;
        this.place = place;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        isPrepared = true;
        View view = inflater.inflate(R.layout.fragment_adapter, container, false);
        mCardView = view.findViewById(R.id.cardView);
        mCardView.setMaxCardElevation(mCardView.getCardElevation()
                * CardAdapter.MAX_ELEVATION_FACTOR);

        titleView = view.findViewById(R.id.title);
        contentView = view.findViewById(R.id.content);
        imageView = view.findViewById(R.id.add);
        indextag = view.findViewById(R.id.index);

        //根据数组中属性img设置相应图片
        Glide.with(getContext()).load("http://192.168.43.126:8080/CakeShopServer/"+img+"")
                .into(imageView);

        TextView tap = view.findViewById(R.id.timeandplace);
        for(int i = 0 ; i < list.size(); i++){
            if(list.get(i).getIndex()==this.index){
                titleView.setText(list.get(i).getTitle());
                contentView.setText(list.get(i).getContent());
                tap.setText(list.get(i).getTime()+list.get(i).getPlace());
                //indextag.setText(list.get(i).getIndex()+"");
            }
        }

        final GestureDetector mGestureDetector = new GestureDetector(
                getActivity(), this);
        myOnTouchListener = new MainActivity.MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent ev) {
                boolean result = mGestureDetector.onTouchEvent(ev);
                return result;
            }
        };

        ((MainActivity) getActivity()).registerMyOnTouchListener(myOnTouchListener);
        onLazyLoad();

        // 一键分享
        // 启动分享发送的属性
        ImageView share = view.findViewById(R.id.share);
        ImageView delete = view.findViewById(R.id.delete);
        View.OnClickListener listener0 = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain"); // 分享发送的数据类型
                String msg = contentView.getText().toString();
                // 分享的内容
                intent.putExtra(Intent.EXTRA_TEXT, msg);
                // 目标应用选择对话框的标题
                startActivity(Intent.createChooser(intent, "选择分享"));
            }
        };
        //删除点击事件
        View.OnClickListener listener1 = new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity mainActivity = new MainActivity();
                //刷新上部图片
                mainActivity.getAdapter().fresh();
                //删除数据库数据
                new Link(2,index+"");

                //Log.e("prev",staticX.getPrevonePosition(index)+"");
                //Log.e("this.index",index+"");

                //删除下部对应卡片后显示到删除卡片的前一张卡片
                mainActivity.getmViewPager().setCurrentItem(staticX.getPrevonePosition(index));//无过度放大效果
                //安卓端本地list数组删除对应数据
                mainActivity.getLists().remove(mainActivity.getDataBeanByIndex(index));
                //全局变量删除对应数据
                staticX.listRemove(index);
                //页面效果删除本cardfragment（卡片）
                mainActivity.getmFragmentCardAdapter().deleteCardFragment(CardFragment.this);
            }
        };
        // 修改点击事件
        View.OnClickListener listener2 = new View.OnClickListener() {
            public void onClick(View v) {
                Log.e("on","click");
                intent = new Intent();
                intent.setClass(getActivity(), Update.class);
                intent.putExtra("title", titleView.getText().toString());
                intent.putExtra("content", contentView.getText().toString());
                intent.putExtra("index", getIndex());
                startActivityForResult(intent, 200);
            }
        };
        if(this.index!=-1){
            titleView.setOnClickListener(listener2);
        }else {
            //Toast.makeText(getActivity(), "模板不可修改", Toast.LENGTH_SHORT).show();
        }
        share.setOnClickListener(listener0);
        if(this.index!=-1){
            delete.setOnClickListener(listener1);
        }else {
            //Toast.makeText(getActivity(), "模板不可删除", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private void onLazyLoad() {
        if (isPrepared && isVisible) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isPrepared = false;// 懒加载，只加载一次,这句话要不要，就具体看需求
                    initData();
                }
            }, 3000);
        } else {
            Log.d("onLazyLoadTag","拒绝执行initData，因为条件不满足");
        }
    }

    // protected abstract void initData();抽象方法（原本为BaseFragment）

    public CardView getCardView() {
        return mCardView;
    }

    //手势滑动
    public void flingUp() {// 自定义方法：处理滑动事件
        // Toast.makeText(getActivity(), "内部向上滑", Toast.LENGTH_SHORT).show();
        if(staticX.getTagup()==1) {
            ((MainActivity) this.getActivity()).showBottomDialog(mCardView);
        }
    }

    public void flingDown() {//自定义方法：处理滑动事件
        // Toast.makeText(getActivity(), "内部向下滑", Toast.LENGTH_SHORT).show();
        if(staticX.getTagup()==1) {
            //((MainActivity) this.getActivity()).showTopDialog(mCardView);
        }
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (e1.getY() - e2.getY() < -200) {
                if(staticX.getTagup()==1){
                    flingDown();
                    staticX.setTagup(0);
                }
                return true;
            } else if (e1.getY() - e2.getY() > 200) {
                if(staticX.getTagup()==1){
                    flingUp();
                    staticX.setTagup(0);
                }
                return true;
            }
        } catch (Exception e) { }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    public void initData(){

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 触摸事件的注销
        ((MainActivity)this.getActivity()).unregisterMyOnTouchListener(myOnTouchListener);
    }

    @Override
    //其他界面返回响应数据时回调
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                ;
                break;
            case 200:
                titleView.setText(data.getStringExtra("titled"));
                contentView.setText(data.getStringExtra("contentd"));
                //new MainActivity().getmFragmentCardAdapter().fresh();
                break;
            default:
                break;
        }
    }

    public int getIndex() {
        return index;
    }


}

