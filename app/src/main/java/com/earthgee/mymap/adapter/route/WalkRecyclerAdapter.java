package com.earthgee.mymap.adapter.route;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.TaxiInfo;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.earthgee.mymap.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 2016/3/16.
 */
public class WalkRecyclerAdapter extends RecyclerView.Adapter<WalkRecyclerAdapter.ViewHolder>{

    private Context mContext;
    private List<WalkingRouteLine> mList;
    private TaxiInfo mTaxiInfo;
    private WalkRecyclerAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public WalkRecyclerAdapter(Context context,List<WalkingRouteLine> list,TaxiInfo taxiInfo){
        this.mContext=context;
        this.mList=list;
        this.mTaxiInfo=taxiInfo;
    }

    @Override
    public WalkRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView= (CardView) LayoutInflater.from(mContext).inflate(R.layout.cardview_walk,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(final WalkRecyclerAdapter.ViewHolder holder, final int position) {
        holder.getmWalkTime().setText(String.valueOf(mList.get(position).getDuration()/60)+"分钟");
        holder.getmWalkDistance().setText(String.valueOf(mList.get(position).getDistance()/1000)+"km");
        if(mTaxiInfo!=null){
            holder.getmTaxiPrice().setText("打车约"+String.valueOf(mTaxiInfo.getTotalPrice())+"元");
        }else{
            holder.getmTaxiPrice().setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(holder.itemView,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder{

        @InjectView(R.id.walk_time)
        TextView mWalkTime;
        @InjectView(R.id.walk_distance)
        TextView mWalkDistance;
        @InjectView(R.id.taxi_price)
        TextView mTaxiPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }

        public TextView getmWalkTime() {
            return mWalkTime;
        }

        public TextView getmWalkDistance() {
            return mWalkDistance;
        }

        public TextView getmTaxiPrice() {
            return mTaxiPrice;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

}
