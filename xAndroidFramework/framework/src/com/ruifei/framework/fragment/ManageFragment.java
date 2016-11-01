package com.ruifei.framework.fragment;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ruifei.framework.R;
import com.ruifei.framework.view.SlideView;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class ManageFragment extends BaseFragment implements SlideView.IOnFinishListener{

    public List<SoftReference<Fragment>> mStacks;
    private static ManageFragment mManageFragment = null;
    private Button button;

    public ManageFragment() {
        init();
    }


    private void init()
    {
        mStacks = new ArrayList<SoftReference<Fragment>>();
    }

    @Override
    public int getContainerLayoutId() {
        return R.layout.manage_fragment_layout;
    }

    public void startFragment(Fragment fragment)
    {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_in,R.anim.fragment_slide_out,
                R.anim.fragment_slide_in,R.anim.fragment_slide_out);
        ft.add(R.id.fragment_container,fragment);
        mStacks.add(new SoftReference<Fragment>(fragment));
        ft.commitAllowingStateLoss();
    }

    public void startFragmentAndClearStack(Fragment fragment)
    {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        /*ft.setCustomAnimations(R.anim.fragment_slide_in,R.anim.fragment_slide_out,
                R.anim.fragment_slide_in,R.anim.fragment_slide_out);*/
        ft.replace(R.id.fragment_container,fragment);
        ft.commitAllowingStateLoss();
        clearStack();
        mStacks.add(new SoftReference<Fragment>(fragment));
    }

    public void clearStack()
    {
        Iterator<SoftReference<Fragment>> iterator = mStacks.iterator();
        while (iterator.hasNext())
        {
            SoftReference<Fragment> softFra = iterator.next();
            iterator.remove();
        }
        if(mStacks==null)mStacks = new ArrayList<SoftReference<Fragment>>();
    }

    @Override
    public void initUi() {
        super.initUi();
    }

    //提供类似于返回键的返回功能.
    public boolean onBackPress()
    {
        Log.i("xzf","mStacks size:"+mStacks.size());
        if(mStacks.size()>0){
            removeTopFragment();
            if(mStacks.size()>0) {
                return showTopFragment();
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    public void removeTopFragment()
    {
        Fragment f = mStacks.get(mStacks.size()-1).get();
        mStacks.remove(mStacks.size()-1);
        FragmentManager fm = getChildFragmentManager();
        fm.beginTransaction().setCustomAnimations(R.anim.fragment_slide_in,R.anim.fragment_slide_out,
                R.anim.fragment_slide_in,R.anim.fragment_slide_out)
                .remove(f).commitAllowingStateLoss();
    }

    public void removeStackTop()
    {
        mStacks.remove(mStacks.size()-1);
    }

    public boolean showTopFragment()
    {
        if(mStacks.size() == 0)return false;
        if(mStacks.get(mStacks.size()-1) != null) {
            Fragment f = mStacks.get(mStacks.size()-1).get();
            FragmentManager fm = getChildFragmentManager();
            fm.beginTransaction().show(f).commit();
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void loadData() {
        super.loadData();
    }

    @Override
    public boolean onFinish() {
        Log.i("xzf","on finish");
        removeTopFragment();
        showTopFragment();
        return true;
    }
}
