package com.earthgee.mymap.adapter.route;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.earthgee.mymap.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 2016/3/16.
 */
public class DriveRecyclerAdapter extends RecyclerView.Adapter<DriveRecyclerAdapter.ViewHolder>{

    private Context mContext;
    private List<DrivingRouteLine> mList;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener=onItemClickListener;
    }

    public DriveRecyclerAdapter(Context context,List<DrivingRouteLine> list){
        this.mContext=context;
        this.mList=list;
    }

    @Override
    public DriveRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView= (CardView) LayoutInflater.from(mContext).inflate(R.layout.cardview_drive,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(final DriveRecyclerAdapter.ViewHolder holder, int position) {
        holder.getmDriveTime().setText(String.valueOf(mList.get(position).getDuration()/60)+"分钟");
        holder.getmDriveDistance().setText(String.valueOf(mList.get(position).getDistance()/1000)+"km");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(holder.itemView,holder.getLayoutPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @InjectView(R.id.drive_time)
        TextView mDriveTime;
        @InjectView(R.id.drive_distance)
        TextView mDriveDistance;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }

        public TextView getmDriveDistance() {
            return mDriveDistance;
        }

        public TextView getmDriveTime() {
            return mDriveTime;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}
