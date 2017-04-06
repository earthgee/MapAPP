package com.earthgee.mymap.fragment.searchnearby;

import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.earthgee.mymap.ChoosePositionActivity;
import com.earthgee.mymap.R;
import com.earthgee.mymap.util.DividerItemDecoration;

/**
 * Created by earthgee on 16/3/25.
 */
public class BusSearchFragment extends AbsSearchFragment<BusSearchAdapter> implements OnGetPoiSearchResultListener{

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
        mRecycleView.addItemDecoration(new DividerItemDecoration(getActivity()));
        mPoiSearch=PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        onRefresh();
    }

    @Override
    protected BusSearchAdapter getAdapter() {
        return new BusSearchAdapter(getActivity(),((ChoosePositionActivity)getActivity()).getmCurrentLocation());
    }

    @Override
    protected void doRefresh() {
        mPoiSearch.searchNearby(new PoiNearbySearchOption().keyword("公交站").radius(2000).location(((ChoosePositionActivity)getActivity()).getmCurrentLocation()).pageNum(mCurrentPageNum).pageCapacity(10));
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
