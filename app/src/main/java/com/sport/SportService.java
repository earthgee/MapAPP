package com.sport;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.earthgee.mymap.R;
import com.sport.entity.Location;
import com.sport.entity.Sport;

import java.util.Date;

/**
 * Created by earthgee on 16/4/12.
 * modify 百度地图必须在主线程中进行定位,所以将IntentService更换为RemoteService
 */
public class SportService extends Service implements SensorEventListener{

    private static final int CYCLE_TIME = 1000*30; //10s
    public static final String PREF_IS_ALARM_ON = "isAlarmOn";

    public static final String ACTION_DOWN_NOTIFICATION = "com.earthgee.SPORT_INFOMATION";
    public static final String PERM_PRIVATE = "com.earthgee.mymap.SPORT_DATA";

    private SportDatabaseHelper mDatabaseHelper;
    private LocationClient mLocationClient;
    private Sport mCurrentSport;

    //计步相关
    private SensorManager sensorManager;
    private StepHelper stepHelper;
    private PowerManager.WakeLock wakeLock;

    private int currentStep=0;

    //记录当前行走距离
    private double currentDistance;
    private double currentVeritory;
    //记录上一次经纬度
    private double lastLatitude=0.0;
    private double lastLongitude=0.0;
    //用于计算速度
    private long startTime;

    //保存IStepListener
    private RemoteCallbackList<IStepListener> mStepListeners=new RemoteCallbackList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private IBinder binder= new IStepManager.Stub() {
        @Override
        public void registerStepListener(IStepListener listener) throws RemoteException {
            mStepListeners.register(listener);
        }

        @Override
        public void unregisterStepListener(IStepListener listener) throws RemoteException {
            mStepListeners.unregister(listener);
        }

        @Override
        public void notifyStep() throws RemoteException {

        }
    };

    protected void onHandleIntent(Intent intent) {
        //在这里处理定位，记步等功能
        //1.定位
        //失败,intent service会死掉,无法定位成功
        //startLocation();

        //android 19以上在这里再设置一个定时器
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), SportService.class);
        PendingIntent pi = PendingIntent.getService(getApplicationContext(), 0, i, 0);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT){
            alarmManager.cancel(pi);
            alarmManager.setWindow(AlarmManager.RTC, System.currentTimeMillis(), CYCLE_TIME, pi);
        }
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = new Intent(context, SportService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            //解决android 19之上定时器定时不准确的情况
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT){
                alarmManager.setWindow(AlarmManager.RTC,System.currentTimeMillis(),CYCLE_TIME,pi);
            }else{
                alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), CYCLE_TIME, pi);
            }
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }

        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(SportService.PREF_IS_ALARM_ON, isOn).commit();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDatabaseHelper=new SportDatabaseHelper(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //插入新的sport
        initNewSport();
        //开启定位
        startLocation();
        startStep();
        return super.onStartCommand(intent, flags, startId);
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent intent = new Intent(context, SportService.class);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    private void sendBackGroundNofication(double latitude,double longitude,double veritory,int requestCode, Notification notification) {
        Intent i = new Intent(ACTION_DOWN_NOTIFICATION);
        i.putExtra("REQUEST_CODE", requestCode);
        i.putExtra("NOTIFICATION", notification);
        i.putExtra("latitude", latitude);
        i.putExtra("longitude", longitude);
        i.putExtra("veritory", veritory);

        sendOrderedBroadcast(i, PERM_PRIVATE, null, null, Activity.RESULT_OK, null, null);
    }

    private void startLocation(){
        mLocationClient=new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //获取经纬度
                double latitude = bdLocation.getLatitude();
                double longitude = bdLocation.getLongitude();

                //第一次定位
                if (lastLatitude == 0.0 && lastLongitude == 0.0) {
                    lastLatitude = latitude;
                    lastLongitude = longitude;
                    startTime = System.currentTimeMillis();

                    Location location = new Location();
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);
                    initNewLocation(location);

                    Notification notification = getSportNotification();
                    sendBackGroundNofication(latitude, longitude, currentVeritory, 0, notification);
                } else {
                    if (avaiableNewLocation(new LatLng(lastLatitude, lastLongitude), new LatLng(latitude, longitude))) {
                        //向数据库中插入一条新的location
                        Location location = new Location();
                        location.setLatitude(latitude);
                        location.setLongitude(longitude);
                        initNewLocation(location);
                        currentDistance += calculateDistance(new LatLng(lastLatitude, lastLongitude), new LatLng(latitude, longitude));
                        double veritory = currentDistance / (System.currentTimeMillis() - startTime);
                        currentVeritory = veritory;

                        Notification notification = getSportNotification();
                        sendBackGroundNofication(latitude, longitude, veritory, 0, notification);
                    }
                }
            }
        });

        LocationClientOption option=new LocationClientOption();
        option.setOpenGps(true);
        option.setScanSpan(5000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private void initNewSport(){
        mCurrentSport=insertSport();
    }

    private Sport insertSport(){
        Sport sport=new Sport();
        sport.setId(mDatabaseHelper.insertSport(sport));
        return sport;
    }

    private void initNewLocation(Location location){
        mDatabaseHelper.insertLocation(mCurrentSport.getId(), location);
    }

    /**
     * 开始计步
     */
    private void startStep(){
        Log.d("earthgee2","start step");
        //获得wakelock使得屏幕关闭后继续计步
        acquireWakeLock();

        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT){
            Log.d("earthgee2","use google");
            addCountStepListener();
        }else{
            Log.d("earthgee2", "use mine");
            addStepHelper();
        }
    }

    private void addCountStepListener(){
        Sensor detectorSensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        if(detectorSensor!=null){
            sensorManager.registerListener(this, detectorSensor, SensorManager.SENSOR_DELAY_UI);
        }else{
            addStepHelper();
        }
    }

    private void addStepHelper(){
        stepHelper=new StepHelper();
        Sensor sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(stepHelper, sensor, SensorManager.SENSOR_DELAY_UI);
        Log.d("earthgee2","init sensor success");
        stepHelper.setOnSensorChangeListener(new StepHelper.OnSensorChangeListener() {
            @Override
            public void onChange() {
                currentStep++;
                tryNotifyStepUpdate();
            }
        });
    }

    @Override
    public void onDestroy() {
        Log.d("earthgee", "service is destroy");
        mLocationClient.stop();
        mDatabaseHelper.close();
        //保存步数信息和结束时间
        save();
        releaseWakeLock();
        super.onDestroy();
    }

    private void acquireWakeLock(){
        if(wakeLock==null){
            PowerManager powerManager= (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock=powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,SportService.class.getName());
            wakeLock.acquire();
        }
    }

    private void releaseWakeLock(){
        if(wakeLock!=null){
            wakeLock.release();
            wakeLock=null;
        }
    }

    //4.4以后会走这里,使用系统原生的计步
    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("earthgee2","on change");
        Sensor sensor=event.sensor;
        if(sensor.getType()==Sensor.TYPE_STEP_DETECTOR){
            currentStep++;
        }else if(sensor.getType()==Sensor.TYPE_STEP_COUNTER){
            currentStep= (int) event.values[0];
        }
        tryNotifyStepUpdate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void tryNotifyStepUpdate(){
        Log.d("earthgee2","current step:"+currentStep);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int count=mStepListeners.beginBroadcast();
                        if(count>0){
                            mStepListeners.getBroadcastItem(0).notifyStep(currentStep);
                        }else{
                            //应用不在前台,在通知栏上更新
                           Notification notification=getSportNotification();
                            //Notification notification = new NotificationCompat.Builder(SportService.this).setTicker("test").setSmallIcon(R.mipmap.ic_launcher).setContentText("content").setContentTitle("title").setAutoCancel(false).build();
                            NotificationManager notificationManager= (NotificationManager) SportService.this.getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(0,notification);
                        }
                        mStepListeners.finishBroadcast();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
    }

    private boolean avaiableNewLocation(LatLng last,LatLng now){
        return DistanceUtil.getDistance(last,now)>50.0;
    }

    private double calculateDistance(LatLng last,LatLng now){
        return DistanceUtil.getDistance(last, now);
    }

    //得到定制通知,在状态栏上显示速度,距离,步数
    private Notification getSportNotification(){
        Notification notification=new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("足迹").setContentText("距离:"+currentDistance+"   速度:"+currentVeritory+"   步数:"+currentStep).setTicker("足迹").setOngoing(true).setContentIntent(getSportIntent()).build();
        notification.flags=Notification.FLAG_NO_CLEAR|Notification.FLAG_AUTO_CANCEL;
        return notification;
    }

    //获得足迹PendingIntent
    private PendingIntent getSportIntent(){
        Intent intent=new Intent(getApplicationContext(),SportActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,intent,0);
        return pendingIntent;
    }

    private void save(){
        mCurrentSport.setEndDate(new Date());
        mCurrentSport.setStep(currentStep);
        mDatabaseHelper.updateSport(mCurrentSport);
    }

}

