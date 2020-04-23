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
    private List<String> list=new ArrayList<>();

    public StaggeredGridAdapter(Context mContext) {
        this.mContext = mContext;
        for(int i=0;i<30;i++){
            list.add(String.format("%s-%s", i/10+1,i));
        }
    }
    @Override
    public StaggeredGridAdapter.LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_staggere_grid_item,parent,false));
    }

    @Override
    public void onBindViewHolder(StaggeredGridAdapter.LinearViewHolder holder, int position) {
        if(position%2==0){
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
        }

    }

    @Override
    public int getItemCount() {
        return 30;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private CardView mCardView;
        public LinearViewHolder(View itemView){
            super(itemView);
            mImageView=(ImageView) itemView.findViewById(R.id.iv);
            mCardView= itemView.findViewById(R.id.card);
        }
    }
}
