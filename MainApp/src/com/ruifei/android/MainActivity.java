package com.ruifei.android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;


import com.ruifei.android.fragment.ClassifyFragment;
import com.ruifei.android.fragment.FindFragment;
import com.ruifei.android.fragment.MySpaceFragment;
import com.ruifei.android.fragment.ShareFragment;
import com.ruifei.framework.fragment.BaseFragment;
import com.ruifei.framework.fragment.ManageFragment;

import test.TestFragemt1;
import test.TestFragment2;

public class MainActivity extends FragmentActivity implements BaseFragment.StartFragmentHelper,
        RadioGroup.OnCheckedChangeListener
{

    private Button button;
    private ManageFragment manageFragment;
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mRadioGroup = (RadioGroup)findViewById(R.id.main_tab);
        mRadioGroup.setOnCheckedChangeListener(this);


        manageFragment = new ManageFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.full_fragment_container,manageFragment)
                .commit();
        button = (Button)findViewById(R.id.ceshi);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestFragemt1 fragemt1 = new TestFragemt1(true);
                startFragment(fragemt1);
            }
        });
    }


    @Override
    public void startFragment(Fragment fragment) {
        if(manageFragment != null)
        {
            manageFragment.startFragment(fragment);
        }
    }

    public void startFragmentAndClearStack(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.main_container,fragment);
        ft.commit();
        /*if(manageFragment != null){
            manageFragment.startFragmentAndClearStack(fragment);
        }*/
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.find:
                FindFragment findFragment = new FindFragment();
                startFragmentAndClearStack(findFragment);
                break;
            case R.id.custom:
//                ClassifyFragment classifyFragment = new ClassifyFragment();
//                startFragmentAndClearStack(classifyFragment);
                TestFragemt1 fragment2 = new TestFragemt1();
                startFragmentAndClearStack(fragment2);
                break;
            case R.id.download:
                ShareFragment shareFragment = new ShareFragment();
                startFragmentAndClearStack(shareFragment);
                break;
            case R.id.myspace:
                MySpaceFragment mySpaceFragment = new MySpaceFragment();
                startFragmentAndClearStack(mySpaceFragment);
                break;
        }
    }
}
