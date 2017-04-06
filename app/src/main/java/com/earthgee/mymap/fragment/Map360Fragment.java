package com.earthgee.mymap.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.lbsapi.model.BaiduPoiPanoData;
import com.baidu.lbsapi.panoramaview.PanoramaRequest;
import com.baidu.lbsapi.panoramaview.PanoramaView;
import com.baidu.lbsapi.panoramaview.PanoramaViewListener;
import com.earthgee.mymap.Map360Activity;
import com.earthgee.mymap.R;

/**
 * Created by earthgee on 16/3/25.
 */
public class Map360Fragment extends AbsBaseFragment{
    private PanoramaView m360View;

    private String uid;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_360;
    }

    @Override
    protected void init(Bundle savedInstanceState, View contentView) {
        m360View= (PanoramaView) contentView.findViewById(R.id.view);
        uid=((Map360Activity)getActivity()).getUid();
        requestData();
    }

    @Override
    protected void onErrorLayoutInit(View view) {
        TextView content= (TextView) view.findViewById(R.id.error_content);
        content.setText("此地点没有数据");
    }

    private void requestData(){
        PanoramaRequest request=PanoramaRequest.getInstance(getActivity());
        BaiduPoiPanoData data=request.getPanoramaInfoByUid(uid);
        if(data.hasInnerPano()){
            initPhoto(PanoramaView.PANOTYPE_INTERIOR);
        }else if(data.hasStreetPano()){
            initPhoto(PanoramaView.PANOTYPE_STREET);
        }else{
            setFragmentStatus(FRAGMENT_STATUC_ERROR);
        }
    }

    private void initPhoto(int type){
        setFragmentStatus(FRAGMENT_STATUS_SUCCESS);
        m360View.setShowTopoLink(true);

        m360View.setPanoramaViewListener(new PanoramaViewListener() {
            @Override
            public void onLoadPanoramaBegin() {
                System.out.print("aaa");
            }

            @Override
            public void onLoadPanoramaEnd(String s) {
                System.out.print(s);
            }

            @Override
            public void onLoadPanoramaError(String s) {
                System.out.print(s);
            }
        });

        m360View.setPanoramaImageLevel(PanoramaView.ImageDefinition.ImageDefinitionHigh);
        m360View.setPanoramaByUid(uid,type);
        m360View.setIndoorAlbumVisible();
    }

    @Override
    public void onPause() {
        super.onPause();
        m360View.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        m360View.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        m360View.destroy();
    }
}
