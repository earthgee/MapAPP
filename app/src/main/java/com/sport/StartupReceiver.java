package com.sport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by earthgee on 16/4/13.
 * 收听开机广播使运动轨迹检测一直存在,需手动取消
 */
public class StartupReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(context);
        boolean isOn=prefs.getBoolean(SportService.PREF_IS_ALARM_ON,false);
        SportService.setServiceAlarm(context,isOn);
    }

}
