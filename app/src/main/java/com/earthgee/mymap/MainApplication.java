package com.earthgee.mymap;

import android.app.Application;

import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.mapapi.SDKInitializer;
import com.earthgee.mymap.util.CrashHandler;
import com.karumi.dexter.Dexter;

/**
 * Created by earthgee on 2016/2/8.
 */
public class MainApplication extends Application{

    BMapManager manager;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        Dexter.initialize(this);
        CrashHandler handler=CrashHandler.getInstance();
        handler.init(this);
        initEnigneManager();
    }

    private void initEnigneManager(){
        manager=new BMapManager(this);
        manager.init(new MKGeneralListener() {
            @Override
            public void onGetPermissionState(int i) {
                //kong
                System.out.print(i);
            }
        });
    }


}
