package com.earthgee.mymap.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;

import com.earthgee.mymap.R;

/**
 * Created by earthgee on 2016/2/9.
 */
public abstract class AbsBaseFragment extends Fragment{
    public static final int FRAGMENT_STATUS_SUCCESS=0x1;
    public static final int FRAGMENT_STATUS_LOADING=0x0;
    public static final int FRAGMENT_STATUC_ERROR=0x2;

    //控制页面显示内容
    private ViewAnimator mViewAnimator;

    //主页面
    private View mContentView;

    private int mFragmentStatus=FRAGMENT_STATUS_LOADING;

    protected void onLoadingLayoutInit(View view){
    }

    protected void onErrorLayoutInit(View view){

    }

    protected abstract int getContentLayout();

    protected void onContentLayoutInit(View view){

    }

    public int getmFragmentStatus() {
        return mFragmentStatus;
    }

    protected void setFragmentStatus(int fragmentStatus){
        if(mViewAnimator==null) return;
        mFragmentStatus=fragmentStatus;
        switch (mFragmentStatus){
            case FRAGMENT_STATUS_LOADING:
                mViewAnimator.setDisplayedChild(0);
                break;
            case FRAGMENT_STATUS_SUCCESS:
                mViewAnimator.setDisplayedChild(1);
                break;
            case FRAGMENT_STATUC_ERROR:
                mViewAnimator.setDisplayedChild(2);
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.base_fragment,container,false);
        mViewAnimator= (ViewAnimator) rootView.findViewById(R.id.e_view_animator);
        View view;

        view=inflater.inflate(R.layout.loading,null,false);
        onLoadingLayoutInit(view);
        mViewAnimator.addView(view);

        mContentView=inflater.inflate(getContentLayout(),null,false);
        onContentLayoutInit(mContentView);
        mViewAnimator.addView(mContentView);

        view=inflater.inflate(R.layout.error,null,false);
        onErrorLayoutInit(view);
        mViewAnimator.addView(view);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init(savedInstanceState, mContentView);
    }

    protected abstract void init(Bundle savedInstanceState,View contentView);


}










