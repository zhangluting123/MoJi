package com.example.ana3.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ana3.LinkToDB.DataBean;
import com.example.ana3.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.myholder> {

    // private static int tag=1;
    // private List<Integer> datas;
    private List<DataBean> lists;
    private Context context;
    private int pos;
    private OnItemClickListener listener;
    //public ImageView imageView;
    //private TextView textView;

    public RecyclerViewAdapter(List lists, Context context) {
        this.lists = lists;
        this.context = context;
        //this.datas = datas;
    }

    public class myholder extends RecyclerView.ViewHolder{
        private final ImageView imageView;
        //private final TextView textView;
        public myholder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            //textView = itemView.findViewById(R.id.title);
        }
    }

    @Override
    public RecyclerViewAdapter.myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        myholder holder =new myholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false));

        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerViewAdapter.myholder holder,final int position) {
        //设置上部图片
        Glide.with(context).load("http://192.168.43.126:8080/CakeShopServer/"+lists.get(position).getImage()+"")
                .into(holder.imageView);

        if(position == pos){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.itemView.setBackground(context.getDrawable(R.drawable.border_chosen));
            }
        }else{
            holder.itemView.setBackground(null);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    longClickListener.onClick(position);
                }
                return true;
            }
        });
    }

    public void setBg(int pos){
        this.pos = pos;
        notifyDataSetChanged();
    }

    public void fresh(){
        //notify();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    //定义接口
    public interface OnItemClickListener {
        void onClick(int position);
    }

    //写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemLongClickListener {
        void onClick(int position);
    }

    private OnItemLongClickListener longClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }
}
