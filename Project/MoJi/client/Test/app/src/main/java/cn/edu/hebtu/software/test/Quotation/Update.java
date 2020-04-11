package cn.edu.hebtu.software.test.Quotation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cn.edu.hebtu.software.test.R;

public class Update extends AppCompatActivity {

    int p;
    Link link = new Link(4);
    StaticX staticX = new StaticX();
    List<DataBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        //接收来自MainActivity的数据
        final Intent request = getIntent();
        //String title = request.getStringExtra("title");
        //String content = request.getStringExtra("content");
        final int index = request.getIntExtra("index",-1);
        final EditText editText = findViewById(R.id.tv_title);
        final EditText editText1 = findViewById(R.id.tv_content);
        list = staticX.getList();
        for (int a = 0; a < list.size(); a++) {
            Log.e("getindex",list.get(a).getIndex()+"");
            //Log.e("this.index",getIndex()+"");
            if ( list.get(a).getIndex()== index) {
                p=a;
                Log.e("on","click");
                String titleup = list.get(a).getTitle();
                String contentup = list.get(a).getContent();
                Log.e("title",titleup);
                editText.setText(titleup);
                editText1.setText(contentup);
            }
        }

        //Log.i("title",title);

        Button button = findViewById(R.id.btn_modify);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送修改后的数据到MainActivity
                Intent response = new Intent();
                response.putExtra("titled",editText.getText().toString());
                response.putExtra("contentd",editText1.getText().toString());
                Log.e("p",p+"");
                Log.e("index",index+"");
                list.get(p).setTitle(editText.getText().toString());
                list.get(p).setContent(editText1.getText().toString());
                new Link(3,list.get(p).getIndex()+","+editText.getText().toString()+","+editText1.getText().toString());

                //响应
                setResult(200,response);
                //结束当前的Activity
                finish();
            }
        });
    }
}
