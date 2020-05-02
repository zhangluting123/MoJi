package cn.edu.hebtu.software.test.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.R;

import static com.baidu.mapapi.BMapManager.getContext;

/**
 * @Author: 邸祯策
 * @Date: 2019/04/23
 * @Describe:
 */

public class StaggeredGridAdapter extends RecyclerView.Adapter<StaggeredGridAdapter.LinearViewHolder>{
    private Context mContext;
    private AdapterView.OnItemClickListener mListener;
    private List<Integer> list=new ArrayList<>();
    private List<Note> noteList = new ArrayList<>();//数据源

    public StaggeredGridAdapter(Context mContext,List<Note> noteList) {
        this.mContext = mContext;
        //list.addAll(mylist);
        this.noteList = noteList;
    }

    public void replaceAll(List<Integer> mylist) {
        list.clear();
        if (mylist != null && mylist.size() > 0) {
            list.addAll(mylist);
        }
        notifyDataSetChanged();
    }

    /**
     * 插入数据使用notifyItemInserted，如果要使用插入动画，必须使用notifyItemInserted
     * 才会有效果。即便不需要使用插入动画，也建议使用notifyItemInserted方式添加数据，
     * 不然容易出现闪动和间距错乱的问题
     * */
    public void addData(int position,ArrayList<Integer> mylist) {
        list.addAll(position,mylist);
        notifyItemInserted(position);
    }

    //移除数据使用notifyItemRemoved
    public void removeData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public StaggeredGridAdapter.LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_staggere_grid_item,parent,false));
    }

    @Override
    public void onBindViewHolder(StaggeredGridAdapter.LinearViewHolder holder, int position) {
        holder.setData(noteList.get(position));

        /*if(position%2==0){
            holder.mImageView.setImageResource(R.drawable.demo1);
            //ViewGroup.LayoutParams para;
            //para = holder.mImageView.getLayoutParams();
            //para.height = 600;
            //holder.mImageView.setLayoutParams(para);
        }
        else if(position%3==0){
            holder.mImageView.setImageResource(R.drawable.demo7);
        }
        else{
            holder.mImageView.setImageResource(R.drawable.demo2);
        }*/
    }

    @Override
    public int getItemCount() {
        return noteList != null ? noteList.size() : 0;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private CardView mCardView;
        private RoundedImageView roundedImageView;
        private TextView title;
        private TextView petname;
        public LinearViewHolder(View itemView){
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv);
            mCardView = itemView.findViewById(R.id.card);
            roundedImageView = itemView.findViewById(R.id.head);
            title = itemView.findViewById(R.id.title);
            petname = itemView.findViewById(R.id.petname);
        }

        void setData(Object data) {
            if (data != null) {
                Note note = (Note)data;
                //mImageView.setImageResource(id);
                //Glide.with(getContext()).load("http://123.56.175.200:8080/MoJi/image/2.jpg").into(mImageView);
                if(null == note.getUser().getUserHeadImg()){
                    roundedImageView.setImageResource(R.drawable.headportrait);
                }else {
                    Glide.with(getContext()).load("http://123.56.175.200:8080/MoJi/" + note.getUser().getUserHeadImg()).into(roundedImageView);
                }
                if(note.getImgList().size() > 0){
                    Glide.with(getContext()).load("http://123.56.175.200:8080/MoJi/" + note.getImgList().get(0)).into(mImageView);
                }else {
                    mImageView.setImageResource(R.mipmap.default_bg);
                }
                title.setText(note.getTitle());
                petname.setText(note.getUser().getUserName());
            }
        }
    }
}
