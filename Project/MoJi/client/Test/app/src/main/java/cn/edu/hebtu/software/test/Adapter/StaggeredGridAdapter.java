package cn.edu.hebtu.software.test.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import cn.edu.hebtu.software.test.R;

/**
 * @Author: 邸祯策
 * @Date: 2019/04/23
 * @Describe:
 */

public class StaggeredGridAdapter extends RecyclerView.Adapter<StaggeredGridAdapter.LinearViewHolder>{
    private Context mContext;
    private AdapterView.OnItemClickListener mListener;
    private List<Integer> list=new ArrayList<>();
    private int[] ids = {R.drawable.demo1,R.drawable.demo2,R.drawable.demo3,R.drawable.demo4,R.drawable.demo5,R.drawable.demo6,R.drawable.demo7,R.drawable.demo8};

    public StaggeredGridAdapter(Context mContext) {
        this.mContext = mContext;
        for(int i=0;i<8;i++){
            list.add(ids[i]);
        }
    }

    public void replaceAll(List<Integer> mylist) {
        list.clear();
        if (mylist != null && mylist.size() > 0) {
            list.addAll(list);
        }
        notifyDataSetChanged();
    }

    /**
     * 插入数据使用notifyItemInserted，如果要使用插入动画，必须使用notifyItemInserted
     * 才会有效果。即便不需要使用插入动画，也建议使用notifyItemInserted方式添加数据，
     * 不然容易出现闪动和间距错乱的问题
     * */
    public void addData(int position,ArrayList<Integer> list) {
        list.addAll(position,list);
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
        holder.setData(list.get(position));

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
        return list != null ? list.size() : 0;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private CardView mCardView;
        public LinearViewHolder(View itemView){
            super(itemView);
            mImageView=(ImageView) itemView.findViewById(R.id.iv);
            mCardView= itemView.findViewById(R.id.card);
        }

        void setData(Object data) {
            if (data != null) {
                int id = (int) data;
                mImageView.setImageResource(id);
            }
        }
    }
}
