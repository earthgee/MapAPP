package com.earthgee.mymap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.RouteStep;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.earthgee.mymap.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by earthgee on 2016/2/12.
 */
public class OneRouteRecyclerAdapter extends RecyclerView.Adapter<OneRouteRecyclerAdapter.ViewHolder>{

    private Context mContext;
    private RouteLine mRouteLine;
    private List<BikingRouteLine.BikingStep> mBikingLine;
    private List<TransitRouteLine.TransitStep> mBusLine;
    private List<DrivingRouteLine.DrivingStep> mDriveLine;
    private List<WalkingRouteLine.WalkingStep> mWalkLine;

    public OneRouteRecyclerAdapter(Context mContext,RouteLine routeLine){
        this.mContext=mContext;
        this.mRouteLine=routeLine;
        if(mRouteLine instanceof BikingRouteLine){
            mBikingLine=((BikingRouteLine)mRouteLine).getAllStep();
        }else if(mRouteLine instanceof TransitRouteLine){
            mBusLine=((TransitRouteLine)mRouteLine).getAllStep();
        }else if(mRouteLine instanceof DrivingRouteLine){
            mDriveLine=((DrivingRouteLine)mRouteLine).getAllStep();
        }else{
            mWalkLine=((WalkingRouteLine)mRouteLine).getAllStep();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder((TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route_line,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mRouteLine instanceof BikingRouteLine){
            bindData1(holder,position);
        }else if(mRouteLine instanceof TransitRouteLine){
            bindData2(holder,position);
        }else if(mRouteLine instanceof DrivingRouteLine){
            bindData3(holder,position);
        }else{
            bindData4(holder,position);
        }
    }

    @Override
    public int getItemCount() {
        return mRouteLine.getAllStep().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final TextView mTextView;

        public ViewHolder(TextView itemView) {
            super(itemView);
            mTextView=itemView;
        }
    }

    private void bindData1(ViewHolder viewHolder,int position){
        viewHolder.mTextView.setText(mBikingLine.get(position).getInstructions());
    }

    private void bindData2(ViewHolder viewHolder,int position){
        viewHolder.mTextView.setText(mBusLine.get(position).getInstructions());
    }

    private void bindData3(ViewHolder viewHolder,int position){
        viewHolder.mTextView.setText(mDriveLine.get(position).getInstructions());
    }

    private void bindData4(ViewHolder viewHolder,int position){
        viewHolder.mTextView.setText(mWalkLine.get(position).getInstructions());
    }
}
