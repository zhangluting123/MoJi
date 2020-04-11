package cn.edu.hebtu.software.test.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.test.Data.Mail;
import cn.edu.hebtu.software.test.R;

/**
 * @ProjectName: MOjo
 * @Description: java类作用描述
 * @Author: 邸凯扬
 * @CreateDate: 2019/11/26 14:39
 * @Version: 1.0
 */
public class TraceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private List<Mail> mailList = new ArrayList<>(1);
    private static final int TYPE_TOP = 0x0000;
    private static final int TYPE_NORMAL= 0x0001;





    public TraceListAdapter(Context context, List<Mail> mailList) {
        inflater = LayoutInflater.from(context);
        this.mailList = mailList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trace,parent,false);

        ViewHolder holder=new ViewHolder(view);
        //绑定监听事件

        return  holder;
    }
    

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder itemHolder = (ViewHolder) holder;
        if (getItemViewType(position) == TYPE_TOP) {
            // 第一行头的竖线不显示
            itemHolder.tvTopLine.setVisibility(View.INVISIBLE);
            // 字体颜色加深
            itemHolder.tvAcceptTime.setTextColor(0xff555555);

            itemHolder.tvDot.setBackgroundResource(R.drawable.timelline_dot_first);
        } else if (getItemViewType(position) == TYPE_NORMAL) {
            itemHolder.tvTopLine.setVisibility(View.VISIBLE);
            itemHolder.tvAcceptTime.setTextColor(0xff999999);

            itemHolder.tvDot.setBackgroundResource(R.drawable.timelline_dot_normal);
        }

        itemHolder.bindHolder(mailList.get(position));
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
    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener listener;

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




    @Override
    public int getItemCount() {
        return mailList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TOP;
        }
        return TYPE_NORMAL;
    }


    public  class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAcceptTime;
        private TextView tvTopLine, tvDot;
        private TextView acName;
        private TextView title;
        public ViewHolder(View itemView) {
            super(itemView);
            tvAcceptTime = (TextView) itemView.findViewById(R.id.tvAcceptTime);
            acName=(TextView) itemView.findViewById(R.id.acName) ;
            title=(TextView) itemView.findViewById(R.id.title);
            tvTopLine = (TextView) itemView.findViewById(R.id.tvTopLine);
            tvDot = (TextView) itemView.findViewById(R.id.tvDot);

        }

        public void bindHolder(Mail mail) {
            tvAcceptTime.setText(mail.getAcceptTime());
            acName.setText("——>");
            title.setText("来自于"+mail.getOtherName()+"的消息");
        }
    }
}
