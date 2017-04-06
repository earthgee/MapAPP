package com.earthgee.mymap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.earthgee.mymap.R;

import java.util.ArrayList;

/**
 * Created by earthgee on 2016/3/20.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{

    private DrawerMenuClickListener menuClickListener;

    ArrayList<String> mMenuItems;
    private Context mContext;
    private int currentPosition=-1;

    public MenuAdapter(Context context){
        mMenuItems=new ArrayList<>();
        mMenuItems.add("足迹");
        mMenuItems.add("离线地图");
        mContext=context;
    }

    public void setMenuClickListener(DrawerMenuClickListener menuClickListener) {
        this.menuClickListener = menuClickListener;
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView rootView= (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_route_line,parent,false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MenuAdapter.ViewHolder holder, final int position) {
        holder.mTextView.setText(mMenuItems.get(position));

        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通知MainActivity
                if(menuClickListener!=null){
                    menuClickListener.onItemClicked(position);
                }
            }
        });
    }

    public interface DrawerMenuClickListener{
        void onItemClicked(int position);
    }

    @Override
    public int getItemCount() {
        return mMenuItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView= (TextView) itemView;
        }

        public TextView getmTextView() {
            return mTextView;
        }
    }
}
