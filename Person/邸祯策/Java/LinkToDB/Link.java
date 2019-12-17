package com.example.ana3.LinkToDB;

import android.os.Message;
import android.util.Log;

import com.example.ana3.MainActivity;
import com.example.ana3.StaticX;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class Link {

    public static int tagm = 1;
    public static int tag = 1;
    StaticX staticX = new StaticX();

    public int command;// for query
    public String toServerData;
    public String backData;
    static List<DataBean> list;

    public Link(int command){
        this.command=command;
        sendToServer();
    }

    public Link(int command,String data){
        this.command=command;
        this.toServerData=data;
        sendToServer();
    }

    public void sendToServer() {
        new Thread() {
            @Override
            public void run() {
                try{
                    if(tag == 1){
                        staticX.addone();
                        tag=0;
                    }
                    // 借用后端cakeShopServer，固定语录表代号为6
                    URL url = new URL("http://192.168.43.126:8080/CakeShopServer/Servlet?infot="+"6"+"&infoc="+command+"&infod="+toServerData);
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String info = reader.readLine();
                    if(command==4){
                        if(tagm==1){
                            wrapperMessage(info);
                            tagm=0;
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void wrapperMessage(String info) {

        Message msg = Message.obtain();
        msg.obj = info;
        backData = (String) msg.obj;
        // 数据拆分放入实体列表
        list = staticX.getList();

        String[] splitAll = backData.split(";");
        Log.i("list length", splitAll.length+"");

        for(int i=0;i<splitAll.length;i++) {
            String[] splitLine = splitAll[i].split(",");
            Log.e("splitLine[0]", splitLine[0]+"");
            DataBean dataBean = new DataBean(Integer.parseInt(splitLine[0]), splitLine[1], splitLine[2], splitLine[3], splitLine[4],splitLine[5]);
            list.add(dataBean);
        }
        staticX.setList(list);
        //list.clear();

    }
}