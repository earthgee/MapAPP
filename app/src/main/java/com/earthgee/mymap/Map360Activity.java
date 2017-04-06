package com.earthgee.mymap;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.lbsapi.panoramaview.PanoramaViewListener;

/**
 * Created by earthgee on 16/3/24.
 */
public class Map360Activity extends AbsBaseActivity{

    @Override
    protected int getLayoutId() {
        return R.layout.activity_360;
    }

    public String uid;

    public String getUid() {
        return uid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //m360View= (PanoramaView) findViewById(R.id.view);
        uid=getIntent().getStringExtra("uid");
        //initPhoto();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}


