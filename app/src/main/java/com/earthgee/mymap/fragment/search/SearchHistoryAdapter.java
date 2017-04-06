package com.earthgee.mymap.fragment.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.earthgee.mymap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by earthgee on 16/3/26.
 */
public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>{

    private Context context;
    private List<String> historys;
    private OnItemClickListener mOnItemClickListener;

    SearchHistoryAdapter(Context context,List<String> historys){
        this.context=context;
        if(historys==null){
            this.historys=new ArrayList<>();
        }else{
            this.historys=historys;
        }
    }

    public SearchHistoryAdapter getInstance(){
        return this;
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_route_line,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ((TextView)holder.itemView).setText(historys.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener!=null){
                    mOnItemClickListener.onItemClicked(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return historys.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener{
        void onItemClicked(int position);
    }

}
