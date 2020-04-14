package cn.edu.hebtu.software.test.Fragment;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
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
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.edu.hebtu.software.test.Data.Note;
import cn.edu.hebtu.software.test.DetailActivity.ShowNoteActivity;
import cn.edu.hebtu.software.test.R;
import cn.edu.hebtu.software.test.Setting.MyApplication;
import cn.edu.hebtu.software.test.Util.DetermineConnServer;


/**
 * @ProjectName:    MoJi
 * @Description:    足迹界面
 * @Author:         李春波
 * @CreateDate:     2019/11/26 15:40
 * @Version:        1.0
 */
public class FootprintFragment extends Fragment {
    private View view;
    private MapView mapView = null;
    //百度地图控制器
    private BaiduMap baiduMap;
    //UI控制器
    private UiSettings uiSettings;
    //定位服务类
    private LocationClient locationClient;
    //定位选项类
    private LocationClientOption locationClientOption;
    //数据源
    private List<Note> list;
    //监听器
    private MyMapStatuChangeListener statusListener;
    private MyMarkerOnClickListener markerOnClickListener;
    //当前页面经纬度范围
    private double left;
    private double right;
    private double top;
    private double bottom;
    //当前定位经纬度坐标
    private double latitude;
    private double longitude;
    private String location;
    //ip地址
    private String ip;
    //用户id
    private String userId;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    removeMarkerOption();
                    list = null;
                    list = (List<Note>)msg.obj;
                    //将获取到的数据展示在地图上
                    showMarkerOption();
                    break;
                case 2:
                    Toast.makeText(getActivity().getApplicationContext(), (CharSequence)msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.footprint_layout,container,false);
        TextView title = getActivity().findViewById(R.id.toorbar_title);
        title.setText("足迹");
        final MyApplication data = (MyApplication) getActivity().getApplication();
        ip = data.getIp();
        userId = data.getUser().getUserId();


        getViews();

        initMap();

        return view;
    }

    /**
     *  @author 春波
     *  @time 2019/11/25  16:06
     *  @Description：获取地图控件
     */
    private void getViews(){
        // 获取地图控件引用
        mapView = view.findViewById(R.id.bMapView);
        baiduMap = mapView.getMap();
        uiSettings = baiduMap.getUiSettings();
    }

    /**
     *  @author 春波
     *  @time 2019/11/12  15:05
     *  @Description：初始化地图
     */
    private void initMap() {

        //设置显示指南针
        baiduMap.setCompassEnable(true);
        uiSettings.setCompassEnabled(true);

        //设置指南针显示的位置
        baiduMap.setCompassPosition(
                new Point(100,100)
        );
        //显示俯视图
        showOverLook();
        //比例尺操作
        zoomLevelOp();
        //设置图层定位
        baiduMap.setMyLocationEnabled(true);
        //定位操作
        locationOption();
        //设置监听器
        registListener();
    }

    /**
     *  @author 春波
     *  @time 2019/11/12  16:17
     *  @Description：给控件设置监听器
     */
    private void registListener(){
        statusListener = new MyMapStatuChangeListener();
        markerOnClickListener = new MyMarkerOnClickListener();
        baiduMap.setOnMapStatusChangeListener(statusListener);
        baiduMap.setOnMarkerClickListener(markerOnClickListener);
    }

    /**
     *  @author 春波
     *  @time 2019/11/12  14:36
     *  @Description：比例尺操作
     */
    private void zoomLevelOp() {
        //设置允许放大和缩小的比例范围
        baiduMap.setMaxAndMinZoomLevel(21, 6);
        //设置默认比例为100m
        MapStatusUpdate msu = MapStatusUpdateFactory
                .zoomTo(19);
        baiduMap.setMapStatus(msu);
    }

    /**
     *  @author 春波
     *  @time 2019/11/14  20:02
     *  @Description：显示俯视图效果
     */
    private void showOverLook() {
        MapStatus mapStatus =
                new MapStatus.Builder()
                        .overlook(0)//0 —— -45度
                        .build();
        MapStatusUpdate msu =
                MapStatusUpdateFactory.newMapStatus(mapStatus);
        //应用地图状态
        baiduMap.setMapStatus(msu);
    }

    /**
     *  @author 春波
     *  @time 2019/11/12  16:02
     *  @Description：地图状态改变监听器
     */
    class MyMapStatuChangeListener implements BaiduMap.OnMapStatusChangeListener {


        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            getRange();
            new Thread() {
                public void run() {
                    try {
                        if(DetermineConnServer.isConnByHttp(getActivity().getApplicationContext())) {
                            List<Note> threadList = new ArrayList<>();
                            URL url = new URL("http://" + ip + ":8080/MoJi/note/list?left=" + left + "&right=" + right + "&top=" + top + "&bottom=" + bottom + "&userId=" + userId);
                            URLConnection conn = url.openConnection();
                            InputStream in = conn.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                            String str = null;
                            while ((str = reader.readLine()) != null) {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<Note>>() {
                                }.getType();
                                threadList = gson.fromJson(str, type);
                            }

                            Message msg = Message.obtain();
                            msg.obj = threadList;
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }else{
                            Message msg = Message.obtain();
                            msg.obj = "未连接到服务器";
                            msg.what = 2;
                            handler.sendMessage(msg);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    /**
     *  @author 春波
     *  @time 2019/12/3  20:02
     *  @Description：marker点击事件监听器
     */
    class MyMarkerOnClickListener implements BaiduMap.OnMarkerClickListener{

        @Override
        public boolean onMarkerClick(Marker marker) {
            //从marker中获取info信息
            Bundle bundle = marker.getExtraInfo();
            final Note note = (Note) bundle.getParcelable("note");

            LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
            View view = inflater.inflate(R.layout.activity_my_foot_infowindow, null);
            final TextView textTime = (TextView) view.findViewById(R.id.text_item_time);
            final TextView textLatLng = (TextView)view.findViewById(R.id.text_item_latlng);
            final TextView textTitle = (TextView) view.findViewById(R.id.ed_item_title);
            Button btnSee = (Button) view.findViewById(R.id.btn_see);
            Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
            textTime.setText("发布时间:" + note.getTime());
            textLatLng.setText("纬度:"+note.getLatitude()+";经度:"+note.getLongitude());
            textTitle.setText("标题:"+note.getTitle());
            final LatLng lngFinal = new LatLng(note.getLatitude(),note.getLongitude());
            //点击view上面的按钮调用方法
            btnSee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //全局变量设置当前便签id
                    final MyApplication data = (MyApplication) getActivity().getApplication();
                    data.setCurrentNoteId(note.getNoteId());

                    Intent jumpToShow = new Intent(getActivity(), ShowNoteActivity.class);
                    Gson gson = new Gson();
                    String noteStr = gson.toJson(note);
                    jumpToShow.putExtra("noteJsonStr", noteStr);
                    startActivity(jumpToShow);
                }
            });
            //点击取消按钮
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    baiduMap.hideInfoWindow();
                }
            });

            InfoWindow infoWindow = new InfoWindow(view, lngFinal, -47);
            baiduMap.showInfoWindow(infoWindow);

            return true;
        }
    }

    /**
     *  @author 春波
     *  @time 2019/11/12  16:51
     *  @Description：获取当前屏幕左上角和右下角经纬度
     */
    private void getRange(){
        //获取手机的宽高
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        int Max_X=dm.widthPixels;
        int Max_Y=dm.heightPixels;
        Log.e("on: ", "屏幕宽:"+Max_X+" 高:"+Max_Y);

        Point pt = new Point(0,0);
        LatLng westNorth = baiduMap.getProjection().fromScreenLocation(pt);
        left = westNorth.latitude;
        top = westNorth.longitude;
        Log.e("左上角经纬度 ", "x:" + westNorth.latitude +"y:" + westNorth.longitude);

        Point pty = new Point();
        pty.x=dm.widthPixels;
        pty.y=dm.heightPixels;
        LatLng eastSouth = baiduMap.getProjection().fromScreenLocation(pty);
        right = eastSouth.latitude;
        bottom = eastSouth.longitude;
        Log.e("右下角经纬度 ", "x:" + eastSouth.latitude +"y:" + eastSouth.longitude);
    }

    /**
     *  @author 春波
     *  @time 2019/11/26  10:07
     *  @Description：删除标注覆盖物
     */
    private void removeMarkerOption(){
        baiduMap.clear();
    }

    /**
     *  @author 春波
     *  @time 2019/11/12  14:36
     *  @Description：显示标注覆盖物
     */
    private void showMarkerOption() {
        if(list.size() > 0){
            Marker marker;
            for(final Note note:list){
                //1、创建标注覆盖物显示位置的LatLng对象
                final LatLng latLng = new LatLng(note.getLatitude(),note.getLongitude());
                //2、创建标注覆盖物对象
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.activity_baidumap_zj);
                OverlayOptions markerOption = new MarkerOptions()
                        .alpha(0.8f)//透明度
                        .icon(icon)//覆盖物图标
                        .position(latLng)//覆盖物位置
                        .rotate(0)//旋转角度，逆时针为正
                        .perspective(false);//是否支持近大远小效果
                //3、把标注覆盖物添加到地图上,添加marker
                marker = (Marker)baiduMap.addOverlay(markerOption);
                //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
                Bundle bundle = new Bundle();
                //info必须实现序列化接口

                bundle.putParcelable("note", note);
                marker.setExtraInfo(bundle);
            }
        }else{
            Log.e("showMarkerOption: ", "没有数据");
        }

    }

    /**
     *  @author 春波
     *  @time 2019/11/12  17:21
     *  @Description：将地图定位到当前所在位置
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
        //设置定位坐标系
        //locationClientOption.setCoorType("gcj02");
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
        locationClient.registerLocationListener(
                new BDAbstractLocationListener() {
                    @Override
                    public void onReceiveLocation(BDLocation bdLocation) {
                        //                        //获取定位详细数据
                        //                        //获取地址信息
                        String addr = bdLocation.getAddrStr();
                        Log.i("lcb", "地址：" + addr);
                        //获取经纬度
                        latitude = bdLocation.getLatitude();
                        longitude = bdLocation.getLongitude();
                        Log.i("lcb", "纬度："+latitude+";经度："+longitude);
                        //获取当前省
                        Log.i("lcb","所在省:" + bdLocation.getProvince());
                        //获取当前城市
                        Log.i( "lcb ","所在城市:" + bdLocation.getCity());
                        Log.i("lcb", "所在县/区"+bdLocation.getDistrict());
                        Log.i("lcb", "所在街道"+bdLocation.getStreet());


                        //获取周边POI信息
                        List<Poi> pois = bdLocation.getPoiList();
                        location = bdLocation.getCity()+"."+pois.get(0).getName();
                        Log.e( "onReceiveLocation: ", location);
                        for (Poi p:pois) {
                            String name = p.getName();
                            String pAddr = p.getAddr();
                            Log.i("lcb", "POI:" + name+":"+pAddr);
                        }
                        String time = bdLocation.getTime();
                        Log.i("lcb", time);
                        //将定位数据显示在地图上
                        showLocOnMap(latitude, longitude);
                        MyApplication data = (MyApplication)getActivity().getApplication();
                        data.setLatitude(latitude);
                        data.setLongitude(longitude);
                        data.setLocation(location);

                        Log.e("isRange-latitude", latitude+"");
                    }
                }
        );
    }

    /**
     *  @author 春波
     *  @time 2019/11/12  17:23
     *  @Description：将定位数据显示在地图上
     */

    private void showLocOnMap(double lat, double lng) {

        //  获取定位图标
//        BitmapDescriptor icon = BitmapDescriptorFactory
//                .fromResource(R.mipmap.activity_baidumap_self);
        //设置显示方式
        MyLocationConfiguration config =
                new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.NORMAL,//普通态
                        false,//是否需要方向
                        null
                );
        //应用显示方式
        baiduMap.setMyLocationConfiguration(config);
        LatLng latLng = new LatLng(lat,lng);
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