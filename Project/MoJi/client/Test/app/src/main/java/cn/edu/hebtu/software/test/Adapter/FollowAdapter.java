package cn.edu.hebtu.software.test.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import cn.edu.hebtu.software.test.Data.Follow;
import cn.edu.hebtu.software.test.R;

public class FollowAdapter extends ArrayAdapter<Follow> {
    private int resourceId;

    // 适配器的构造函数，把要适配的数据传入这里
    public FollowAdapter(Context context, int textViewResourceId, List<Follow> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    // convertView 参数用于将之前加载好的布局进行缓存
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Follow follow=getItem(position); //获取当前项的Fruit实例

        // 加个判断，以免ListView每次滚动时都要重新加载布局，以提高运行效率
        View view;
        ViewHolder viewHolder;
        if (convertView==null){

            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            // 避免每次调用getView()时都要重新获取控件实例
            viewHolder=new ViewHolder();
            viewHolder.followImage=view.findViewById(R.id.fruit_image);
            viewHolder.followName=view.findViewById(R.id.fruit_name);

            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
            view.setTag(viewHolder);
        } else{
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }

        // 获取控件实例，并调用set...方法使其显示出来
        //viewHolder.followImage.setImageResource(follow.getTwoId());
        viewHolder.followName.setText(follow.getTwoId());
        return view;
    }

    // 定义一个内部类，用于对控件的实例进行缓存
    class ViewHolder{
        ImageView followImage;
        TextView followName;
    }
}
