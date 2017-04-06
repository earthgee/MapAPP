package com.earthgee.mymap.adapter.route;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.route.BikingRouteLine;
import com.earthgee.mymap.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 2016/3/16.
 */
public class BikingRecyclerAdapter extends RecyclerView.Adapter<BikingRecyclerAdapter.ViewHolder>{

    private OnItemClickListener mOnItemClickListener;
    private Context mContext;
    private List<BikingRouteLine> mList;

    public BikingRecyclerAdapter(Context context,List<BikingRouteLine> list){
        this.mContext=context;
        this.mList=list;
    }

    @Override
    public BikingRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView= (CardView) LayoutInflater.from(mContext).inflate(R.layout.cardview_biking,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(final BikingRecyclerAdapter.ViewHolder holder, int position) {
        bindData(holder,position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClick(holder.itemView,holder.getLayoutPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void bindData(ViewHolder viewHolder,int position){
        TextView bikingTime=viewHolder.getmBikingTime();
        TextView bikingDistance=viewHolder.getmBikingDistance();
        bikingTime.setText(String.valueOf(mList.get(position).getDuration()/60)+"分钟");
        bikingDistance.setText(String.valueOf(mList.get(position).getDistance()/1000)+"km");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @InjectView(R.id.biking_time)
        TextView mBikingTime;
        @InjectView(R.id.biking_distance)
        TextView mBikingDistance;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }

        public TextView getmBikingTime() {
            return mBikingTime;
        }

        public TextView getmBikingDistance() {
            return mBikingDistance;
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}
