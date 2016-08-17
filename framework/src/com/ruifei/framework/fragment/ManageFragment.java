package com.ruifei.framework.fragment;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.ruifei.framework.R;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class ManageFragment extends BaseFragment{

    public List<SoftReference<Fragment>> mStacks;
    private static ManageFragment mManageFragment = null;

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

        ft.add(R.id.fragment_container,fragment);

    }


}
