package com.ruifei.framework.fragment;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruifei.framework.view.SlideView;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/8/15.
 */
public abstract class BaseFragment extends Fragment {

    private boolean canSlide = false;//默认不能侧滑移除
    public Context mContext;
    private View mContainerView;
    public BaseFragment() {

    }

    public BaseFragment(boolean canSlide)
    {
        this.canSlide = canSlide;
    }

    public abstract int getContainerLayoutId();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getActivity().getApplicationContext();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(canSlide==true){
            SlideView slideView = new SlideView(getActivity());
            mContainerView = inflater.inflate(getContainerLayoutId(),slideView,true);

        }else{
            mContainerView = inflater.inflate(getContainerLayoutId(),null);
        }
        return mContainerView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mContext = getActivity().getApplicationContext();
        initUi();
        loadData();
    }

    public void startFragment(Fragment fragment)
    {
        Activity activity = getActivity();
        if(activity instanceof StartFragmentHelper)
        {
            ((StartFragmentHelper) activity).startFragment(fragment);
        }
    }

    public void initUi(){}
    public void loadData(){}

    public interface StartFragmentHelper
    {
        public void startFragment(Fragment fragment);
    }

    public View findViewById(int id) {

        View view = null;
        if (mContainerView != null) {
            view = mContainerView.findViewById(id);
        }else{
            view = getActivity().findViewById(id);
        }
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
