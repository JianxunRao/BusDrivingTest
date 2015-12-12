package com.trojx.busdrivingtest.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.github.mikephil.charting.charts.LineChart;
import com.trojx.busdrivingtest.R;
import com.trojx.busdrivingtest.Util.CoordinateConversion;
import com.trojx.busdrivingtest.Util.WGS84_BD09;
import com.trojx.busdrivingtest.domain.Point;
import com.trojx.busdrivingtest.domain.Record;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    FloatingActionButton fab;
    TextView tv_xAccel;
    TextView tv_yAccel;
    TextView tv_zAccel;
    TextView tv_xOrien;
    TextView tv_yOrien;
    TextView tv_zOrien;
    TextView tv_Lng;
    TextView tv_Lat;
    LineChart lc_accel;
    LineChart lc_orien;
    MapView mapView;
    LinearLayout ll_accel;
    LinearLayout ll_orien;
    SensorManager sensorManager;
    LocationManager locationManager;
    Record nowRecord;
    BaiduMap baiduMap;
    String provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_test);
        nowRecord=new Record();//初始化record
        //nowRecord.setSN(); 从初始设置Activity传过来
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tv_xAccel= (TextView) findViewById(R.id.tv_xAccel);
        tv_yAccel= (TextView) findViewById(R.id.tv_yAccel);
        tv_zAccel= (TextView) findViewById(R.id.tv_zAccel);
        tv_xOrien= (TextView) findViewById(R.id.tv_xOrien);
        tv_yOrien= (TextView) findViewById(R.id.tv_yOrien);
        tv_zOrien= (TextView) findViewById(R.id.tv_zOrien);
        tv_Lng= (TextView) findViewById(R.id.tv_Lng);
        tv_Lat= (TextView) findViewById(R.id.tv_Lat);
        lc_accel= (LineChart) findViewById(R.id.lc_accel);
        lc_orien= (LineChart) findViewById(R.id.lc_orien);
        ll_accel= (LinearLayout) findViewById(R.id.ll_accel_detail);
        ll_orien= (LinearLayout) findViewById(R.id.ll_orien_detail);
        mapView= (MapView) findViewById(R.id.map_view);
        baiduMap=mapView.getMap();
        locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取可用的位置提供器
        List<String> providerList=locationManager.getProviders(true);
        if(providerList.contains(LocationManager.GPS_PROVIDER)){
            provider=LocationManager.GPS_PROVIDER;
            Toast.makeText(this,"使用GPS位置提供器",Toast.LENGTH_SHORT).show();
        }else if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
            provider=LocationManager.NETWORK_PROVIDER;
            Toast.makeText(this,"使用NETWORK位置提供器",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"没有可用的位置提供器",Toast.LENGTH_SHORT).show();
        }

        PackageManager pkm = getPackageManager();
        boolean has_permission = (PackageManager.PERMISSION_GRANTED
                == pkm.checkPermission("android.permission.ACCESS_FINE_LOCATION", "com.trojx.busdrivingtest"));

        Location location=locationManager.getLastKnownLocation(provider);
        if(location!=null) {
                navigateTo(location);
            }
        locationManager.requestLocationUpdates(provider,5000,1,locationListener);
//        checkPermission();
//        requestPermissions();

        /**
         * fab按钮监听
         */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
        /**
         * 加速度传感器、磁场传感器
         */
         sensorManager= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensorAccel=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener,sensorAccel,SensorManager.SENSOR_DELAY_GAME);//在此修改传感器灵敏度
        Sensor sensorMagnetic=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(listener,sensorMagnetic,SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * 地图定位到指定location
     * @param location
     */
    private  void navigateTo(Location location){
            Point point= WGS84_BD09.trans(location.getLongitude(), location.getLatitude());
            LatLng latLng=new LatLng(point.getLat(),point.getLng());
            MapStatusUpdate update= MapStatusUpdateFactory.newLatLng(latLng);
            baiduMap.animateMapStatus(update);
            update=MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
    }
    /**
     * 位置传感器监听
     */
    private LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(location!=null){
                navigateTo(location);
                Toast.makeText(TestActivity.this,"onLocationChanged",Toast.LENGTH_LONG).show();
                Point point= WGS84_BD09.trans(location.getLongitude(), location.getLatitude());
                nowRecord.setLat((float) point.getLat());//直接保存转换后的百度坐标
               nowRecord.setLng((float) point.getLng());
                refreshAll();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    /**
     * 加速度、磁场传感器监听
     */
    private SensorEventListener listener=new SensorEventListener() {
        float[] accelerometerValues=new float[3];
        float[] magneticValues=new float[3];
        @Override
        public void onSensorChanged(SensorEvent event) {
            //判断传感器种类
            if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER)   {
                nowRecord.setxAccel(event.values[0]);
                nowRecord.setyAccel(event.values[1]);
                nowRecord.setzAccel(event.values[2]);
                accelerometerValues=event.values.clone();
            }else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
                magneticValues=event.values.clone();
            }
            float[] R=new float[9];
            float[] values=new float[3];
            sensorManager.getRotationMatrix(R,null,accelerometerValues,magneticValues);
            sensorManager.getOrientation(R, values);
            nowRecord.setxOrien(values[1]);
            nowRecord.setyOrien(values[2]);
            nowRecord.setzOrien(values[0]);
            refreshAll();//刷新全局显示
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    /**
     * 刷新显示
     */
    public void refreshAll(){
        tv_xAccel.setText("X加速度："+nowRecord.getxAccel());
        tv_yAccel.setText("Y加速度："+nowRecord.getyAccel());
        tv_zAccel.setText("Z加速度："+nowRecord.getzAccel());
        tv_xOrien.setText("X方向角："+Math.toDegrees(nowRecord.getxOrien()));
        tv_yOrien.setText("Y方向角："+Math.toDegrees(nowRecord.getyOrien()));
        tv_zOrien.setText("Z方向角："+Math.toDegrees(nowRecord.getzOrien()));
        tv_Lng.setText("经度："+nowRecord.getLng());
        tv_Lat.setText("纬度："+nowRecord.getLat());
        //..

    }

    








    /**
     * mapview生命周期管理
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(sensorManager!=null){
            sensorManager.unregisterListener(listener);//注销加速度、磁场传感器监听
        }
        if(locationManager!=null){
            locationManager.removeUpdates(locationListener);//注销位置监听
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
