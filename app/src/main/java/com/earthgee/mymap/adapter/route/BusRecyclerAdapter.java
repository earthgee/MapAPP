package com.earthgee.mymap.adapter.route;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.VehicleInfo;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.earthgee.mymap.R;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 2016/3/16.
 */
public class BusRecyclerAdapter extends RecyclerView.Adapter<BusRecyclerAdapter.ViewHolder>{

    private Context mContext;
    private List<TransitRouteLine> mList;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public BusRecyclerAdapter(Context mContext,List<TransitRouteLine> mList){
        this.mContext=mContext;
        this.mList=mList;
    }

    @Override
    public BusRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView= (CardView) LayoutInflater.from(mContext).inflate(R.layout.cardview_bus,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(final BusRecyclerAdapter.ViewHolder holder, int position) {
        TransitRouteLine line=mList.get(position);
        List<TransitRouteLine.TransitStep> transitSteps=line.getAllStep();
        //站点显示
        StringBuffer titleBuffer=new StringBuffer();
        //站数
        int stepNumber=0;
        //时间
        int stepSecond=0;
        //步行距离
        int walkDistance=0;

        String lastStation="";

        for(TransitRouteLine.TransitStep transitStep:transitSteps){
            stepSecond+=transitStep.getDuration();
            if(transitStep.getStepType()== TransitRouteLine.TransitStep.TransitRouteStepType.WAKLING){
                walkDistance+=transitStep.getDistance();
            }else{
                VehicleInfo carInfo=transitStep.getVehicleInfo();
                if(!carInfo.getTitle().equals(lastStation)){
                    titleBuffer.append(carInfo.getTitle()).append("->");
                    lastStation=carInfo.getTitle();
                }
                stepNumber+=carInfo.getPassStationNum();
            }
        }

        holder.getmBusTitle().setText(titleBuffer.toString().substring(0,titleBuffer.toString().lastIndexOf("->")));
        holder.getmBusStep().setText(String.valueOf(stepNumber)+"站");
        holder.getmBusTime().setText(String.valueOf(stepSecond/60)+"分钟");
        holder.getmWalkDistance().setText("步行"+String.valueOf(walkDistance)+"米");

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

        @InjectView(R.id.bus_title)
        TextView mBusTitle;
        @InjectView(R.id.bus_time)
        TextView mBusTime;
        @InjectView(R.id.bus_step)
        TextView mBusStep;
        @InjectView(R.id.walk_distance)
        TextView mWalkDistance;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }

        public TextView getmBusTitle() {
            return mBusTitle;
        }

        public TextView getmBusTime() {
            return mBusTime;
        }

        public TextView getmBusStep() {
            return mBusStep;
        }

        public TextView getmWalkDistance() {
            return mWalkDistance;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}
