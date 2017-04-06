package com.earthgee.mymap.fragment.searchnearby;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.earthgee.mymap.ChoosePositionActivity;
import com.earthgee.mymap.R;

/**
 * Created by earthgee on 16/3/24.
 */
public class FoodSearchFragment extends AbsSearchFragment<FoodSearchAdapter> implements OnGetPoiSearchResultListener{

    private int mTotalPageNum;
    private PoiSearch mPoiSearch;

    @Override
    protected void onErrorLayoutInit(View view) {
        super.onErrorLayoutInit(view);
        TextView textView = (TextView) view.findViewById(R.id.error_content);
        textView.setText("暂无数据");
    }


    @Override
    protected void initData() {
        mPoiSearch=PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        onRefresh();
    }



    @Override
    protected FoodSearchAdapter getAdapter() {
        return new FoodSearchAdapter(getActivity(),((ChoosePositionActivity)getActivity()).getmCurrentLocation());
    }

    @Override
    protected void doRefresh() {
        mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword("美食").radius(2000).location(((ChoosePositionActivity)getActivity()).getmCurrentLocation()).pageNum(mCurrentPageNum).pageCapacity(10));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPoiSearch.destroy();
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if(poiResult==null&&mCurrentPageNum==0){
            setFragmentStatus(FRAGMENT_STATUC_ERROR);
            return;
        }

        if(poiResult==null){
            //提示没有数据
            onLoadDataSuccess();
            return;
        }

        mTotalPageNum=poiResult.getTotalPageNum();
        if(mCurrentPageNum==0){
            adapter.refresh(poiResult.getAllPoi(),poiResult.getAllAddr());
        }else{
            adapter.add(poiResult.getAllPoi(),poiResult.getAllAddr());
        }

        onLoadDataSuccess();
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    protected void loadMore() {
        mCurrentPageNum++;
        if(mCurrentPageNum>=mTotalPageNum){
            //提示
            return;
        }
        super.loadMore();
        doRefresh();
    }
}
