package com.earthgee.mymap.fragment.searchnearby;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiAddrInfo;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.earthgee.mymap.Map360Activity;
import com.earthgee.mymap.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 16/3/24.
 */
public class FoodSearchAdapter extends AbsSearchAdapter<FoodSearchAdapter.ViewHolder,PoiInfo> {

    private List<PoiAddrInfo> mPoiAddrInfos;
    private LatLng mCurrentLocation;

    public FoodSearchAdapter(Context context,LatLng location) {
        super(context);
        mCurrentLocation=location;
    }

    protected void refresh(List<PoiInfo> list,List<PoiAddrInfo> mAddrList) {
        if(mAddrList==null){
            mAddrList=new ArrayList<>();
        }
        this.mPoiAddrInfos=mAddrList;
        refresh(list);
    }

    protected void add(List<PoiInfo> list,List<PoiAddrInfo> mAddrList){
        if(this.list!=null){
            this.list.addAll(list);
        }
        if(mAddrList!=null){
            this.mPoiAddrInfos.addAll(mAddrList);
        }
        notifyDataSetChanged();
    }

    @Override
    protected ViewHolder doCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_foot_detail,parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void doBindViewHolder(ViewHolder holder, int position) {
        PoiInfo poiInfo=list.get(position);
        holder.getmTelDetail().setText(poiInfo.phoneNum);
        holder.getDistance().setText("距离:" + (int) DistanceUtil.getDistance(mCurrentLocation, poiInfo.location) + "米");
        final String uid=poiInfo.uid;

        holder.itemView.setTag(uid);
        PoiSearch poiSearch=PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new OnSimpleGetPoiSearchResultListener(poiSearch,holder,uid));
        poiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(uid));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入全景地图
                Intent intent=new Intent(mContext, Map360Activity.class);
                intent.putExtra("uid",uid);
                mContext.startActivity(intent);
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.food_name)
        TextView mFoodName;
        @InjectView(R.id.ratingbar)
        RatingBar mRatingBar;
        @InjectView(R.id.tel_detail)
        TextView mTelDetail;
        @InjectView(R.id.distance)
        TextView distance;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }

        public RatingBar getmRatingBar() {
            return mRatingBar;
        }

        public TextView getmFoodName() {
            return mFoodName;
        }

        public TextView getmTelDetail() {
            return mTelDetail;
        }

        public TextView getDistance() {
            return distance;
        }
    }

    class OnSimpleGetPoiSearchResultListener implements OnGetPoiSearchResultListener{

        ViewHolder viewHolder;
        private String uid;
        private PoiSearch poiSearch;

        public OnSimpleGetPoiSearchResultListener(PoiSearch poiSearch,ViewHolder viewHolder,String uid){
            this.poiSearch=poiSearch;
            this.viewHolder=viewHolder;
            this.uid=uid;
        }

        @Override
        public void onGetPoiResult(PoiResult poiResult) {

        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            poiSearch.destroy();

            if(viewHolder.itemView.getTag().equals(uid)){
                if(poiDetailResult.error!= SearchResult.ERRORNO.NO_ERROR){
                    return;
                }

                viewHolder.getmFoodName().setText(poiDetailResult.getName());
                viewHolder.getmRatingBar().setRating((float) poiDetailResult.getOverallRating());
            }
        }
    }
}
