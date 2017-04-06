package com.earthgee.mymap.util;

import android.content.Context;

/**
 * Created by earthgee on 2016/3/20.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler{

    private static CrashHandler INSTANCE=new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler(){

    }

    public static CrashHandler getInstance(){
        return INSTANCE;
    }

    public void init(Context mContext){
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        System.out.print(ex);
    }
}
