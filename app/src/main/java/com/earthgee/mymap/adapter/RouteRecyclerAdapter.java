package com.earthgee.mymap.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.earthgee.mymap.R;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 2016/2/12.
 */
public class RouteRecyclerAdapter extends RecyclerView.Adapter<RouteRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private SearchResult mSearchResult;
    private List<BikingRouteLine> mBikingList = null;
    private List<TransitRouteLine> mBusList = null;
    private List<DrivingRouteLine> mDriveList = null;
    private List<WalkingRouteLine> mWalkList = null;

    private SearchResultRecyclerAdapter.OnItemClickListener mOnItemClickListener;

    public RouteRecyclerAdapter(Context mContext, SearchResult mSearchResult) {
        this.mContext = mContext;
        this.mSearchResult = mSearchResult;
        if (mSearchResult instanceof BikingRouteResult) {
            mBikingList = ((BikingRouteResult) mSearchResult).getRouteLines();
        } else if (mSearchResult instanceof TransitRouteResult) {
            mBusList = ((TransitRouteResult) mSearchResult).getRouteLines();
        } else if (mSearchResult instanceof DrivingRouteResult) {
            mDriveList = ((DrivingRouteResult) mSearchResult).getRouteLines();
        } else {
            mWalkList = ((WalkingRouteResult) mSearchResult).getRouteLines();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_route, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mBikingList != null) {
            bindData(mBikingList, holder, position);
            return;
        }
        if (mBusList != null) {
            bindData2(mBusList, holder, position);
            return;
        }
        if (mDriveList != null) {
            bindData3(mDriveList, holder, position);
            return;
        }
        if (mWalkList != null) {
            bindData4(mWalkList, holder, position);
            return;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.itemView,holder.getLayoutPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mBikingList != null) {
            return mBikingList.size();
        }

        if (mBusList != null) {
            return mBusList.size();
        }

        if (mDriveList != null) {
            return mDriveList.size();
        }

        if (mWalkList != null) {
            return mWalkList.size();
        }

        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final CardView mCardView;

        @InjectView(R.id.test)
        TextView mTextView;

        public TextView getmTextView() {
            return mTextView;
        }

        public ViewHolder(CardView itemView) {
            super(itemView);
            mCardView = itemView;
            ButterKnife.inject(this, itemView);
        }
    }

    private void bindData(List<BikingRouteLine> list, ViewHolder viewHolder, int position) {
        viewHolder.getmTextView().setText(list.get(position).getTitle());
    }

    private void bindData2(List<TransitRouteLine> list, ViewHolder viewHolder, int position) {
        viewHolder.getmTextView().setText(list.get(position).getTitle());
    }

    private void bindData3(List<DrivingRouteLine> list, ViewHolder viewHolder, int position) {
        viewHolder.getmTextView().setText(list.get(position).getTitle());
    }

    private void bindData4(List<WalkingRouteLine> list, ViewHolder viewHolder, int position) {
        viewHolder.getmTextView().setText(list.get(position).getTitle());
    }

    public void setOnItemClickListener(SearchResultRecyclerAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
