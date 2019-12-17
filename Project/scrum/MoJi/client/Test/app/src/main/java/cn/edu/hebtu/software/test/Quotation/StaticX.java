package cn.edu.hebtu.software.test.Quotation;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class StaticX {

    public int tagx = 0;
    public static int tagup = 1;// 0表示关闭
    private static List<DataBean> list = new ArrayList<>();

    public int getTagup() {
        return tagup;
    }

    public void setTagup(int tagup) {
        StaticX.tagup = tagup;
    }

    public List<DataBean> getList() {
        return list;
    }

    public void setList(List<DataBean> list) {
        StaticX.list = list;
    }

    public void addone(){
        list.add(new DataBean(-1,"《夏目友人帐》","只要有想见的人，就不再是孤身一人了。我必须承认生命中的大部分时光是属于孤独的，努力成长是在孤独里可以进行的最好的游戏。","2019/12/17","","images/photo.jpg"));
    }

    public int getAddIndex(){
        if(list.size()==1){
            return 0;
        }else{
            int x=list.get(list.size()-1).getIndex()+1;
            return x;
        }
    }

    public void listAdd(DataBean dataBean){
        list.add(dataBean);
    }

    public void listRemove(int index){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIndex() == index) {
                list.remove(i);
            }
        }
    }

    public int getPrevonePosition(int index) {
        Log.e("index",index+"");
        Log.e("list.size()",list.size()+"");
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIndex() == index) {
                tagx = i - 1;
            }
        }
        return tagx;
    }
}
