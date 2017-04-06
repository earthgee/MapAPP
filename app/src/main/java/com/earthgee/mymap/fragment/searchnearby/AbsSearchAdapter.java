package com.earthgee.mymap.fragment.searchnearby;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by earthgee on 16/3/24.
 */
public abstract class AbsSearchAdapter<VH extends RecyclerView.ViewHolder,E> extends RecyclerView.Adapter<VH>{

    private OnItemClickListener onItemClickListener;
    protected Context mContext;
    List<E> list;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AbsSearchAdapter(Context context){
        this.mContext=context;
        this.list=new ArrayList<>();
    }

    public AbsSearchAdapter(Context context,List<E> list){
        this.mContext=context;
        this.list=list;
    }

    protected void refresh(List<E> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return doCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        doBindViewHolder(holder,position);
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    protected abstract VH doCreateViewHolder(ViewGroup parent, int viewType);
    protected abstract void doBindViewHolder(VH holder,int position);

    interface OnItemClickListener{
        void onItemClick(int position);
    }


}
