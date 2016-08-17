package com.ruifei.framework.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruifei.framework.view.SlideView;

/**
 * Created by Administrator on 2016/8/15.
 */
public abstract class BaseFragment extends Fragment {

    private boolean canSlide = false;//默认不能侧滑移除
    public Context mContext;
    public BaseFragment() {
        mContext = getActivity().getApplicationContext();
    }

    public BaseFragment(boolean canSlide)
    {
        this.canSlide = canSlide;
        mContext = getActivity().getApplicationContext();
    }

    public abstract int getContainerLayoutId();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(canSlide==true){
            SlideView slideView = new SlideView(mContext);
            return inflater.inflate(getContainerLayoutId(),slideView,true);
        }else{
            return inflater.inflate(getContainerLayoutId(),null);
        }
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUi();
        loadData();
    }

    public void initUi(){}
    public void loadData(){}

}
