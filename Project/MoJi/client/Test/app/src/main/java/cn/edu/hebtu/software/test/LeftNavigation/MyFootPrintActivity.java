package cn.edu.hebtu.software.test.LeftNavigation;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.edu.hebtu.software.test.Activity.MainActivity;
import cn.edu.hebtu.software.test.Adapter.MyFootAdapter;
import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.DetailActivity.ShowNoteActivity;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.ActivityManager;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;
import cn.edu.hebtu.software.test.R;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class MyFootPrintActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listView;
    private List<Note> noteList;
    private String userId;
    private MyApplication data;
    private String ip;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    Toast.makeText(getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_foot_print);

        ActivityManager.getInstance().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.MyTheam_color));

        }

        //获取全局变量
        data = (MyApplication) getApplication();
        userId = data.getUser().getUserId();

        init();
        toolbar = findViewById(R.id.foot_toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent response = new Intent(MyFootPrintActivity.this, MainActivity.class);
                response.putExtra("flag",true);
                startActivity(response);
                overridePendingTransition(R.animator.in_from_left, R.animator.out_to_right);
                finish();
            }
        });


        if(noteList!=null && noteList.size()>0){
            listView = findViewById(R.id.lv_footList);
            MyFootAdapter myFootAdapter = new MyFootAdapter(R.layout.item_footlist,this,noteList);
            listView.setAdapter(myFootAdapter);

        }
    }

    private void init() {
        Thread getNote = new GetNote();
        getNote.start();
        try {
            getNote.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class GetNote extends Thread{
        @Override
        public void run() {

            try {
                boolean b = DetermineConnServer.isConnByHttp(getApplicationContext());
                if(b){
                    URL url = new URL("http://"+getResources().getString(R.string.internet_ip)+":8080/MoJi/note/download?userId=" + userId);
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                    String str = reader.readLine();
                    if(null != str){
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Note>>(){}.getType();
                        noteList = gson.fromJson(str,type);
                    }
                    in.close();
                    reader.close();
                }else{
                    Message message = new Message();
                    message.what = 100;
                    message.obj ="未连接到服务器";
                    handler.sendMessage(message);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
