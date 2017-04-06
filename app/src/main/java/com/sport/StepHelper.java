package com.sport;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

/**
 * Created by earthgee on 2016/4/22.
 */
public class StepHelper implements SensorEventListener{

    final int valueNum=4;
    //用于存放计算阈值的波峰波谷差值
    float[] tempValue=new float[valueNum];
    int tempCount=0;
    //持续上升次数
    int countinueUpCount=0;
    int countinueFormerCount=0;

    float peakOfWave=0;
    float valleyOfWave=0;
    //此次波峰的时间
    long timeOfThisPeak=0;
    //上次波峰的时间
    long timeOfLastPeak=0;

    long timeNow=0;

    final float initialValue= (float) 1.7;

    //上次传感器的值
    float gravityOld=0;
    //是否上升的标志位
    private boolean isDirectionup=false;
    //上一点的状态
    private boolean lastStatus;
    //三个方向加速度的叠加
    private float average=0;
    //阈值(动态改变)
    float threadValue= (float) 2.0;

    OnSensorChangeListener onSensorChangeListener;

    public interface OnSensorChangeListener{
        void onChange();
    }

    public void setOnSensorChangeListener(OnSensorChangeListener onSensorChangeListener) {
        this.onSensorChangeListener = onSensorChangeListener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("earthgee2","receive event");
        Sensor sensor=event.sensor;
            if(sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                calcStep(event);
        }
    }

    private void calcStep(SensorEvent event){
        average= (float) Math.sqrt(Math.pow(event.values[0],2)+Math.pow(event.values[1],2)+Math.pow(event.values[2],2));
        Log.d("earthgee3","average="+average);
        detectorNewStep(average);
    }

    private void detectorNewStep(float average){
        if(gravityOld==0){
            gravityOld=average;
        }else{
            //是波峰
            if(detectorPeak(average,gravityOld)){
                timeOfLastPeak=timeOfThisPeak;
                timeNow=System.currentTimeMillis();
                if(timeNow-timeOfLastPeak>200&&timeNow-timeOfLastPeak<2000&&peakOfWave-valleyOfWave>=threadValue){
                    timeOfThisPeak=timeNow;
                    //一步
                    if(onSensorChangeListener!=null){
                        Log.d("earthgee3","one step");
                        onSensorChangeListener.onChange();
                    }
                }
                if(timeNow-timeOfLastPeak>200&&peakOfWave-valleyOfWave>=initialValue){
                    timeOfThisPeak=timeNow;
                    threadValue=calcthreadValue(peakOfWave-valleyOfWave);
                }
            }
        }
        gravityOld=average;
    }

    /**
     * 检测波峰
     * @param newValue
     * @param oldValue
     * @return
     */
    private boolean detectorPeak(float newValue,float oldValue){
        lastStatus=isDirectionup;
        //给isDirevtionUp赋值,true为上升的状态
        if(newValue>=oldValue){
            isDirectionup=true;
            countinueUpCount++;
        }else{
            countinueFormerCount=countinueUpCount;
            countinueUpCount=0;
            isDirectionup=false;
        }

        if(!isDirectionup&&lastStatus&&countinueFormerCount >= 2 && oldValue >= 11.76 && oldValue < 19.6){
            peakOfWave=oldValue;
            Log.d("earthgee3","peakOfWave is:"+peakOfWave);
            return true;
        }else if(!lastStatus&&isDirectionup){
            valleyOfWave=oldValue;
            return false;
        }else{
            return false;
        }
    }

    //计算阈值

    /**
     * 通过波峰和波谷的差值计算
     * @param value
     * @return
     */
    private float calcthreadValue(float value){
        float tempThread=threadValue;
        if(tempCount<valueNum){
            tempValue[tempCount]=value;
            tempCount++;
        }else {
            tempCount--;
            tempThread = averageValue(tempValue, valueNum);
            for (int i = 1; i < valueNum; i++) {
                tempValue[i - 1] = tempValue[i];
            }
            tempValue[valueNum - 1] = value;
        }
        return tempThread;
    }

    public float averageValue(float value[], int n) {
        float ave = 0;
        for (int i = 0; i < n; i++) {
            ave += value[i];
        }
        ave = ave / valueNum;
        if (ave >= 8)
            ave = (float) 4.3;
        else if (ave >= 7 && ave < 8)
            ave = (float) 3.3;
        else if (ave >= 4 && ave < 7)
            ave = (float) 2.3;
        else if (ave >= 3 && ave < 4)
            ave = (float) 2.0;
        else {
            ave = (float) 1.3;
        }
        Log.d("earthgee3","ave="+ave);
        return ave;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

