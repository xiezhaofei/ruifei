package com.ruifei.android;

import android.app.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import com.ruifei.framework.fragment.BaseFragment;
import com.ruifei.framework.fragment.ManageFragment;

import test.TestFragemt1;

public class MainActivity extends FragmentActivity implements BaseFragment.StartFragmentHelper{

    private Button button;
    private ManageFragment manageFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manageFragment = new ManageFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container,manageFragment)
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
        if(manageFragment!=null)
        {
            manageFragment.startFragment(fragment);
        }
    }
}
