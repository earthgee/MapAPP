package com.sport;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.earthgee.mymap.AbsBaseActivity;
import com.earthgee.mymap.R;
import com.sport.entity.Location;

/**
 * Created by earthgee on 16/4/13.
 */
public class SportActivity extends AbsBaseActivity implements Handler.Callback{

    private IStepManager stepManager;

    private Handler handler=new Handler(this);

    ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("earthgee","client bind service");
            stepManager=IStepManager.Stub.asInterface(service);
            try {
                stepManager.registerStepListener(mStepUpdateListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("earthgee","client unbind service");
            try {
                stepManager.unregisterStepListener(mStepUpdateListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sport;
    }

    public static void jumpToSportActivity(Context context){
        Intent intent=new Intent(context,SportActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=new Intent(this,SportService.class);
        startService(intent);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed() {
        Dialog dialog=new AlertDialog.Builder(this).setMessage("是否结束记录").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //保存结束日期
                //需要使用进程间通信,须在service中定义一个方法,因为
                //结束service
                Intent intent=new Intent(SportActivity.this,SportService.class);
                stopService(intent);
                finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    private IStepListener mStepUpdateListener=new IStepListener.Stub(){

        @Override
        public void notifyStep(int step) throws RemoteException {
            Log.d("earthgee2", "client step update");
            handler.sendMessage(Message.obtain(handler, 1, step));
        }
    };

    @Override
    public boolean handleMessage(Message msg) {
        int step= (int) msg.obj;
        //通知fragment更新ui
        SportFragment fragment= (SportFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        fragment.updateStep(step);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if(stepManager!=null){
                stepManager.registerStepListener(mStepUpdateListener);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.print("aaa");
    }

    @Override
    protected void onPause() {
        if(stepManager!=null){
            try {
                if(stepManager!=null){
                    stepManager.unregisterStepListener(mStepUpdateListener);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        super.onPause();
    }
}
