package com.earthgee.mymap.util;

import android.support.v4.app.Fragment;

import com.earthgee.mymap.adapter.LocationMode;
import com.earthgee.mymap.fragment.MapFragment;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

/**
 * Created by earthgee on 2016/3/19.
 */
public class LocationPermissionListener implements PermissionListener {

    private MapFragment mFragment;

    public LocationPermissionListener(MapFragment fragment){
        mFragment=fragment;
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        mFragment.startLocation(true);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        mFragment.startLocation(false);
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

    }
}
