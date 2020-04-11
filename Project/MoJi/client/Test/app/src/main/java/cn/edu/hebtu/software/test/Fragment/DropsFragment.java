package cn.edu.hebtu.software.test.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.mapapi.utils.SpatialRelationUtil;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.hebtu.software.test.Adapter.MsgAdapter;
import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.DetailActivity.DropsDetailActivity;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;

/**
 * @ProjectName:    MoJi
 * @Description:    点滴界面
 * @Author:         李晓萌
 * @CreateDate:     2019/11/26 15:39
 * @Version:        1.0
 */
public class DropsFragment extends Fragment {
    private View view;
    private TextView tvRangenum;
    private Button btnUp;
    private Button btnDown;
    private CustomOnClickListener listener;
    private List<Note> noteList = new ArrayList<>();

    private BaiduMap baiduMap;
    private MapView mapView;
    private LocationClient locationClient;
    private LocationClientOption locationClientOption;
    private Double latitude;
    private Double longitude;
    private String location;
    private MyApplication data;
    private String ip;

    private MsgAdapter msgAdapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1001:
                    Toast.makeText(getActivity().getApplicationContext(), (CharSequence) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.drops_layout, container, false);
        TextView title = getActivity().findViewById(R.id.toorbar_title);
        title.setText("点滴");

        getViews();

        registerListener();

        data = (MyApplication)getActivity().getApplication();
        ip = data.getIp();
        if(data.getDistance() == null) {
            tvRangenum.setText(1+"");

        }else{
            tvRangenum.setText(data.getDistance());
        }

        init();


        return view;
    }

    /**
     * @author 张璐婷
     * @time 2019/12/10  14:48
     * @Description：初始化界面
     */

    private void init() {
        //显示俯视图
        showOverLook();
        //比例尺操作
        zoomLevelOp();
        //设置图层定位
        baiduMap.setMyLocationEnabled(true);
        //隐藏百度的LOGO
        View child = mapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }
        // 不显示地图上比例尺
        mapView.showScaleControl(false);
        // 不显示地图缩放控件（按钮控制栏）
        mapView.showZoomControls(false);
        //不显示指南针
        baiduMap.setCompassEnable(false);


        //绑定adapter
        final ListView listView = view.findViewById(R.id.lv_msgs);

        msgAdapter = new MsgAdapter(view.getContext(),
                noteList,
                R.layout.msglist_item);
        listView.setDivider(null);
        listView.setAdapter(msgAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DropsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("note", noteList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //获取数据
        new getMessage().start();

        locationOption();

    }

    private void registerListener() {
        listener = new CustomOnClickListener();
        btnUp.setOnClickListener(listener);
        btnDown.setOnClickListener(listener);
    }

    private void getViews() {
        mapView = view.findViewById(R.id.bMapView);
        baiduMap = mapView.getMap();
        tvRangenum = view.findViewById(R.id.tv_rangenum);
        btnUp = view.findViewById(R.id.btnUp);
        btnDown = view.findViewById(R.id.btnDown);
    }

    class CustomOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnUp:
                    int tvUpNum = Integer.parseInt(tvRangenum.getText().toString().trim());
                    if (tvUpNum < 10) {
                        tvUpNum += 1;
                    }else if(tvUpNum < 50){
                        tvUpNum += 10;
                    }
                    tvRangenum.setText(tvUpNum + "");
                    data.setDistance(tvUpNum+"");
                    break;
                case R.id.btnDown:
                    int tvDownNum = Integer.parseInt(tvRangenum.getText().toString().trim());
                    if (tvDownNum > 10) {
                        tvDownNum -= 10;
                    }else if(tvDownNum > 1){
                        tvDownNum -= 1;
                    }
                    tvRangenum.setText(tvDownNum + "");
                    data.setDistance(tvDownNum+"");
                    break;
            }
            //获取数据
            Log.e("------------", "---------------");
            Thread thread = new getMessage();
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            selectVisualNote();
            msgAdapter.setNoteList(noteList);
            msgAdapter.notifyDataSetChanged();
        }
    }


    /**
     * @author: 张璐婷
     * @time: 2019/12/10  15:10
     * @Description: 定位操作
     */
    private void locationOption() {
        //1. 创建定位服务客户端类的对象
        locationClient = new LocationClient(getActivity().getApplicationContext());
        //2. 创建定位客户端选项类的对象，并设置定位参数
        locationClientOption = new LocationClientOption();
        //设置定位参数
        //打开GPS
        locationClientOption.setOpenGps(true);
        //设置定位间隔时间
        locationClientOption.setScanSpan(1000);
        SDKInitializer.setCoordType(CoordType.GCJ02);
        //设置定位模式:高精度模式
        locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //需要定位的地址数据
        locationClientOption.setIsNeedAddress(true);
        //需要地址描述
        locationClientOption.setIsNeedLocationDescribe(true);
        //需要周边POI信息
        locationClientOption.setIsNeedLocationPoiList(true);
        //3. 将定位选项参数应用给定位服务客户端类的对象
        locationClient.setLocOption(locationClientOption);
        //4. 开始定位
        locationClient.start();
        //5. 给定位客户端端类的对象注册定位监听器
        locationClient.registerLocationListener(new BDAbstractLocationListener() {
                    @Override
                    public void onReceiveLocation(BDLocation bdLocation) {
                        //获取定位详细数据
                        //获取地址信息
                        String addr = bdLocation.getAddrStr();
                        Log.e("lcb", "地址：" + addr);
                        //获取经纬度
                        latitude = bdLocation.getLatitude();
                        longitude = bdLocation.getLongitude();
                        Log.e("lcb", "纬度：" + latitude + ";经度：" + longitude);
                        //获取当前省
                        Log.e("lcb", "所在省:" + bdLocation.getProvince());
                        //获取当前城市
                        Log.e("lcb ", "所在城市:" + bdLocation.getCity());
                        location = bdLocation.getProvince() + "·" + bdLocation.getCity()+"·"+bdLocation.getDistrict();
                        Log.e("onReceiveLocation: ", location);

                        showLocOnMap(latitude, longitude);

                        MyApplication data = (MyApplication) getActivity().getApplication();
                        data.setLatitude(latitude);
                        data.setLongitude(longitude);
                        data.setLocation(location);

                        selectVisualNote();
                        msgAdapter.setNoteList(noteList);
                        msgAdapter.notifyDataSetChanged();

                    }
                }
        );
    }


    /**
     *  @author: 张璐婷
     *  @time: 2019/12/10  16:11
     *  @Description: 获取数据
     */
    class getMessage extends Thread{
        @Override
        public void run() {
            try {
                if (DetermineConnServer.isConnByHttp(getActivity().getApplicationContext())) {
                    URL url = new URL("http://" + ip + ":8080/MoJi/QueryVisualNoteServlet");
                    URLConnection conn = url.openConnection();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String str = null;
                    while ((str = reader.readLine()) != null) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Note>>() {
                        }.getType();
                        noteList = gson.fromJson(str, type);
                        Collections.reverse(noteList);
                    }
                    in.close();
                    reader.close();
                } else {
                    Message message = Message.obtain();
                    message.what = 1001;
                    message.obj = "未连接到服务器";
                    handler.sendMessage(message);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     *  @author: 张璐婷
     *  @time: 2019/12/10  20:47
     *  @Description: 显示定位
     */
    private void showLocOnMap(double lat, double lng) {

        //设置显示方式
        MyLocationConfiguration config =
                new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.NORMAL,
                        false,//是否需要方向
                        null
                );
        //应用显示方式
        baiduMap.setMyLocationConfiguration(config);
        LatLng latLng = new LatLng(lat, lng);
        //显示
        MyLocationData locData = new MyLocationData
                .Builder()
                .latitude(latLng.latitude)
                .longitude(latLng.longitude)
                .build();
        baiduMap.setMyLocationData(locData);

        //移动到中心位置
        MapStatusUpdate msu =
                MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(msu);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2019/12/10  18:42
     *  @Description: 显示俯视图效果
     */
    private void showOverLook() {
        MapStatus mapStatus =
                new MapStatus.Builder()
                        .overlook(0)//0 —— -45度
                        .build();
        MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(mapStatus);
        //应用地图状态
        baiduMap.setMapStatus(msu);
    }
    /**
     *  @author: 张璐婷
     *  @time: 2019/12/10  18:44
     *  @Description: 比例尺操作
     */
    private void zoomLevelOp() {
        //设置允许放大和缩小的比例范围
        baiduMap.setMaxAndMinZoomLevel(19, 19);
        //设置默认比例为100m
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(19);
        baiduMap.setMapStatus(msu);
    }

    /**
     *  @author: 张璐婷
     *  @time: 2019/12/10  17:44
     *  @Description: 筛选在范围内的note
     */
    private void selectVisualNote(){
        for(int i = 0; i < noteList.size();i++){
            Log.e("location", noteList.get(i).getLocation());
            if(noteList.get(i).getLatitude() != null && noteList.get(i).getLongitude() != null){
                if(!isRange(noteList.get(i).getLatitude(), noteList.get(i).getLongitude())){
                    noteList.remove(i);
                    i--;
                }
            }

        }
    }

    /**
     *  @author: 张璐婷
     *  @time: 2019/12/10  15:16
     *  @Description: 判断某一位置是否在当前位置一定范围内，是则显示
     *  var0表示圆心的坐标，var1代表圆心的半径，var2代表要判断的点是否在圆内
     *  isCircleContainsPoint(LatLng var0, int var1, LatLng var2);
     */
    private boolean isRange(double lat,double lon) {
        int num = Integer.parseInt(tvRangenum.getText().toString().trim());
        Log.e("dis",DistanceUtil.getDistance(new LatLng(latitude, longitude),new LatLng(lat, lon))+"");
        return SpatialRelationUtil.isCircleContainsPoint(new LatLng(latitude, longitude), num * 1000, new LatLng(lat, lon));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }
}